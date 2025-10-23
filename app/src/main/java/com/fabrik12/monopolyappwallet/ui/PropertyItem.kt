package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fabrik12.monopolyappwallet.data.Property
import com.fabrik12.monopolyappwallet.data.PropertyColors

/**
 * Componente que muestra un elemento de propiedad.
 *
 * @param property La propiedad a mostrar
 */
@Composable
fun PropertyItem(property: Property, modifier: Modifier = Modifier) {
    // Construccion de la tarjeta de propiedad
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color Box
            PropertyAvatar(property = property)

            Spacer(modifier = Modifier.width(16.dp))

            // Informacion con Nombre y Estado
            // Columna para apilar elementos verticalmente
            Column(modifier = Modifier.weight(1f)) {
                Text (
                    text = property.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = property.status,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (property.price > 0) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$${property.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PropertyAvatar(property: Property, modifier: Modifier = Modifier) {
    val size = 56.dp
    if (property.groupColor != Color.Transparent) {
        Box(
            modifier = modifier
                .size(size)
                .background(property.groupColor, RoundedCornerShape(8.dp))
        )
    } else {
        Icon(
            imageVector = Icons.Default.QuestionMark,
            contentDescription = property.name,
            modifier = modifier.size(size).padding(8.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// --- Previsualización ---
// Esta preview nos permite diseñar el item de forma aislada.
@Preview(showBackground = true)
@Composable
fun PropertyItemPreview() {
    val sampleProperty = Property(
        id = 1,
        name = "Av. Mediterráneo",
        price = 60,
        groupColor = PropertyColors.BROWN,
        status = "Propiedad Tuya"
    )
    Box(modifier = Modifier.padding(8.dp)) {
        PropertyItem(property = sampleProperty)
    }
}

@Preview(showBackground = true)
@Composable
fun PropertyItemSpecialPreview() {
    val sampleSpecial = Property(
        id = 2,
        name = "Arca Comunal",
        price = 0,
        groupColor = Color.Transparent,
        status = "Toma una carta"
    )
    Box(modifier = Modifier.padding(8.dp)) {
        PropertyItem(property = sampleSpecial)
    }
}