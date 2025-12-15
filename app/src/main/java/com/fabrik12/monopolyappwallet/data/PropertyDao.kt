package com.fabrik12.monopolyappwallet.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    /**
     * @brief Insertar una lista de propiedades en la base de datos.
     * Si ya existe una propiedad con el mismo ID, se actualizará.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProperties(properties: List<PropertyEntity>)

    /**
     * @brief Obtener todas las propiedades desde la base de datos.
     * @return Un flujo que emite una lista de propiedades.
     */
    @Query("SELECT * FROM properties")
    fun getAllProperties(): Flow<List<PropertyEntity>>

    /**
     * @brief Obtener todas las propiedades desde la base de datos una sola vez.
     * @return Una lista de propiedades.
     */
    @Query("SELECT * FROM properties")
    suspend fun getAllPropertiesOneShot(): List<PropertyEntity>

    /**
     * @brief Actualizar una propiedad existente en la base de datos.
     * @param property La entidad de la propiedad a actualizar.
     */
    @Update
    suspend fun updateProperty(property: PropertyEntity)
}