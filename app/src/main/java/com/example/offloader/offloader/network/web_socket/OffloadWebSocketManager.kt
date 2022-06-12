package com.example.offloader.offloader.network.web_socket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class OffloadWebSocketManager(private val serverUrl: String) : MessageListener {

    lateinit var webSocketManager: WebSocketManager
    private val _state: MutableLiveData<Boolean> = MutableLiveData(false)
    val state: LiveData<Boolean> = _state

    fun initWebSock() {
        webSocketManager = WebSocketManager()
        webSocketManager.init(serverUrl, this)
        webSocketManager.connect()
    }

    fun sendMessage(message: String) {
        webSocketManager.sendMessage( message )
    }

    override fun onConnectSuccess() {
        _state.postValue(true)
        addText( " Connected successfully $serverUrl \n " )
    }

    override fun onConnectFailed() {
        addText( " Connection failed \n " )
    }

    override fun onClose() {
        addText( " Closed successfully \n " )
    }

    override fun onMessage(text: String?) {
        addText( " Receive message: $serverUrl body: $text \n " )
    }

    private fun addText(text: String?) {
        println(text)
    }

}