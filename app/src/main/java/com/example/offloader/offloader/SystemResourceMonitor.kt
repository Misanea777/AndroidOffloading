package com.example.offloader.offloader

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.example.offloader.offloader.util.NetworkStateMonitor

class SystemResourceMonitor (context: Context) {

    val networkStateMonitor = NetworkStateMonitor(context)

    private val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
        context.registerReceiver(null, ifilter)
    }

    val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    var isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
            || status == BatteryManager.BATTERY_STATUS_FULL

    var isBatteryStateOkay: Boolean = getBatteryPct()?.let { it > 30 } ?: false

    fun isBatteryConditionOkay(): Boolean = isCharging || isBatteryStateOkay

    fun getBatteryPct() = batteryStatus?.let { intent ->
        val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        level * 100 / scale.toFloat()
    }
}