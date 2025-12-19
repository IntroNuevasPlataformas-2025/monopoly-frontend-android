package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.fabrik12.monopolyappwallet.data.SettingsRepository
import com.fabrik12.monopolyappwallet.viewmodel.SettingsViewModel

/**
 * @brief Recibir el tema actual y una funcion para cambiar el tema
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onOneTimeSimulation: () -> Unit,
    onPeriodicSimulation: () -> Unit,
    serverIp: String?,
    onSaveServerIp: (String?) -> Unit
) {
    val iconPrimary = colorScheme.primary

    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Configuración") })
        },
        snackbarHost = { androidx.compose.material3.SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Seleccionar Tema",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Column {
                ThemeOption(
                    text = "Claro",
                    selected = currentTheme == SettingsRepository.LIGHT_MODE,
                    onClick = { onThemeSelected(SettingsRepository.LIGHT_MODE) }
                )
                ThemeOption(
                    text = "Oscuro",
                    selected = currentTheme == SettingsRepository.DARK_MODE,
                    onClick = { onThemeSelected(SettingsRepository.DARK_MODE) }
                )
                ThemeOption(
                    text = "Automático (Sistema)",
                    selected = currentTheme == SettingsRepository.SYSTEM_MODE,
                    onClick = { onThemeSelected(SettingsRepository.SYSTEM_MODE) }
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 24.dp)
            )

            Text(
                text = "Developer Tools (Lab 10)",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error, // Color de advertencia
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Simulador de Servidor",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // IP del servidor editable
            val serverIpInput = remember { mutableStateOf(serverIp ?: "") }

            OutlinedTextField(
                value = serverIpInput.value,
                onValueChange = { serverIpInput.value = it },
                placeholder = { Text("WebSocket server IP (ej: 10.0.2.2:3000 o ws://10.0.2.2:3000)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                maxLines = 1
            )

            val scope = rememberCoroutineScope()

            Button(
                onClick = {
                    val raw = serverIpInput.value.ifBlank { null }
                    val normalized = raw?.let { if (it.startsWith("ws://") || it.startsWith("wss://")) it else "ws://$it" }
                    scope.launch {
                        onSaveServerIp(normalized)
                        // Mostrar snackbar verde de 2 segundos
                        val message = "Configuración guardada: ${normalized ?: "(eliminada)"}"
                        snackbarHostState.showSnackbar(message)
                        kotlinx.coroutines.delay(2000)
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Guardar IP")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Btn 1: Simulacion unica
            Button(
                onClick = onOneTimeSimulation,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = iconPrimary,
                    contentColor = colorScheme.onPrimary
            )
            ) {
                Text("Simular Evento Único")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Btn 2: Simulacion periodica
            OutlinedButton(
                onClick = onPeriodicSimulation,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Worker Periodico (15 min)")
            }
        }
    }
}

/**
 * @brief Pantalla de Configuracion de Tema
 * Como un envoltorio para SettingsViewModel
 */
@Composable
fun SettingsScreen() {
    // Obtener el ViewModel
    val viewModel: SettingsViewModel = viewModel()
    // "Observar" el tema actual
    val currentTheme by viewModel.themePreference.collectAsState()
    val serverIpState by viewModel.serverIp.collectAsState()

    SettingsScreenContent(
        currentTheme = currentTheme,
        onThemeSelected = { viewModel.saveThemePreference(it) },
        onOneTimeSimulation = { viewModel.triggerOneTimeSimulation() },
        onPeriodicSimulation = { viewModel.startPeriodicSimulation() },
        serverIp = serverIpState,
        onSaveServerIp = { viewModel.saveServerIp(it) }
    )
}

/**
 * @brief Componente para una opcion de tema
 */
@Composable
fun ThemeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Toda la fila clickable
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick // radio button click
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)

    }

}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        currentTheme = SettingsRepository.DARK_MODE,
        onThemeSelected = {}, // No-op para preview
        onOneTimeSimulation = {},
        onPeriodicSimulation = {},
        serverIp = "ws://10.0.2.2:3000",
        onSaveServerIp = {}
    )
}