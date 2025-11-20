package com.fabrik12.monopolyappwallet.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fabrik12.monopolyappwallet.ui.theme.*

@Composable
fun JoinScreen(navController: NavHostController) {
    val playerName = remember { mutableStateOf("") }
    val gameId = remember { mutableStateOf("") }
    val isDarkTheme = isSystemInDarkTheme()

    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val surfaceColor = if (isDarkTheme) SurfaceDark else SurfaceLight
    val textColor = if (isDarkTheme) TextDark else TextLight
    val mutedColor = if (isDarkTheme) MutedDark else MutedLight
    val borderColor = if (isDarkTheme) BorderDark else BorderLight

    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Casino,
                contentDescription = "Casino Icon",
                tint = Primary,
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Crear o Unirse a una Partida",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = playerName.value,
                onValueChange = { playerName.value = it },
                placeholder = { Text("Tu nombre", color = mutedColor) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person Icon",
                        tint = mutedColor
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = borderColor,
                    unfocusedContainerColor = surfaceColor,
                    focusedContainerColor = surfaceColor,
                    unfocusedTextColor = textColor,
                    focusedTextColor = textColor,
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = gameId.value,
                onValueChange = { gameId.value = it },
                placeholder = { Text("Nombre o ID de la partida", color = mutedColor) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MeetingRoom,
                        contentDescription = "Meeting Room Icon",
                        tint = mutedColor
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = borderColor,
                    unfocusedContainerColor = surfaceColor,
                    focusedContainerColor = surfaceColor,
                    unfocusedTextColor = textColor,
                    focusedTextColor = textColor,
                ),
                singleLine = true
            )

            Button(
                onClick = {
                    if (gameId.value.isNotBlank()) {
                        navController.navigate("main_screen/${gameId.value}")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Crear Partida",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    if (gameId.value.isNotBlank()) {
                        navController.navigate("main_screen/${gameId.value}")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(Primary)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Primary
                )
            ) {
                Text(
                    "Unirse",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun JoinScreenPreviewLight() {
    MonopolyAppWalletTheme(darkTheme = false) {
        JoinScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun JoinScreenPreviewDark() {
    MonopolyAppWalletTheme(darkTheme = true) {
        JoinScreen(navController = rememberNavController())
    }
}