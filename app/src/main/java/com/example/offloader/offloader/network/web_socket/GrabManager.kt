package com.example.offloader.offloader.network.web_socket

class GrabManager(
    private val serverUrl: String,
    private val onConnectSuccess: (() -> Unit)? = null,
    private val onMessage: (() -> Unit)? = null,
    private val onConnectFailed: (() -> Unit)? = null,
    private val onClose: (() -> Unit)? = null,
) : MessageListener {

    private lateinit var webSocketManager: WebSocketManager

    fun initWebSock() {
        webSocketManager = WebSocketManager()
        webSocketManager.init(serverUrl, this)
        webSocketManager.connect()
    }

    fun sendMessage(message: String): Boolean {
        val isConnected = webSocketManager.isConnect()
        if (isConnected) webSocketManager.sendMessage(message)
        return isConnected
    }

    override fun onConnectSuccess() {
        onConnectSuccess?.invoke()
        addText( " Connected successfully $serverUrl \n " )
    }

    override fun onConnectFailed() {
        onConnectFailed?.invoke()
        addText( " Connection failed $serverUrl\n " )
    }

    override fun onClose() {
        onClose?.invoke()
        addText( " Closed successfully $serverUrl\n " )
    }

    override fun onMessage(text: String?) {
        onMessage?.invoke()
        addText( " Receive message: $serverUrl body: $text \n " )
    }

    private fun addText(text: String?) {
        println(text)
    }
}