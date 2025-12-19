package com.fabrik12.monopolyappwallet.data.models

/**
 * Representa un jugador en la partida.
 */
data class Player(
    val id: String,
    val name: String,
    val balance: Int,
    val properties: List<String> = emptyList()
)
