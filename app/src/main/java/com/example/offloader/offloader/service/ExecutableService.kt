package com.example.offloader.offloader.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.offloader.MainActivity
import com.example.offloader.offloader.OffloadManager
import com.example.offloader.offloader.util.Constants
import com.example.offloader.offloader.util.Constants.CHANNEL_ID
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExecutableService : Service() {

    private lateinit var offloadManager: OffloadManager

    private lateinit var batteryStateReceiver: BatteryStateReceiver

    override fun onBind(intent: Intent?): IBinder {
        return ExecutableServiceBinder()
    }

    inner class ExecutableServiceBinder : Binder() {
        fun getOffloadManagerInstance()= offloadManager
    }

    override fun onCreate() {
        super.onCreate()

        offloadManager = OffloadManager(this)
        println("yay")
        println(offloadManager.systemResourceMonitor.networkStateMonitor.isUnmetered)

        batteryStateReceiver = BatteryStateReceiver(offloadManager.systemResourceMonitor)
        registerReceiver(batteryStateReceiver, BatteryStateReceiver.intentFilter)


        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Offloader executable",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )


            val notificationIntent = Intent(this, MainActivity::class.java)

            val intent = PendingIntent.getActivity(
                this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )

            val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(intent)
                .setContentTitle("Offloader executable")
                .setContentText("Executing external tasks").build()

            startForeground(1, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.action.equals(Constants.ServiceActions.OPEN_GATE)) {
            println("open for executing")
        } else if(intent?.action.equals(Constants.ServiceActions.CLOSE_GATE)) {
            println("closed for executing")
        }
        return START_STICKY
    }
}