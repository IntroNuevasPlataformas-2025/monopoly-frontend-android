package com.fabrik12.monopolyappwallet.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabrik12.monopolyappwallet.viewmodel.GameViewModel

@Composable
fun GameScreenContent(
    gameId: String?,
    onStopGame: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "!Te has unido a la partida!",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
        Text("ID: $gameId")
        
        Spacer(
            modifier = Modifier
                .height(12.dp)
        )

        // DEBUG: Botones para detener el temporizador
        Button(onClick = onStopGame) {
            Text("Terminar Partida (Stop Timer)")
        }
    }
}

@Composable
fun GameScreen(
    gameId: String?,
    gameViewModel: GameViewModel = viewModel()
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                gameViewModel.startGameSession()
            } else {
                // Permiso denegado
            }
        }
    )

    // Iniciar temporizador al entrar en la pantalla
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                gameViewModel.startGameSession()
            } else {
                // Pedir permiso
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            
        } else {
            gameViewModel.startGameSession()
        }
    }

    GameScreenContent(
        gameId = gameId,
        onStopGame = {
            gameViewModel.stopGameSession()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    GameScreenContent(
        gameId = "preview-123",
        onStopGame = {}
    )
}