package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Text (
        text = property.name,
        modifier = modifier.padding(16.dp)
    )
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
        status = "Sin dueño"
    )
    PropertyItem(property = sampleProperty)
}
