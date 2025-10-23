package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.material3.NavigationBarItem
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fabrik12.monopolyappwallet.navigation.Screen

@Composable
fun MainScreen(gameId: String?) {
    // Navegacion interna
    val innerNavController = rememberNavController()

    // Lista de pantallas para barra inferior
    val screens = listOf(
        Screen.Game,
        Screen.Actions,
        Screen.Properties
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            screens.forEach { screen ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
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
                    icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                    label = { Text(text = screen.label) }
                )
            }
        }

    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = Screen.Game.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Game.route) {
                GameScreen(gameId = gameId)
            }
            composable(Screen.Actions.route) {
                ActionsScreen()
            }
            composable(Screen.Properties.route) {
                PropertiesScreen()
            }
        }
    }
}