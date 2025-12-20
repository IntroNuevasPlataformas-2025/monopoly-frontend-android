package com.fabrik12.monopolyappwallet.ui

import android.util.Log
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabrik12.monopolyappwallet.viewmodel.GameViewModel

@Composable
fun MainScreen(
    gameId: String?,
    gameViewModel: GameViewModel
) {
    Log.d("ViernesDebug", "MainScreen - VM Hash: ${System.identityHashCode(gameViewModel)}")

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
                GameScreen(
                    gameId = gameId,
                    gameViewModel = gameViewModel
                )
            }
            composable(Screen.Properties.route) {
                PropertiesScreen(
                    gameViewModel = gameViewModel
                )
            }
            composable(Screen.Actions.route) {
                ActionsScreen(
                    gameViewModel = gameViewModel
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
