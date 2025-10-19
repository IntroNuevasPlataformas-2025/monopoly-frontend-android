package com.fabrik12.monopolyappwallet.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

var SuccessGreen = Color(0x67FF6B)

/**
 * Composable que muestra animacion de exito (circulo verde que se expande y desvanece).
 *
 * @param visible Control si la animacion se debe mostrar o no
 * @param onAnimationFinished Callback que se invoca cuando la animacion termina,
 * para permitir al componente padre resetear el estado 'visible'
 */
@Composable
fun TransactionSuccessAnimation(
    visible: Boolean,
    onAnimationFinished: () -> Unit, // Avisar a ActionsScreen cuando se termina la animacion
    modifier: Modifier = Modifier
){
    // Valores para la animacion
    // 'scale' y 'alpha' se usan para controlar el tamaño y opacidad del circulo
    val scale = remember { Animatable(0f) }
    val verticalOffset = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    val gradientStop = remember { Animatable(0f) } // 0.5f = Sólido, 1f = Borde

    // Lanzar efecto cada vez que visible cambia
    LaunchedEffect(visible) {
        if (visible) {
            // Resetear valores iniciales
            scale.snapTo(0f)
            alpha.snapTo(1f)
            verticalOffset.snapTo(0f)
            gradientStop.snapTo(0f) // Empezar solido

            // --- Etapa 1: "Punto" (350ms) ---
            scale.animateTo(0.3f, tween(150))
            delay(350) // Sostener un instante

            // --- Etapa 2: "Descenso y Encogimiento" (300ms) ---
            launch { verticalOffset.animateTo(20f, tween(400)) } // Descenso controlado
            scale.animateTo(0.15f, tween(400)) // Se encoge aún más
            delay(400) // Esperar a que termine

            // --- Etapa 3: "Onda" (600ms) ---
            // Cambiar desde estado 0.1f, ocurre la metamorfosis
            launch { scale.animateTo(8f, tween(600)) } // Expansión masiva
            launch { alpha.animateTo(0f, tween(600)) } // Desvanecimiento
            // La "onda": el anillo verde viaja hacia afuera
            gradientStop.animateTo(1.0f, tween(600))


            // fin
            delay(500)
            onAnimationFinished() // NOtificar fin de animacion
        }
    }

    // Logica para dibujar el circulo
    Canvas(
        modifier = modifier
            .size(100.dp)
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationY = verticalOffset.value,
                alpha = alpha.value // El alfa maestro controla el desvanecimiento
            )
    ) {
        val radius = size.minDimension / 2
        val currentStop = gradientStop.value // El valor animado de la "onda"

        // Circulo Solido
        drawCircle(
            brush = Brush.radialGradient(
                // Lista de paradas de color (color y posición)
                0.0f to SuccessGreen.copy(alpha = 1f - currentStop), // El centro se vuelve transparente
                (currentStop * 0.5f) to SuccessGreen, // Anillo verde
                (currentStop * 0.9f).coerceAtLeast(0.01f) to SuccessGreen, // Anillo verde (más sólido)
                (currentStop).coerceAtLeast(0.02f) to SuccessGreen.copy(alpha = 0.5f), // Borde difuso
                1.0f to Color.Transparent, // Borde exterior con "blur"
                radius = radius
            ),
            radius = radius
        )
    }
}

// --- Preview (Solo para referencia) ---
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun TransactionSuccessAnimationPreview() {
    TransactionSuccessAnimation(visible = true, onAnimationFinished = {})
}
