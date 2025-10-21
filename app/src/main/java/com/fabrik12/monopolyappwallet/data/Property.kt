package com.fabrik12.monopolyappwallet.data

import androidx.compose.ui.graphics.Color

data class Property (
    val id: Int,
    val name: String,
    val price: Int,
    val groupColor: Color,
    val status: String
)