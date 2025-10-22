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
val mockPropertyList = listOf(
    Property(1, "Av. Mediterráneo", 60, PropertyColors.BROWN, "Sin dueño"),
    Property(2, "Arca Comunal", 0, Color.Transparent, "Toma una carta"),
    Property(3, "Av. Báltica", 60, PropertyColors.BROWN, "Hipotecada"),
    Property(4, "Impuesto sobre Ingresos", 0, Color.Transparent, "Paga $200"),
    Property(5, "Ferrocarril Reading", 200, PropertyColors.RAILROAD, "Sin dueño"),
    Property(6, "Av. Oriental", 100, PropertyColors.LIGHT_BLUE, "Sin dueño"),
    Property(7, "Casualidad", 0, Color.Transparent, "Toma una carta"),
    Property(8, "Av. Vermont", 100, PropertyColors.LIGHT_BLUE, "Tuya"),
    Property(9, "Av. Connecticut", 120, PropertyColors.LIGHT_BLUE, "Sin dueño"),
    Property(10, "Cárcel (De visita)", 0, Color.Transparent, "De visita"),
    Property(11, "Plaza San Carlos", 140, PropertyColors.PINK, "Sin dueño"),
    Property(12, "Compañía de Electricidad", 150, PropertyColors.UTILITY, "Sin dueño"),
    Property(13, "Av. de los Estados", 140, PropertyColors.PINK, "Tuya"),
    Property(14, "Av. Virginia", 160, PropertyColors.PINK, "Sin dueño"),
    Property(15, "Ferrocarril de Pensilvania", 200, PropertyColors.RAILROAD, "Sin dueño"),
    Property(16, "Plaza St. James", 180, PropertyColors.ORANGE, "Hipotecada"),
    Property(17, "Arca Comunal", 0, Color.Transparent, "Toma una carta"),
    Property(18, "Av. Tennessee", 180, PropertyColors.ORANGE, "Sin dueño"),
    Property(19, "Av. Nueva York", 200, PropertyColors.ORANGE, "Sin dueño"),
    Property(20, "Estacionamiento Gratuito", 0, Color.Transparent, "Descansa"),
    // TODO: Añadir las propiedades restantes (Rojas, Amarillas, Verdes, Azules)
    Property(21, "Av. Kentucky", 220, PropertyColors.RED, "Sin dueño"),
    Property(22, "Casualidad", 0, Color.Transparent, "Toma una carta"),
    Property(23, "Av. Indiana", 220, PropertyColors.RED, "Tuya"),
    Property(24, "Av. Illinois", 240, PropertyColors.RED, "Sin dueño"),
    Property(25, "Ferrocarril B. & O.", 200, PropertyColors.RAILROAD, "Sin dueño")
)