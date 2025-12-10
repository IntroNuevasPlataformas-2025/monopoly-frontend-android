package com.fabrik12.monopolyappwallet.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fabrik12.monopolyappwallet.data.MonopolyDatabase

class ServerSimulationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        // Log para trazabilidad
        Log.d("SimuladorServer", "--- INICIA SIMULACION DE SERVIDOR ---")

        return try {
            // Instanciar la base de datos y el DAO
            val database = MonopolyDatabase.getInstance(applicationContext)
            val dao = database.propertyDao()

            val properties = dao.getAllPropertiesOneShot() // Obtener todas las propiedades una sola vez

            if (properties.isNotEmpty()) {
                val randomProperty = properties.random() // Seleccionar una propiedad aleatoria

                val estadosMonopoly = listOf(
                    "Propiedad de: CPU_Alfa",  // Simula compra
                    "Propiedad de: CPU_Beta",  // Simula otro comprador
                    "Hipotecada",              // Mecánica clave del juego
                    "En Subasta",              // Regla oficial
                    "Con 1 Casa",              // Desarrollo
                    "Libre"                    // Reseteo (Bancarrota)
                )

                val nuevoEstado = estadosMonopoly.random()

                Log.d("SimuladorServer", "Propiedad seleccionada: ${randomProperty.name}")
                Log.d("SimuladorServer", "Cambio: '${randomProperty.status}' -> '$nuevoEstado'")

                val propiedadModificada = randomProperty.copy(
                    status = nuevoEstado
                )

                dao.updateProperty(propiedadModificada) // Actualizar la propiedad en la base de datos

                Log.d("SimuladorServer", "--- CAMBIO GUARDADO CON EXITO ---")
                Result.success()

            } else {
                Log.d("SimuladorServer", "No hay propiedades disponibles para simular.")
                Result.success()
            }

        } catch (e: Exception) {
            Log.e("SimuladorServer", "Error durante la simulacion del servidor", e)
            Result.failure()
        }
    }
}