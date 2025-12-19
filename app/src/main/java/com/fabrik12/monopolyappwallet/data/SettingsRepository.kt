package com.fabrik12.monopolyappwallet.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * @brief Crear una instancia unica (Singleton) de DataStore
 */
private const val DATASTORE_NAME = "settings"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)


/**
 * Repositorio para gestionar las preferencias de la aplicacion.
 *
 * @param context Contexto de la aplicacion. Para acceder a DataStore.
 */
class SettingsRepository(private val context: Context) {
    // Definicion de Claves y Valores

    // Guardar preferencias de tema
    private object Keys {
        val THEME_KEY = stringPreferencesKey("theme_preference")
        val GAME_START_TIME = longPreferencesKey("game_start_time")
        val SERVER_IP = stringPreferencesKey("server_ip")
    }

    // Posibles valores
    companion object {
        const val LIGHT_MODE = "LIGHT"
        const val DARK_MODE = "DARK"
        const val SYSTEM_MODE = "SYSTEM"
    }

    // Exponer el flujo
    // Lee desde Preferences Datastore
    val themePreferenceFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[Keys.THEME_KEY] ?: SYSTEM_MODE
        }

    // Servidor IP configurado por el usuario
    val serverIpFlow: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[Keys.SERVER_IP]
        }

    // Funcion para Guardar preferencia
    suspend fun saveThemePreference(theme: String) { // suspend para usar corrutinas
        context.dataStore.edit { preferences ->
            preferences[Keys.THEME_KEY] = theme
        }
    }

    // --- CRONOMETRO DE PARTIDA ---

    /**
     * Flujo que expone el tiempo de inicio de la partida.
     * Retorna 0L si no se ha establecido.
     */
    val gameStartTimeFlow: Flow<Long> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[Keys.GAME_START_TIME] ?: 0L
        }

    /**
     * Guarda el momento exacto en que inicio la partida.
     */
    suspend fun saveGameStartTime(startTime: Long) {
        context.dataStore.edit { preferences ->
            preferences[Keys.GAME_START_TIME] = startTime
        }
    }

    suspend fun saveServerIp(serverIp: String?) {
        context.dataStore.edit { preferences ->
            if (serverIp == null) preferences.remove(Keys.SERVER_IP)
            else preferences[Keys.SERVER_IP] = serverIp
        }
    }

    /**
     * Borra el tiempo de inicio de la partida.
     */
    suspend fun clearGameStartTime() {
        context.dataStore.edit { preferences ->
            preferences.remove(Keys.GAME_START_TIME)
        }
    }
}