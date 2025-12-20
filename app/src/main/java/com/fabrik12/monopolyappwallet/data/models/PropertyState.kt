package com.fabrik12.monopolyappwallet.data.models

/**
 * Estado de una propiedad en el juego
 */
data class PropertyState (
    val id: String,
    val name: String,
    val ownerId: String?,
    val price: Int,
    val houses: Int = 0,
    val isMortgaged: Boolean = false,
    val rentPrices: List<Int>? = null,
    val houseCost: Int = 0
)