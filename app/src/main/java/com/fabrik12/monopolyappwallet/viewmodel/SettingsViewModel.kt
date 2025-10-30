package com.fabrik12.monopolyappwallet.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fabrik12.monopolyappwallet.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @brief Acceder al 'Application' Context desde el ViewModel
 * para luego pasarlo a SettingsDataStore
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    // Instancia del repositorio
    private val settingsRepository = SettingsRepository(application)

    val themePreference: StateFlow<String> = settingsRepository.themePreferenceFlow
        .stateIn(
            scope = viewModelScope, // "ciclo de vida" del ViewModel
            started = SharingStarted.WhileSubscribed(5000), // Mantener en memoria
            initialValue = SettingsRepository.SYSTEM_MODE // Valor inicial
        )

    fun saveThemePreference(theme: String) {
        viewModelScope.launch {
            settingsRepository.saveThemePreference(theme)
        }
    }
}