package com.example.offloader.offloader

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.offloader.offloader.data.models.*
import com.example.offloader.offloader.network.web_socket.OffloadWebSocketManager
import com.example.offloader.offloader.util.*
import com.google.gson.Gson
import retrofit2.HttpException
import kotlin.reflect.KFunction


class OffloadManager(val context: Context) {

    val systemResourceMonitor = SystemResourceMonitor(context)

    val functions = HashMap<Int, KFunction<Any>>()

    private val uploadWebSocketManager = OffloadWebSocketManager(
        Constants.URLs.UPLOAD_WEB_SOCKET_URL,
        onConnectSuccess = {},
        onMessage = {}
    ).apply {
        initWebSock()
    }

    private val grabWebSocketManager = OffloadWebSocketManager(
        Constants.URLs.GRAB_WEB_SOCKET_URL,
        onConnectSuccess = {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.postDelayed(
                object : Runnable {
                    override fun run() {
                        getNewTask()
                        mainHandler.postDelayed(this, 5000)
                    }
                },
                0)
        },
        onMessage = {
            println("Received in grab: $it")
            executeReceivedTask(Gson().fromJson(it, ReceivedTask::class.java))
        }
    ).apply {
        initWebSock()
    }

    fun execute(hashCode: Int, vararg params: Any?) = safeFunctionCall<Any> {
        val function = functions[hashCode]!!
        val deferredFunction = DeferredFunction(function, *params)
        deferredFunction.invoke()
    }

    fun executeRemotely(hashCode: Int, vararg params: Any?) {
        val toUpload = buildUploadTaskAsJson(context, hashCode, *params)
        uploadWebSocketManager.sendMessage(toUpload)
    }

    fun register(function: KFunction<Any>): Int {
        val hashCode = function.hashCode()
        functions[hashCode] = function
        return hashCode
    }



    var wasExecuted = true


    private fun getNewTask() {
        val result = if(wasExecuted) safeFunctionCall {
            val readyMsg = ReadyForWork("test cluster")
            grabWebSocketManager.sendMessage(Gson().toJson(readyMsg))
            wasExecuted = false
        } else null

        if (result == null) println("Offloader is full")

        when (result) {
            is LocalResource.Success -> println("Getting new task...")
            is LocalResource.Failure -> println(result.throwable.message)
        }
    }

    fun executeReceivedTask(task: ReceivedTask) {
        println("Executing remote task...")
        val hash = task.hash.toInt()


        val function = functions[hash]!!
        val args = task.getArgs()
        val deferredFunction = DeferredFunction(function, *args)
        val funcResult = deferredFunction.invoke<Any>()


        val taskResult = TaskResult(
            id = task.id,
            result = funcResult,
            task_id = task.task_id
        )

        val converted = Gson().toJson(taskResult)
        println("Converted : $converted")
        grabWebSocketManager.sendMessage(converted)
        wasExecuted = true
    }

    fun freeSockets() {
        uploadWebSocketManager.close()
        grabWebSocketManager.close()
    }


    private fun <T> safeFunctionCall(
        funCall: () -> T
    ): LocalResource<T> {
        return try {
            if (!systemResourceMonitor.isBatteryConditionOkay()) throw BatteryLowException("low battery!")
            if (!systemResourceMonitor.networkStateMonitor.isUnmetered) throw NetworkUnstableException(
                "network metered"
            )
            LocalResource.Success(funCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    LocalResource.Failure(
                        true,
                        false,
                        false,
                        throwable
                    )
                }
                is BatteryLowException -> {
                    LocalResource.Failure(
                        false,
                        true,
                        false,
                        throwable
                    )
                }
                is NetworkUnstableException -> {
                    LocalResource.Failure(
                        false,
                        false,
                        true,
                        throwable
                    )
                }
                else -> {
                    LocalResource.Failure(
                        false,
                        false,
                        false,
                        throwable
                    )
                }
            }
        }
    }

}
