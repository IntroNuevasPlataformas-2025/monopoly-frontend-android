package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabrik12.monopolyappwallet.data.SettingsRepository
import com.fabrik12.monopolyappwallet.viewmodel.SettingsViewModel

/**
 * @brief Recibir el tema actual y una funcion para cambiar el tema
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    currentTheme: String,
    onThemeSelected: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Configuración") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
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

    SettingsScreenContent(
        currentTheme = currentTheme,
        onThemeSelected = { viewModel.saveThemePreference(it) }
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
        onThemeSelected = {} // No-op para preview
    )
}