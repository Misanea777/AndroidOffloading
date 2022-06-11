package com.example.offloader.offloader.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.offloader.offloader.SystemResourceMonitor

class BatteryStateReceiver(private val systemResourceMonitor: SystemResourceMonitor) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            Intent.ACTION_POWER_CONNECTED -> systemResourceMonitor.isCharging = true
            Intent.ACTION_POWER_DISCONNECTED -> systemResourceMonitor.isCharging = false
            Intent.ACTION_BATTERY_OKAY -> systemResourceMonitor.isBatteryStateOkay = true
            Intent.ACTION_BATTERY_LOW -> systemResourceMonitor.isBatteryStateOkay = false
        }

        println("in receiver")
        println(systemResourceMonitor.isCharging)
        println(systemResourceMonitor.isBatteryStateOkay)
    }

    companion object {
        val intentFilter =  IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
        }
    }
}