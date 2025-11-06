package com.fabrik12.monopolyappwallet.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Game: Screen(
        route = "game_screen",
        label = "Juego",
        icon = Icons.Default.Home
    )
    object Actions: Screen(
        route = "actions_screen",
        label = "Acciones",
        icon = Icons.Default.PlayCircle
    )
    object Properties: Screen(
        route = "properties_screen",
        label = "Propiedades",
        icon = Icons.AutoMirrored.Filled.List
    )

    object Settings: Screen(
        route = "settings_screen",
        label = "Settings",
        icon = Icons.Default.Settings //Engranaje
    )
}