package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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
            modifier = Modifier.padding(innerPadding)
        ){
            Text("Aqui tenemos el contenido principal")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionsScreenPreview() {
    ActionsScreen()
}