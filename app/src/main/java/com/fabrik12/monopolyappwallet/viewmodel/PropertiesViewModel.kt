package com.fabrik12.monopolyappwallet.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fabrik12.monopolyappwallet.data.MonopolyDatabase
import com.fabrik12.monopolyappwallet.data.PropertyEntity
import com.fabrik12.monopolyappwallet.data.PropertyRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * @brief Acceder al 'Application' Context desde el ViewModel
 */
class PropertiesViewModel(application: Application) : AndroidViewModel(application) {

    // Inicializar Repositorio
    private val repository: PropertyRepository

    // Exponer el Flow de datos como StateFlow
    val properties: StateFlow<List<PropertyEntity>>

    init {
        // Obtener instancia del DAO y del Repositorio
        val propertyDao = MonopolyDatabase.getInstance(application).propertyDao()
        repository = PropertyRepository(propertyDao)

        // Obtener todas las propiedades desde el repositorio
        properties = repository.allProperties.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // Empezar con lista vacia
        )
    }
}