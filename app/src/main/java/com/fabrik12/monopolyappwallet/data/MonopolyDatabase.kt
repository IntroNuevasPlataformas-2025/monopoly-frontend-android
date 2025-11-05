package com.fabrik12.monopolyappwallet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

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
                ).fallbackToDestructiveMigration().build() // Simple para pruebas
                INSTANCE = instance
                instance
            }
        }
    }
}