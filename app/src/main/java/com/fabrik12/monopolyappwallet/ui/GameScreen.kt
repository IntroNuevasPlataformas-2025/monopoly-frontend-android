package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GameScreen(gameId: String?) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    GameScreen(gameId = "preview-123")
}