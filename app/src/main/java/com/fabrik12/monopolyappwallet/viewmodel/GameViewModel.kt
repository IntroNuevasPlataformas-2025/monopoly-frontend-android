package com.fabrik12.monopolyappwallet.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fabrik12.monopolyappwallet.data.SettingsRepository
import com.fabrik12.monopolyappwallet.service.GameTimerService
import com.fabrik12.monopolyappwallet.ui.WebSocketClient
import com.fabrik12.monopolyappwallet.data.models.Player
import com.fabrik12.monopolyappwallet.data.models.GameState
import com.fabrik12.monopolyappwallet.data.models.WebSocketMessage
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

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

    // WebSocket client (creado si no hay DI). No conectar todavía: se conectará cuando se una al juego.
    private val wsClient: WebSocketClient = WebSocketClient.createDefault()
    private val gson = Gson()

    // Player id actual (se puede establecer desde UI al iniciar sesión /unirse)
    private var currentPlayerId: String? = null

    // UI-exposed state
    private val _currentBalance = MutableStateFlow<Int?>(null)
    val currentBalance: StateFlow<Int?> = _currentBalance.asStateFlow()

    private val _myProperties = MutableStateFlow<List<String>>(emptyList())
    val myProperties: StateFlow<List<String>> = _myProperties.asStateFlow()

    private val _gameStatus = MutableStateFlow<String?>(null)
    val gameStatus: StateFlow<String?> = _gameStatus.asStateFlow()

    // Lista de jugadores expuesta para la UI (dropdown de pago)
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    // Lista de propiedades disponibles (no compradas)
    private val ALL_PROPERTIES = listOf("mediterranean_avenue", "boardwalk", "park_place", "baltic_avenue")
    private val _availableProperties = MutableStateFlow<List<String>>(ALL_PROPERTIES)
    val availableProperties: StateFlow<List<String>> = _availableProperties.asStateFlow()

    // UI events (errores, notificaciones)
    private val _uiEvents = MutableSharedFlow<String>(replay = 0)
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        // Colectar game state y eventos de WebSocket
        viewModelScope.launch {
            wsClient.gameState.collectLatest { gs ->
                // Actualizar status
                _gameStatus.emit(gs?.status)

                // Actualizar lista de jugadores
                _players.emit(gs?.players ?: emptyList())

                // Actualizar propiedades y balance del jugador actual
                val pid = currentPlayerId
                if (pid != null && gs != null) {
                    val player = gs.players.find { it.id == pid }
                    _currentBalance.emit(player?.balance)
                    _myProperties.emit(player?.properties ?: emptyList())
                } else if (pid == null) {
                    // Si no hay playerId aún, dejar valores nulos/vacíos
                    _currentBalance.emit(null)
                    _myProperties.emit(emptyList())
                }

                // Calcular propiedades disponibles (no compradas)
                val owned = gs?.players?.flatMap { it.properties }?.toSet() ?: emptySet()
                _availableProperties.emit(ALL_PROPERTIES.filter { !owned.contains(it) })
            }
        }

        viewModelScope.launch {
            wsClient.events.collectLatest { ev ->
                // Reemite eventos como UI events
                _uiEvents.emit(ev)
            }
        }
    }

    /**
     * Establecer el id del jugador actual (usado para filtrar propiedades/balance)
     */
    fun setPlayerId(playerId: String) {
        this.currentPlayerId = playerId
        // Reconstruir estado inmediato desde el último GameState
        val lastState = wsClient.gameState.value
        viewModelScope.launch {
            _gameStatus.emit(lastState?.status)
            val player = lastState?.players?.find { it.id == playerId }
            _currentBalance.emit(player?.balance)
            _myProperties.emit(player?.properties ?: emptyList())
        }
    }

    /**
     * Unirse / crear partida (se manda CREATE_GAME al servidor)
     */
    fun joinGame(playerName: String, serverUrl: String? = null) {
        val pid = currentPlayerId ?: "android-${System.currentTimeMillis()}"
        currentPlayerId = pid

        // Conectar al servidor (si se pasa serverUrl lo usaremos), y enviar CREATE_GAME
        wsClient.connect(serverUrl)

        val payload = JsonObject().apply {
            addProperty("playerId", pid)
            addProperty("playerName", playerName)
            // gameId opcional: dejar fuera para que el servidor lo genere
        }
        val msg = JsonObject().apply {
            addProperty("type", "CREATE_GAME")
            add("payload", payload)
        }

        wsClient.send(gson.toJson(msg))
    }

    /**
     * Enviar pago (transactionType = payment). toPlayerId es opcional.
     */
    fun sendPayment(amount: Int, toPlayerId: String? = null) {
        val pid = currentPlayerId ?: run {
            viewModelScope.launch { _uiEvents.emit("Player ID not set") }
            return
        }
        val payload = JsonObject().apply {
            addProperty("transactionType", "payment")
            addProperty("toPlayerId", toPlayerId)
            addProperty("amount", amount)
            addProperty("source", pid)
        }
        val msg = JsonObject().apply {
            addProperty("type", "PROCESS_TRANSACTION")
            add("payload", payload)
        }
        wsClient.send(gson.toJson(msg))
    }

    /**
     * Solicitar compra de propiedad
     * Nota: por simplicidad se envía amount = 0 si no está disponible.
     */
    fun buyProperty(propertyId: String, amount: Int = 0) {
        val pid = currentPlayerId ?: run {
            viewModelScope.launch { _uiEvents.emit("Player ID not set") }
            return
        }
        val payload = JsonObject().apply {
            addProperty("playerId", pid)
            addProperty("propertyId", propertyId)
            addProperty("amount", amount)
        }
        val msg = JsonObject().apply {
            addProperty("type", "REQUEST_PURCHASE")
            add("payload", payload)
        }
        wsClient.send(gson.toJson(msg))
    }

    override fun onCleared() {
        super.onCleared()
        wsClient.disconnect()
    }

    /*
     * Snippet de ejemplo para un @Preview (comentado):
     *
     * @Preview
     * @Composable
     * fun PreviewBalance() {
     *   val vm = /* obtener instancia de GameViewModel */
     *   val balance by vm.currentBalance.collectAsState(null)
     *   Text(text = "Balance actual: ${'$'}{balance ?: "--"}")
     * }
     */
}
