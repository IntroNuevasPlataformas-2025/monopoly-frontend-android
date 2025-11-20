package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fabrik12.monopolyappwallet.navigation.Screen
import com.fabrik12.monopolyappwallet.ui.theme.MutedLight
import com.fabrik12.monopolyappwallet.ui.theme.MutedDark
import androidx.compose.material3.MaterialTheme

@Composable
fun MainScreen(gameId: String?) {
    // Navegacion interna
    val innerNavController = rememberNavController()

    // Lista de pantallas para barra inferior
    // Updated order based on design reference: Home, Properties, Actions, Settings
    val screens = listOf(
        Screen.Game, // Home
        Screen.Properties, // Foundation
        Screen.Actions, // SwapHoriz
        Screen.Settings
    )

    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    val colorScheme = MaterialTheme.colorScheme
    val unselectedColor = if (isDarkTheme) MutedDark else MutedLight

    Scaffold(
        bottomBar = {
            // Crear el MonopolyBottomNavigation
            NavigationBar(
                containerColor = colorScheme.surface // Fondo dinámico según tema
            ) {
                val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    // Verificar estado de seleccion
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            innerNavController.navigate(screen.route) {
                                // Acceder al destino inicial del gráfico para evitar acumular
                                // una gran cantidad de destinos en la pila de tareas
                                // a medida que los usuarios seleccionan elementos.
                                popUpTo(innerNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true // Evitar copias del mismo destino
                                restoreState = true // Restaurar el estado al volver
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.label
                            )
                        },
                        label = { Text(text = screen.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorScheme.primary,
                            selectedTextColor = colorScheme.primary,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = unselectedColor,
                            unselectedTextColor = unselectedColor
                        )
                    )
                }
            }
        }

    ) { innerPadding ->
        // Configurar el NavHost
        NavHost(
            navController = innerNavController,
            startDestination = Screen.Game.route, // Empezar en GameScreen 'pantalla de juego'
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Game.route) {
                // Pasar el gameId que necesita
                GameScreen(gameId = gameId)
            }
            composable(Screen.Properties.route) {
                PropertiesScreen()
            }
            composable(Screen.Actions.route) {
                ActionsScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
