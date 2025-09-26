package com.fabrik12.monopolyappwallet.ui

import android.util.Log
import okhttp3.*
import org.json.JSONObject

object WebSocketClient {
    private const val SERVER_URL = "ws://localhost:3000"

    private lateinit var client: OkHttpClient
    private var webSocket: WebSocket? = null

    fun connect() {
        client = OkHttpClient()
        val request = Request.Builder().url(SERVER_URL).build()

        // Crear Listener
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Conexión abierta")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Mensaje recibido: $text")
                // Manejar el mensaje recibido
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                Log.d("WebSocket", "Cerrando conexión: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error en la conexion: ${t.message}")
            }
        }

        // Iniciar la conexion
        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(playerName: String, gameId: String) {
        // Construir el mensaje JSON
        val jsonPayload = JSONObject().apply {
            put("playerId", "android-${System.currentTimeMillis()}")
            put("playerName", playerName)
            put("gameId", gameId)
        }

        val jsonMessage = JSONObject().apply {
            put("type", "JOIN_GAME")
            put("payload", jsonPayload)
        }

        val message = jsonMessage.toString()
        webSocket?.send(message)
        Log.d("WebSocket", "Mensaje enviado: $message")

    }

    fun disconnect() {
        webSocket?.close(1000, "Cierre normal")
    }
}