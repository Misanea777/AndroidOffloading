package com.example.offloader.offloader.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.content.ContextCompat.getSystemService
import com.example.offloader.offloader.OffloadManager
import com.example.offloader.offloader.util.Constants

class ServiceConnectionManager(private val context: Context) {

    lateinit var connection: ServiceConnection
    lateinit var offloadManager: OffloadManager

    fun initService() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(Constants.CHANNEL_ID, "Execute_channel", NotificationManager.IMPORTANCE_HIGH)
            getSystemService(context, NotificationManager::class.java)!!.createNotificationChannel(notificationChannel)
        }

        initConnection()
        context.bindService(Intent(context, ExecutableService::class.java), connection, Context.BIND_ABOVE_CLIENT)

        val serviceIntent = Intent("Some command", Uri.parse(""), context, ExecutableService::class.java )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService (serviceIntent)
        } else {
            context.startService (serviceIntent)
        }
    }

    private fun initConnection() {
        connection = object: ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                if(service is ExecutableService.ExecutableServiceBinder) {
                    offloadManager = service.getOffloadManagerInstance()
                    println("Connected to service")
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }
        }
    }

}