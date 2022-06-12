package com.example.offloader.offloader.util

import com.example.offloader.BuildConfig

object Constants {
    const val CHANNEL_ID = "execute_ch"
    const val EXECUTE_SERVICE = "EXECUTE_SERVICE"

    object ServiceActions {
        const val OPEN_GATE = "OPEN_GATE"
        const val CLOSE_GATE = "CLOSE_GATE"
    }

    object URLs {
        const val HTTP_SERVER_URL: String = BuildConfig.HTTP_SERVER_URL
        const val WEB_SOCKET_URL: String = BuildConfig.WEB_SOCKET_URL
        const val UPLOAD_WEB_SOCKET_URL: String = "$WEB_SOCKET_URL/upload"
        const val GRAB_WEB_SOCKET_URL: String = "$WEB_SOCKET_URL/grab"
    }
}