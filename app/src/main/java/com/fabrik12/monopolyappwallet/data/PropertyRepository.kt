package com.fabrik12.monopolyappwallet.data

import kotlinx.coroutines.flow.Flow

/**
 * @brief El Repositorio es una clase que se encarga de interactuar con los datos.
 *
 * @param propertyDao La interfaz DAO para acceder a los datos de las propiedades.
 */
class PropertyRepository(private val propertyDao: PropertyDao) {

    /**
     * @brief Obtener todas las propiedades desde la base de datos.
     * @return Un flujo que emite una lista de propiedades.
     */
    val allProperties: Flow<List<PropertyEntity>> = propertyDao.getAllProperties()

    /**
     * @brief Insertar o actualizar una lista de propiedades en la base de datos.
     * @param properties La lista de propiedades a insertar o actualizar.
     */
    suspend fun insertOrUpdateProperties(properties: List<PropertyEntity>) {
        propertyDao.insertOrUpdateProperties(properties)
    }
}