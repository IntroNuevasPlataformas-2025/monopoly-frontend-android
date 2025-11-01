package com.fabrik12.monopolyappwallet.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = BrandBlueLight,      // Un azul más claro para que resalte en fondos oscuros
    background = BrandDarkText,    // Usamos el color de texto oscuro como fondo
    onBackground = Color.White,    // Texto blanco sobre fondo oscuro
    surface = BrandDarkText,
    onSurface = Color.White,
    onPrimary = BrandDarkText      // Texto oscuro sobre el botón azul claro
)

private val LightColorScheme = lightColorScheme(
    primary = BrandBlue,           // El color principal para botones
    background = Color.White,      // Fondo blanco estándar
    onBackground = BrandDarkText,  // El color del título sobre el fondo blanco
    surface = Color.White,
    onSurface = BrandDarkText,     // Color de texto sobre superficies
    onPrimary = Color.White        // Texto blanco sobre el botón azul
)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

@Composable
fun MonopolyAppWalletTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}