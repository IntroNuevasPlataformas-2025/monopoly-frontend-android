package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

import com.fabrik12.monopolyappwallet.ui.WebSocketClient

@Composable
fun JoinScreen() {
    // Memoria para campo de texto
    val playerName = remember { mutableStateOf("") }
    val gameId = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Texto de bienvenida
        Text(
            text = "Crea o Unete a una partida",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de texto para el nombre del Jugador
        OutlinedTextField(
            value = playerName.value,
            onValueChange = { playerName.value = it },
            label = { Text("Tu Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para el ID de partida
        OutlinedTextField(
            value = gameId.value,
            onValueChange = { gameId.value = it },
            label = { Text("ID de la Partida") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (playerName.value.isNotBlank() && gameId.value.isNotBlank()) {
                    WebSocketClient.connect()
                }
                /* Logica con Websocket */
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Unirse a la Partida")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JoinScreenPreview() {
    JoinScreen();
}