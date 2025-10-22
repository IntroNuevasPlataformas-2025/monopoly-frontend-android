package com.fabrik12.monopolyappwallet.data

import androidx.compose.ui.graphics.Color

/**
 * Definicion de la estructura de una propiedad en el juego.
 *
 * @param id Un identificador único.
 * @param name El nombre de la propiedad (ej. "Av. Mediterráneo").
 * @param price El precio de compra.
 * @param groupColor El color del grupo (para el "avatar").
 * @param status El estado actual (ej. "Sin dueño", "Hipotecada", "Tuya").
 */
data class Property (
    val id: Int,
    val name: String,
    val price: Int,
    val groupColor: Color,
    val status: String
)