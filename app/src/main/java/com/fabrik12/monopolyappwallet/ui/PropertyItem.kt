package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fabrik12.monopolyappwallet.ui.models.PropertyUiModel

@Composable
fun PropertyItem(propertyUi: PropertyUiModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar de color
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(propertyUi.color, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (propertyUi.color == Color.Gray) {
                    // Icono para utilidades/estaciones si el color es gris
                    Icon(Icons.Default.QuestionMark, contentDescription = null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = propertyUi.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                // Mostrar Dueño y Estado
                val statusText = if (propertyUi.isMortgaged) "HIPOTECADA" else propertyUi.ownerName ?: "Banco"
                val statusColor = if (propertyUi.isMortgaged) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor
                )
            }

            // Columna de valor y casitas
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${propertyUi.price}",
                    fontWeight = FontWeight.Bold
                )
                if (propertyUi.houseCount > 0 || propertyUi.hotelCount > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val icon = if (propertyUi.hotelCount > 0) Icons.Default.Hotel else Icons.Default.Home
                        val count = if (propertyUi.hotelCount > 0) propertyUi.hotelCount else propertyUi.houseCount
                        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Text("x$count", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}