package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class) // TopBar y Scaffold
@Composable
fun ActionsScreen() {
    Scaffold(
        topBar = {
            // BarraSuperior (TopAppBar)
            TopAppBar(title = { Text("Acciones del Juego") })
    },
        bottomBar = {
            // Barra de Navegacion (inferior)
            Text("Barra de Navegacion Inferior (proto)")
        }
    ) { innerPadding ->
        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ){
            Text(
                text = "Acciones comunes",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Form para Pagar a Jugador
            PayPlayerForm()

            // Mas secciones
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayPlayerForm(){
    var selectedPlayer by remember { mutableStateOf("Select Player") }
    var amount by remember { mutableStateOf("")}
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val players = listOf("Plater 2", "Player 3", "The Banker") // Datos de ejemplo

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = it },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = selectedPlayer,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                players.forEach { player ->
                    DropdownMenuItem(
                        text = { Text(player) },
                        onClick = {
                            selectedPlayer = player
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.weight(0.7f)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Boton principal
    Button(
        onClick = { /* Acción al hacer clic: Logica de Pago */ },
        modifier = Modifier.fillMaxWidth()
    ){
        Text("Pagar Jugador")
    }

}

@Preview(showBackground = true)
@Composable
fun ActionsScreenPreview() {
    ActionsScreen()
}