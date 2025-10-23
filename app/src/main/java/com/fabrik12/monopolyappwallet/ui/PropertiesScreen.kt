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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fabrik12.monopolyappwallet.data.mockPropertyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Propiedades del Tablero") })
        },
    ) { innerPadding ->
        // Contenido de la pantalla

        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(mockPropertyList) { property ->
                PropertyItem(property = property)
            }
        }
        Spacer(modifier = Modifier.height(24.dp)) // Espacio final

    }
}

@Preview(showBackground = true)
@Composable
fun PropertiesScreenPreview() {
    PropertiesScreen()
}
