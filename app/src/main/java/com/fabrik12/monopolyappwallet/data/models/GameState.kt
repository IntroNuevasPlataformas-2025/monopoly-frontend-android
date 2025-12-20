package com.fabrik12.monopolyappwallet.data.models

import com.google.gson.annotations.SerializedName

/**
 * Estado general de la partida
 */
data class GameState(
    val gameId: String? = null,
    val hostId: String? = null,
    val status: String = "",
    val isClosedForNewPlayers: Boolean = false,

    @SerializedName("players")
    val players: Map<String, Player> = emptyMap(),

    @SerializedName("properties")
    val properties: Map<String, PropertyState> = emptyMap()
)
