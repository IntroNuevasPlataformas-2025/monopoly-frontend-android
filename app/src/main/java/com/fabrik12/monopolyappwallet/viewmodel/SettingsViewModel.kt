package com.fabrik12.monopolyappwallet.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.fabrik12.monopolyappwallet.data.SettingsRepository
import com.fabrik12.monopolyappwallet.worker.ServerSimulationWorker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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

    // Server IP
    val serverIp: StateFlow<String?> = settingsRepository.serverIpFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun saveThemePreference(theme: String) {
        viewModelScope.launch {
            settingsRepository.saveThemePreference(theme)
        }
    }

    fun saveServerIp(ip: String?) {
        viewModelScope.launch { settingsRepository.saveServerIp(ip) }
    }

    /**
     * @brief Iniciar una simulación de servidor una sola vez.
     * Utiliza WorkManager para gestionar la tarea en segundo plano.
     */
    fun triggerOneTimeSimulation() {
        val workRequest = OneTimeWorkRequestBuilder<ServerSimulationWorker>()
            .build()

        WorkManager.getInstance(getApplication()).enqueue(workRequest)

    }

    fun startPeriodicSimulation() {
        val periodicRequest = PeriodicWorkRequestBuilder<ServerSimulationWorker>(
            15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(getApplication()).enqueueUniquePeriodicWork(
            "MonopolyServerSimulation",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }
}