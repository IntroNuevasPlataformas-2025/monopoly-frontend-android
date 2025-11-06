package com.fabrik12.monopolyappwallet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PropertyEntity::class], version = 1, exportSchema = false)
@TypeConverters(ColorConverter::class) // Usar el convertidor personalizado
abstract class MonopolyDatabase : RoomDatabase() {
    // Funciones implementadas por Room
    abstract fun propertyDao(): PropertyDao

    // Patron singleton para la base de datos
    companion object {
        @Volatile
        private var INSTANCE: MonopolyDatabase? = null

        fun getInstance(context: Context): MonopolyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MonopolyDatabase::class.java,
                    "monopoly_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(DatabaseSeeder(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * @brief Manejar el "seeder" de la base de datos.
 * @param context Contexto de la aplicacion.
 */
private class DatabaseSeeder(private val context: Context) : RoomDatabase.Callback() {
    /**
     * @brief Llenar la base de datos con datos de maqueta (Mock Data).
     * SOLO SE EJECUTA LA PRIMERA VEZ.
     */
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // Obtener la instancia de la base de datos
        val database = MonopolyDatabase.getInstance(context)

        // Corrutina para insertar datos de maqueta en un hilo de fondo
        // De acuerdo con 'suspend' en DAO y Repositorio
        CoroutineScope(Dispatchers.IO).launch {
            database.propertyDao().insertOrUpdateProperties(mockPropertyEntityList)
        }
    }
}