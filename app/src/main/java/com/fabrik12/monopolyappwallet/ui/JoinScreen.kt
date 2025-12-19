package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabrik12.monopolyappwallet.ui.theme.*

@Composable
fun JoinScreen(navController: NavHostController) {
    val playerName = remember { mutableStateOf("") }
    val gameId = remember { mutableStateOf("") }
    val isDarkTheme = isSystemInDarkTheme()
    val settingsViewModel: com.fabrik12.monopolyappwallet.viewmodel.SettingsViewModel = viewModel()
    val settingsServerIp by settingsViewModel.serverIp.collectAsState()

    val gameViewModel: com.fabrik12.monopolyappwallet.viewmodel.GameViewModel = viewModel()

    // Local UI state
    val isJoining = remember { androidx.compose.runtime.mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme

    val backgroundColor = colorScheme.background
    val surfaceColor = colorScheme.surface
    val textColor = colorScheme.onBackground
    val mutedColor = if (isDarkTheme) MutedDark else MutedLight // Mantener muted personalizado para mejor contraste
    val borderColor = if (isDarkTheme) BorderDark else BorderLight // Mantener border personalizado
    val iconPrimary = colorScheme.primary
    val iconSecondary = if (isDarkTheme) MutedDark else MutedLight // Gris para claro, gris oscuro para dark

    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney, // Icono de billete, más temático para Monopoly
                    contentDescription = "Monopoly Icon",
                    tint = iconPrimary,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Crear o Unirse a una Partida",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = playerName.value,
                    onValueChange = { playerName.value = it },
                    placeholder = { Text("Tu nombre", color = mutedColor) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Person Icon",
                            tint = iconSecondary // Gris en claro, gris oscuro en dark
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = iconPrimary,
                        unfocusedBorderColor = borderColor,
                        unfocusedContainerColor = surfaceColor,
                        focusedContainerColor = surfaceColor,
                        unfocusedTextColor = textColor,
                        focusedTextColor = textColor,
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = gameId.value,
                    onValueChange = { gameId.value = it },
                    placeholder = { Text("Nombre o ID de la partida", color = mutedColor) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.MeetingRoom,
                            contentDescription = "Meeting Room Icon",
                            tint = iconSecondary // Gris en claro, gris oscuro en dark
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = iconPrimary,
                        unfocusedBorderColor = borderColor,
                        unfocusedContainerColor = surfaceColor,
                        focusedContainerColor = surfaceColor,
                        unfocusedTextColor = textColor,
                        focusedTextColor = textColor,
                    ),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (gameId.value.isNotBlank()) {
                            // Crear partida localmente sin usar WebSocket (mantener comportamiento previo)
                            navController.navigate("main_screen/${gameId.value}")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = iconPrimary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    Text(
                        "Crear Partida",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Unirse con WebSocket real: botón habilitado solo si nombre no vacío
                OutlinedButton(
                    onClick = {
                        if (playerName.value.isNotBlank()) {
                            isJoining.value = true
                            // Usar IP configurada si existe, si no usar valor por defecto en GameViewModel/wsClient
                            gameViewModel.joinGame(playerName.value, settingsServerIp)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = playerName.value.isNotBlank(),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                        width = 2.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(iconPrimary)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = iconPrimary
                    )
                ) {
                    if (isJoining.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = iconPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        "Unirse",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }

                // Navegar automáticamente cuando GameViewModel indique que gameStatus ya no es null
                val gameStatus by gameViewModel.gameStatus.collectAsState()
                LaunchedEffect(gameStatus) {
                    if (gameStatus != null) {
                        isJoining.value = false
                        // Navegar a la pantalla principal. Si el servidor retorna gameId en GameState, preferirlo.
                        val gid = gameViewModel.gameStatus.value ?: gameId.value.ifBlank { "unknown" }
                        navController.navigate("main_screen/${gid}")
                    }
                }

                // Mostrar Snackbar si WebSocket emite error
                val snackbarHostState = remember { SnackbarHostState() }
                LaunchedEffect(Unit) {
                    gameViewModel.uiEvents.collectLatest { ev ->
                        snackbarHostState.showSnackbar(ev)
                        isJoining.value = false
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar el host de snackbars
                SnackbarHost(hostState = snackbarHostState)

            }
        }
    }
}

/**
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun JoinScreenPreviewLight() {
    MonopolyAppWalletTheme(darkTheme = false, dynamicColor = false) {
        JoinScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun JoinScreenPreviewDark() {
    MonopolyAppWalletTheme(darkTheme = true, dynamicColor = false) {
        JoinScreen(navController = rememberNavController())
    }
}
 */