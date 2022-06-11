package com.example.offloader.offloader.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService


class NetworkStateMonitor(context: Context) {

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            isUnmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            val wifiInfo = networkCapabilities.transportInfo as WifiInfo?
            wifiInfo?.let {
                wifiLevel = WifiManager.calculateSignalLevel(wifiInfo.rssi, 5)
            }

            setNetworkStatus()

            println(isUnmetered)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
        }
    }

    private val connectivityManager = getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager

    var isUnmetered = false
    var wifiLevel = 0
    var networkStatusOkay = true

    init{
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    private fun setNetworkStatus() = isUnmetered && networkStatusOkay
}