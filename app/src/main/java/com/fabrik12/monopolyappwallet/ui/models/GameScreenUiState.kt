package com.fabrik12.monopolyappwallet.ui.models

import androidx.compose.ui.graphics.Color

data class PropertyUiModel(
    val id: String,
    val name: String,
    val color: Color,
    val houseCount: Int = 0,
    val hotelCount: Int = 0,
    val isMortgaged: Boolean = false,
    val value: Int
)

enum class TransactionType {
    PURCHASE, RENT, CONSTRUCTION, EVENT
}

data class TransactionUiModel(
    val id: String,
    val type: TransactionType,
    val title: String,
    val subtitle: String,
    val amount: Int,
    val isPositive: Boolean
)

object MockData {
    val properties = listOf(
        PropertyUiModel(
            id = "1",
            name = "Avenida Principal",
            color = Color(0xFF3B82F6), // Blue
            hotelCount = 1,
            value = 400
        ),
        PropertyUiModel(
            id = "2",
            name = "Plaza Central",
            color = Color(0xFFEF4444), // Red
            houseCount = 3,
            value = 250
        ),
        PropertyUiModel(
            id = "3",
            name = "Paseo del Río",
            color = Color(0xFFFB923C), // Orange
            isMortgaged = true,
            value = 120
        )
    )

    val transactions = listOf(
        TransactionUiModel(
            id = "1",
            type = TransactionType.PURCHASE,
            title = "Compra de Propiedad",
            subtitle = "Avenida Principal",
            amount = 200,
            isPositive = false
        ),
        TransactionUiModel(
            id = "2",
            type = TransactionType.RENT,
            title = "Cobro de Renta",
            subtitle = "Jugador 2",
            amount = 50,
            isPositive = true
        ),
        TransactionUiModel(
            id = "3",
            type = TransactionType.CONSTRUCTION,
            title = "Construcción",
            subtitle = "1 casa en Plaza Central",
            amount = 100,
            isPositive = false
        ),
        TransactionUiModel(
            id = "4",
            type = TransactionType.EVENT,
            title = "Evento Especial",
            subtitle = "Premio de belleza",
            amount = 10,
            isPositive = true
        )
    )
}
