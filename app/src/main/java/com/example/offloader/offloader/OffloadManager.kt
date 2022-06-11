package com.example.offloader.offloader

import android.content.Context
import com.example.offloader.offloader.util.BatteryLowException
import com.example.offloader.offloader.util.NetworkUnstableException
import com.example.offloader.offloader.util.LocalResource
import com.google.gson.Gson
import retrofit2.HttpException
import kotlin.reflect.KFunction


class OffloadManager(context: Context) {

    val systemResourceMonitor = SystemResourceMonitor(context)

    private val functions = HashMap<Int, KFunction<Any>>()

    fun execute(hashCode: Int, vararg params: Any?) = safeFunctionCall<Any> {
        val function = functions[hashCode]!!
        val converted = params.map { Gson().toJson(it) }.toTypedArray()
        println("converted:")
        converted.forEach { println(it) }
        val unconverted = converted.map { Gson().fromJson(it, Any::class.java) }.toTypedArray()
        println("unconverted:")
        unconverted.forEach { println(it) }
        val deferredFunction = DeferredFunction(function, *unconverted)
        deferredFunction.invoke()
    }

    fun register(function: KFunction<Any>): Int {
        val hashCode = function.hashCode()
        functions[hashCode] = function
        return hashCode
    }

    fun <T> safeFunctionCall(
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
