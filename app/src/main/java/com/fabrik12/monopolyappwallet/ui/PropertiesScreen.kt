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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabrik12.monopolyappwallet.data.mockPropertyEntityList
import com.fabrik12.monopolyappwallet.viewmodel.PropertiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesScreen() {
    // Obtener el viewModel
    val viewModel: PropertiesViewModel = viewModel()

    // Observar los cambios en la lista de propiedades
    val propertiesList by viewModel.properties.collectAsState()


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
                PropertyItem(propertyEntity = property)
            }
        }
        Spacer(modifier = Modifier.height(24.dp)) // Espacio final

    }
}

//@Preview(showBackground = true)
//@Composable
//fun PropertiesScreenPreview() {
//    PropertiesScreen()
//}
