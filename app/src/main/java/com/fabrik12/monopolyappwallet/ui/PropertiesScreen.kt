package com.fabrik12.monopolyappwallet.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Propiedades del Tablero") })
        }
    ) { innerPadding ->
        // Contenido de la pantalla
        Text("Contenido de la pantalla de Propiedades")

    }
}

@Preview(showBackground = true)
@Composable
fun PropertiesScreenPreview() {
    PropertiesScreen()
}
