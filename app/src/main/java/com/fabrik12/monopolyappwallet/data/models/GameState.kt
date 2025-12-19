package com.fabrik12.monopolyappwallet.data.models

/**
 * Estado general de la partida
 */
data class GameState(
    val gameId: String? = null,
    val status: String = "",
    val players: List<Player> = emptyList()
)
