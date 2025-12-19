package com.fabrik12.monopolyappwallet.ui

import android.util.Log
import okhttp3.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect

class WebSocketClient(
    private val gson: com.google.gson.Gson,
    private val scope: kotlinx.coroutines.CoroutineScope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO + kotlinx.coroutines.SupervisorJob())
) {

    //private const val SERVER_URL = "ws://192.168.18.29:3000"
    companion object {
        private const val SERVER_URL = "ws://10.0.2.2:3000"
        fun createDefault() = WebSocketClient(com.google.gson.Gson())
    }

    private val client: OkHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    // StateFlow para el estado actual de la partida (null hasta recibir un update)
    private val _gameState = kotlinx.coroutines.flow.MutableStateFlow<com.fabrik12.monopolyappwallet.data.models.GameState?>(null)
    val gameState: kotlinx.coroutines.flow.StateFlow<com.fabrik12.monopolyappwallet.data.models.GameState?> = _gameState.asStateFlow()

    // SharedFlow para eventos one-shot (errores, notificaciones)
    private val _events = kotlinx.coroutines.flow.MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 64)
    val events: kotlinx.coroutines.flow.SharedFlow<String> = _events.asSharedFlow()

    fun connect(serverUrl: String? = null) {
        // Si ya hay una conexion abierta, cerrarla antes de reconectar
        webSocket?.close(1000, "reconnect")
        webSocket = null

        val url = serverUrl ?: SERVER_URL
        val request = Request.Builder().url(url).build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                this@WebSocketClient.webSocket = webSocket
                Log.d("WebSocketClient", "Conexión abierta: $url")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocketClient", "Mensaje recibido: $text")
                scope.launch { handleIncomingMessage(text) }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                Log.d("WebSocketClient", "Cerrando conexión: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocketClient", "Error en la conexion: ${t.message}")
                scope.launch { _events.emit("WebSocket error: ${t.message ?: "unknown"}") }
            }
        }

        webSocket = client.newWebSocket(request, listener)
    }

    private suspend fun handleIncomingMessage(text: String) {
        try {
            val wsMsg = gson.fromJson(text, com.fabrik12.monopolyappwallet.data.models.WebSocketMessage::class.java)
            when (wsMsg.type) {
                "GAME_STATE_UPDATE", "GAME_CREATED", "GAME_JOINED" -> {
                    wsMsg.payload?.let { payload ->
                        val gameState = gson.fromJson(payload, com.fabrik12.monopolyappwallet.data.models.GameState::class.java)
                        _gameState.emit(gameState)
                    }
                }
                "ERROR", "INFO", "PLAYER_REQUEST" -> {
                    val info = wsMsg.payload?.let { it.toString() } ?: wsMsg.type
                    _events.emit(info)
                }
                else -> {
                    _events.emit("Unknown message type: ${wsMsg.type}")
                }
            }
        } catch (e: Exception) {
            Log.e("WebSocketClient", "Error parseando mensaje: ${e.message}")
            _events.emit("Parse error: ${e.message}")
        }
    }

    fun send(message: String) {
        if (webSocket == null) {
            scope.launch { _events.emit("Cannot send: not connected") }
            return
        }
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "Cierre normal")
        webSocket = null
    }

    /**
     * Ejemplo de cómo un ViewModel podría colectar ambos flows.
     * Uso (dentro de un ViewModel):
     *
     * init {
     *   wsClient.sampleCollectorsExample(viewModelScope)
     * }
     */
    fun sampleCollectorsExample(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        viewModelScope.launch {
            gameState.collect { gs -> Log.d("WebSocketClient", "Collected game state: $gs") }
        }
        viewModelScope.launch {
            events.collect { ev -> Log.d("WebSocketClient", "Event: $ev") }
        }
    }
}
