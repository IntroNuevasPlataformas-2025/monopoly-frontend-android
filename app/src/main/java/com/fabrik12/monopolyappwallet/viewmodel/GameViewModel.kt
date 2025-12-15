package com.fabrik12.monopolyappwallet.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fabrik12.monopolyappwallet.data.SettingsRepository
import com.fabrik12.monopolyappwallet.service.GameTimerService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application)

    /**
     * Se llama al iniciar una nueva partida para guardar el tiempo de inicio
     */
    fun startGameSession() {
        viewModelScope.launch {
            // Consultar a DataStore el tiempo actual del sistema
            val storedTime = settingsRepository.gameStartTimeFlow.first()

            val startTimeToSend = if (storedTime == 0L) {
                val now = System.currentTimeMillis()
                settingsRepository.saveGameStartTime(now) // Guardar el tiempo actual
                now
            } else {
                // Recuperar el tiempo almacenado previamente
                storedTime
            }

            // Iniciar el servicio con el tiempo de inicio
            startTimeService(startTimeToSend)
        }
    }

    /**
     * LLamar cuando el juego termina
     */
    fun stopGameSession() {
        viewModelScope.launch {
            // Limpiar persistencia
            settingsRepository.clearGameStartTime()

            // Detener el servicio
            stopTimeService()
        }
    }

    // Metodos para iniciar y detener el servicio de temporizador
    private fun startTimeService(startTime: Long) {
        val intent = Intent(getApplication(), GameTimerService::class.java).apply {
            action = GameTimerService.ACTION_START
            putExtra(GameTimerService.EXTRA_START_TIME, startTime)
        }
        getApplication<Application>().startService(intent)
    }

    private fun stopTimeService() {
        val intent = Intent(getApplication(), GameTimerService::class.java).apply {
            action = GameTimerService.ACTION_STOP
        }
        getApplication<Application>().startService(intent)
    }
}