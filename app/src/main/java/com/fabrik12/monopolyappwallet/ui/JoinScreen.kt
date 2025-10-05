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
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fabrik12.monopolyappwallet.ui.theme.BrandBlue
import com.fabrik12.monopolyappwallet.ui.theme.BrandGray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun JoinScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    // Memoria para campo de texto
    val playerName = remember { mutableStateOf("") }
    val gameId = remember { mutableStateOf("") }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        // El color de fondo ahora depende del tema
        focusedContainerColor = if (isSystemInDarkTheme()) {
            Color.White.copy(alpha = 0.1f) // gris muy sutil para el modo oscuro
        } else {
            BrandGray.copy(alpha = 0.1f) // Gris sutil para el modo claro
        },
        unfocusedContainerColor = if (isSystemInDarkTheme()) {
            Color.White.copy(alpha = 0.1f)
        } else {
            BrandGray.copy(alpha = 0.1f)
        },
        // Asegurar constraste entre el texto y el borde
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        // Borde usando el color primario del tema actual
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = MaterialTheme.colorScheme.primary
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.widthIn(max = 500.dp)
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
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de texto para el ID de partida
            OutlinedTextField(
                value = gameId.value,
                onValueChange = { gameId.value = it },
                label = { Text("ID de la Partida") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (playerName.value.isNotBlank() && gameId.value.isNotBlank()) {
                        WebSocketClient.connect{ message ->
                            scope.launch(Dispatchers.Main) {
                                val jsonResponse = JSONObject(message)
                                val type = jsonResponse.getString("type")

                                if (type == "GAME_CREATED") {
                                    val payload = jsonResponse.getJSONObject("payload")
                                    val receivedGameId = payload.getString("gameId")

                                    // Navegacion
                                    navController.navigate("game_screen/$receivedGameId")
                                }
                            }
                        }
                        Thread.sleep(500) // Esperar 0.5 segundos
                        WebSocketClient.sendMessage(
                            playerName.value,
                            gameId.value)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("CREAR PARTIDA")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Preview(showBackground = true)
@Composable
fun JoinScreenPreview() {
    val navController = rememberNavController()
    JoinScreen(navController = navController);
}