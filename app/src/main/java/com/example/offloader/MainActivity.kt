package com.example.offloader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.offloader.databinding.ActivityMainBinding
import com.example.offloader.offloader.OffloadManager
import com.example.offloader.offloader.data.models.ReadyForWork
import com.example.offloader.offloader.data.models.UploadTask
import com.example.offloader.offloader.network.web_socket.OffloadWebSocketManager
import com.example.offloader.offloader.service.ExecutableService
import com.example.offloader.offloader.service.ServiceConnectionManager
import com.example.offloader.offloader.util.Constants
import com.example.offloader.offloader.util.Constants.CHANNEL_ID
import com.example.offloader.offloader.util.LocalResource
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var serviceConnectionManager: ServiceConnectionManager

    private lateinit var connection: ServiceConnection

    var offloadManager: OffloadManager? = null

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val uploadSocketManager = OffloadWebSocketManager(Constants.URLs.GRAB_WEB_SOCKET_URL).apply {
            initWebSock()
        }

        uploadSocketManager.state.observe(this) {
            val toSend = Gson().toJson(ReadyForWork("fasfas"))
            uploadSocketManager.sendMessage(toSend)
        }

//        initService()

        serviceConnectionManager = ServiceConnectionManager(this)
        serviceConnectionManager.initService()

        connection = serviceConnectionManager.connection
        offloadManager = serviceConnectionManager.offloadManager


        initListeners()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        viewModel.cluster_list.observeForever {
            println(it.message[0])
        }
        return super.onCreateView(name, context, attrs)
    }

    private fun initListeners() {
        viewModel.numbers.observe(this) {
            binding.button.isEnabled = it.isNotEmpty()
        }
        binding.button.setOnClickListener {
            val uploadSocketManager = OffloadWebSocketManager(Constants.URLs.UPLOAD_WEB_SOCKET_URL).apply {
                initWebSock()
            }

            uploadSocketManager.state.observe(this) {
                val toSend = Gson().toJson(UploadTask("args", "id", "id", "id"))
                uploadSocketManager.sendMessage(toSend)
            }



            println("Clicked!")
//            test(viewModel.numbers.value!!.toMutableList())
        }
    }

    private fun initService() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, "Execute_channel", NotificationManager.IMPORTANCE_HIGH)
            getSystemService(NotificationManager::class.java).createNotificationChannel(notificationChannel)
        }

        initConnection()
        bindService(Intent(this, ExecutableService::class.java), connection, Context.BIND_ABOVE_CLIENT)

        val serviceIntent = Intent("Some command", Uri.parse(""), this, ExecutableService::class.java )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService (serviceIntent)
        } else {
            startService (serviceIntent)
        }
    }

    private fun initConnection() {
        connection = object: ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                if(service is ExecutableService.ExecutableServiceBinder) {
                    offloadManager = service.getOffloadManagerInstance()
                    println("good")
//                    test()
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }
        }
    }

    fun test(list: MutableList<Int>) {
        serviceConnectionManager.offloadManager?.let {
            println("executing...")
            val id = it.register(viewModel::sort)

            val res = it.execute(id, list)
            when(res) {
                is LocalResource.Success -> println(res.value)
                is LocalResource.Failure -> println(res.throwable.message)
            }
        }
    }

    fun whatever(name: String, age: Double): String {
        return "$name of age $age"
    }

}