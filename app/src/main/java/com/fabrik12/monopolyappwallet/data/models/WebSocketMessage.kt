package com.fabrik12.monopolyappwallet.data.models

import com.google.gson.JsonElement

/**
 * Mensaje genérico para WebSocket según la especificación del backend.
 */
data class WebSocketMessage(
    val type: String,
    val payload: JsonElement? = null
)
