package com.fabrik12.monopolyappwallet.data

import androidx.compose.ui.graphics.Color

// Definicion de los colores del tablero para reutilizacion
object PropertyColors {
    val BROWN = Color(0xFF955438)
    val LIGHT_BLUE = Color(0xFFA9E1F9)
    val PINK = Color(0xFFD83A95)
    val ORANGE = Color(0xFFF69322)
    val RED = Color(0xFFED1E2A)
    val YELLOW = Color(0xFFFFF10A)
    val GREEN = Color(0xFF1FB25A)
    val DARK_BLUE = Color(0xFF0756A6)
    val RAILROAD = Color(0xFF000000)
    val UTILITY = Color(0xFFB1B1B1)
}

// Lista de propiedades de maqueta (Mock Data)
val mockPropertyEntityLists = listOf(
    PropertyEntity(1, "Av. Mediterráneo", 60, PropertyColors.BROWN, "Sin dueño"),
    PropertyEntity(2, "Arca Comunal", 0, Color.Transparent, "Toma una carta"),
    PropertyEntity(3, "Av. Báltica", 60, PropertyColors.BROWN, "Hipotecada"),
    PropertyEntity(4, "Impuesto sobre Ingresos", 0, Color.Transparent, "Paga $200"),
    PropertyEntity(5, "Ferrocarril Reading", 200, PropertyColors.RAILROAD, "Sin dueño"),
    PropertyEntity(6, "Av. Oriental", 100, PropertyColors.LIGHT_BLUE, "Sin dueño"),
    PropertyEntity(7, "Casualidad", 0, Color.Transparent, "Toma una carta"),
    PropertyEntity(8, "Av. Vermont", 100, PropertyColors.LIGHT_BLUE, "Tuya"),
    PropertyEntity(9, "Av. Connecticut", 120, PropertyColors.LIGHT_BLUE, "Sin dueño"),
    PropertyEntity(10, "Cárcel (De visita)", 0, Color.Transparent, "De visita"),
    PropertyEntity(11, "Plaza San Carlos", 140, PropertyColors.PINK, "Sin dueño"),
    PropertyEntity(12, "Compañía de Electricidad", 150, PropertyColors.UTILITY, "Sin dueño"),
    PropertyEntity(13, "Av. de los Estados", 140, PropertyColors.PINK, "Tuya"),
    PropertyEntity(14, "Av. Virginia", 160, PropertyColors.PINK, "Sin dueño"),
    PropertyEntity(15, "Ferrocarril de Pensilvania", 200, PropertyColors.RAILROAD, "Sin dueño"),
    PropertyEntity(16, "Plaza St. James", 180, PropertyColors.ORANGE, "Hipotecada"),
    PropertyEntity(17, "Arca Comunal", 0, Color.Transparent, "Toma una carta"),
    PropertyEntity(18, "Av. Tennessee", 180, PropertyColors.ORANGE, "Sin dueño"),
    PropertyEntity(19, "Av. Nueva York", 200, PropertyColors.ORANGE, "Sin dueño"),
    PropertyEntity(20, "Estacionamiento Gratuito", 0, Color.Transparent, "Descansa"),
    // TODO: Añadir las propiedades restantes (Rojas, Amarillas, Verdes, Azules)
    PropertyEntity(21, "Av. Kentucky", 220, PropertyColors.RED, "Sin dueño"),
    PropertyEntity(22, "Casualidad", 0, Color.Transparent, "Toma una carta"),
    PropertyEntity(23, "Av. Indiana", 220, PropertyColors.RED, "Tuya"),
    PropertyEntity(24, "Av. Illinois", 240, PropertyColors.RED, "Sin dueño"),
    PropertyEntity(25, "Ferrocarril B. & O.", 200, PropertyColors.RAILROAD, "Sin dueño")
)