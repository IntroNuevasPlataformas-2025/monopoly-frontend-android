package com.fabrik12.monopolyappwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fabrik12.monopolyappwallet.ui.ActionsScreen
import com.fabrik12.monopolyappwallet.ui.GameScreen
import com.fabrik12.monopolyappwallet.ui.theme.MonopolyAppWalletTheme

import com.fabrik12.monopolyappwallet.ui.JoinScreen
import com.fabrik12.monopolyappwallet.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonopolyAppWalletTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "join_screen") {
                        composable("join_screen") {
                            JoinScreen(navController = navController)
                        }
                        composable("main_screen/{gameId}") { backStackEntry ->
                            val gameId = backStackEntry.arguments?.getString("gameId")
                            MainScreen(gameId = gameId) // Llamada al nuevo composable
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MonopolyAppWalletTheme {
        Greeting("Android")
    }
}