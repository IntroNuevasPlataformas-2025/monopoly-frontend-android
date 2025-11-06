package com.fabrik12.monopolyappwallet.data

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

/**
 * @brief Convertir colores a hexadecimal (Long)
 * para que Room pueda guardarlos
 */
class ColorConverter {
    @TypeConverter
    fun fromColor(color: Color): Long {
        return color.value.toLong()
    }

    @TypeConverter
    fun toColor(value: Long): Color {
        return Color(value.toULong())
    }
}