package com.example.offloader.offloader.network.web_socket

interface MessageListener {
    fun  onConnectSuccess () // successfully connected
    fun  onConnectFailed () // connection failed
    fun  onClose () // close
    fun onMessage(text: String?)
}