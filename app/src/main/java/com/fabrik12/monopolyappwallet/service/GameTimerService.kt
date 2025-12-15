package com.fabrik12.monopolyappwallet.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.fabrik12.monopolyappwallet.R
import kotlinx.coroutines.*
import okhttp3.internal.notify

class GameTimerService: Service() {
    // Alcance del servicio
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    companion object {
        const val CHANNEL_ID = "monopoly_game_timer_channel"
        const val NOTIFICATION_ID = 1
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val EXTRA_START_TIME = "EXTRA_START_TIME"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                // Recibir el tiempo de inicio desde el Intent
                val startTime = intent.getLongExtra(EXTRA_START_TIME, System.currentTimeMillis())
                startTimer(startTime)
            }
            ACTION_STOP -> {
                stopTimer()
            }
        }
        return START_NOT_STICKY
    }

    private fun startTimer(startTime: Long) {
        // Lanzar notificacion inicial
        val notification = buildNotification("Calculando tiempo...")

        // Compatibilidad con versiones anteriores de Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        // Inicial el bucle
        serviceScope.launch {
            while (isActive) {
                val tiempoTranscurrido = System.currentTimeMillis() - startTime
                val tiempoFormateado = formatTime(tiempoTranscurrido)

                // Actualizar notificacion
                val updatedNotification = buildNotification("Tiempo de partida: $tiempoFormateado")
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NOTIFICATION_ID, updatedNotification)

                delay(1000L) // Actualizar cada segundo
            }
        }
    }

    private fun stopTimer() {
        serviceJob.cancel() // Detener el bucle
        stopForeground(STOP_FOREGROUND_REMOVE) // Detener el servicio en primer plano
        stopSelf() // Detener el servicio
    }

    // Metodos auxiliares
    private fun buildNotification(contentText: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Monopoly Wallet En Juego")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.outline_timer_play_24)
            .setOngoing(true) // Notificacion persistente
            .setOnlyAlertOnce(true) // No alertar en cada actualizacion
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Cronometro de Partida",
                NotificationManager.IMPORTANCE_LOW // Importancia baja para no molestar
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel() // Limpieza final al destruir el servicio
    }


}