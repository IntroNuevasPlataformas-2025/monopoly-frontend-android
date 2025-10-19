package com.fabrik12.monopolyappwallet.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTopAppBarState
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
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f)) // Resorte superior

                // --- Seccion de Acciones Comunes ---
                Text(
                    text = "Acciones comunes",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    // Estado para el dialogo de confirmacion
                    var showAnimation by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier.widthIn(max = 300.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Form para Pagar a Jugador
                        PayPlayerForm(onPayClicked = {
                            showAnimation = true
                        })
                    }

                    // NUEVO: Animacion de exito
                    TransactionSuccessAnimation(
                        visible = showAnimation,
                        onAnimationFinished = {
                            showAnimation = false // <- Animation finished
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 15.dp) // Ajuste para centrarlo en Spacer
                    )
                }


                // --- Resorte Intermedio ---
                // Se expande para tomar todo el espacio sobrante
                Spacer(modifier = Modifier.weight(1f)) // Espacio entre secciones

                // Botones secundarios
                Column(
                    modifier = Modifier.widthIn(max = 400.dp),
                ){
                    Text(
                        text = "Manejar Propiedades",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SecondaryActionButton(
                            text = "Construir Casa/Hotel",
                            onClick = {
                                Log.d(
                                    "ActionsScreen",
                                    "Boton presionado: CONSTRUIR CASA/HOTEL"
                                )},
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryActionButton(
                            text = "Hipotecar",
                            onClick = { Log.d("ActionsScreen", "Boton presionado: HIPOTECAR")},
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryActionButton(
                            text = "Deshipotecar",
                            onClick = { Log.d("ActionsScreen", "Boton presionado: DESHIPOTECAR")},
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp)) // Espacio entre secciones

                    Text(
                        text = "Eventos del juego",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SecondaryActionButton(
                            text = "Arca Comunal",
                            onClick = { Log.d("ActionsScreen", "Boton presionado: ARCA COMUNAL")},
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryActionButton(
                            text = "Casualidad",
                            onClick = { Log.d("ActionsScreen", "Boton presionado: CASUALIDAD")},
                            modifier = Modifier.weight(1f)
                        )
                    }
                }


                Spacer(modifier = Modifier.height(24.dp)) // Espacio final

            }
        }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayPlayerForm(
    onPayClicked: () -> Unit
){
    var selectedPlayer by remember { mutableStateOf("Select Player") }
    var amount by remember { mutableStateOf("")}
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val players = listOf("Player 2", "Player 3", "The Banker") // Datos de ejemplo

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

    Spacer(modifier = Modifier.height(36.dp))

    // Boton principal
    Button(
        onClick = {
            Log.d("ActionsScreen", "Boton presionado: PAGAR A JUGADOR. Jugador: $selectedPlayer, Cantidad: $amount")
            // NUEVO: Avisar al Box que se debe mostrar la animacion
            onPayClicked()
        },
        modifier = Modifier.fillMaxWidth()
    ){
        Text("Pagar Jugador")
    }

}

/*
 Componente para botones secundarios (reutilizable)
 */
@Composable
fun SecondaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        // Colores de tema adaptables
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(text.uppercase())
    }
}

@Preview(showBackground = true)
@Composable
fun ActionsScreenPreview() {
    ActionsScreen()
}