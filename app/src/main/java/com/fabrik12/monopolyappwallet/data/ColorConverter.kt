package com.fabrik12.monopolyappwallet.data

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.fabrik12.monopolyappwallet.ui.theme.*

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

    companion object {
        fun getColor(propertyId: String):Color {
            return when {
                propertyId.contains("mediterranean") || propertyId.contains("baltic") -> Color(0xFF8D6E63) // Marrón
                propertyId.contains("oriental") || propertyId.contains("vermont") || propertyId.contains("connecticut") -> Color(0xFF42A5F5) // Azul Cielo
                propertyId.contains("charles") || propertyId.contains("states") || propertyId.contains("virginia") -> Color(0xFFEC407A) // Rosa
                propertyId.contains("james") || propertyId.contains("tennessee") || propertyId.contains("york") -> Color(0xFFFFA726) // Naranja
                propertyId.contains("kentucky") || propertyId.contains("indiana") || propertyId.contains("illinois") -> Color(0xFFEF5350) // Rojo
                propertyId.contains("atlantic") || propertyId.contains("ventnor") || propertyId.contains("marvin") -> Color(0xFFFFEE58) // Amarillo
                propertyId.contains("pacific") || propertyId.contains("carolina") || propertyId.contains("pennsylvania") -> Color(0xFF66BB6A) // Verde
                propertyId.contains("park") || propertyId.contains("boardwalk") -> Color(0xFF1E88E5) // Azul Oscuro
                else -> Color.Gray // Estaciones o Servicios
            }
        }
    }

}