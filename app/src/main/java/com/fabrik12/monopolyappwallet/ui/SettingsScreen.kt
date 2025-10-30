package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabrik12.monopolyappwallet.data.SettingsRepository
import com.fabrik12.monopolyappwallet.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    // Obtener el ViewModel
    val viewModel: SettingsViewModel = viewModel()

    // "Observar" el StateFlow del ViewModel
    val currentTheme by viewModel.themePreference.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Configuración")})
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
            // Grupo de opciones
            Column {
                ThemeOption(
                    text = "Claro",
                    selected = currentTheme == SettingsRepository.LIGHT_MODE,
                    onClick = {
                        viewModel.saveThemePreference(SettingsRepository.LIGHT_MODE)
                    }
                )
                ThemeOption(
                    text = "Oscuro",
                    selected = currentTheme == SettingsRepository.DARK_MODE,
                    onClick = {
                        viewModel.saveThemePreference(SettingsRepository.DARK_MODE)
                    }
                )
                ThemeOption(
                    text = "Automático (Sistema)",
                    selected = currentTheme == SettingsRepository.SYSTEM_MODE,
                    onClick = {
                        viewModel.saveThemePreference(SettingsRepository.SYSTEM_MODE)
                    }
                )
            }
        }
    }
}

@Composable
fun ThemeOption(text: String, selected: Boolean, onClick: () -> Unit) {
    TODO("Not yet implemented")
}