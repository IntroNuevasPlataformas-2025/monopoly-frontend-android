package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabrik12.monopolyappwallet.data.PropertyEntity
import com.fabrik12.monopolyappwallet.data.mockPropertyEntityList
import com.fabrik12.monopolyappwallet.ui.models.PropertyUiModel
import com.fabrik12.monopolyappwallet.viewmodel.GameViewModel
import com.fabrik12.monopolyappwallet.viewmodel.PropertiesViewModel

@Composable
fun PropertiesScreen(
    gameViewModel: GameViewModel
) {
    // Observar los cambios en la lista de propiedades
    val propertiesList by gameViewModel.allPropertiesUi.collectAsState()

    // Solo pasar el estado
    PropertiesContent(propertiesList = propertiesList)

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesContent(
    propertiesList: List<PropertyUiModel>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Propiedades del Tablero") })
        },
    ) { innerPadding ->
        // Contenido de la pantalla

        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(propertiesList) { property ->
                PropertyItem(propertyUi = property)
            }
        }
        Spacer(modifier = Modifier.height(24.dp)) // Espacio final

    }
}
/*
// --- Previsualización ---
@Preview(showBackground = true)
@Composable
fun PropertiesScreenPreview() {
    // Datos mock rápidos solo para el preview
    val previewData = listOf(
        PropertyEntity(1, "Av. Mediterráneo", 60, Color(0xFF955438), "Sin dueño"),
        PropertyEntity(2, "Av. Báltica", 60, Color(0xFF955438), "Hipotecada")
    )

    PropertiesContent(propertiesList = previewData)
}
*/
