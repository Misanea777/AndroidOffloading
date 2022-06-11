package com.example.offloader.offloader.network.web_socket

class OffloadWebSocketManager : MessageListener {
    fun initWebSock() {
        val serverUrl = "ws://immense-brushlands-80363.herokuapp.com/websocket"
        WebSocketManager.init(serverUrl, this)
        WebSocketManager.connect()
        WebSocketManager .sendMessage( " Client send " )
        WebSocketManager .sendMessage( " Client send " )
        WebSocketManager .sendMessage( " Client send " )

    }


    override fun onConnectSuccess() {
        addText( " Connected successfully \n " )
    }

    override fun onConnectFailed() {
        addText( " Connection failed \n " )
    }

    override fun onClose() {
        addText( " Closed successfully \n " )
    }

    override fun onMessage(text: String?) {
        println("message received: $text")
        addText( " Receive message: $text \n " )
    }

    private fun addText(text: String?) {
        println(text)
    }

}