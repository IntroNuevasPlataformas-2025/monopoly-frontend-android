package com.fabrik12.monopolyappwallet.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var SuccessGreen = Color(0xFF4CAF50)

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
    val alpha = remember { Animatable(1f) }

    // Lanzar efecto cada vez que visible cambia
    LaunchedEffect(visible) {
        if (visible) {
            // Resetear valores iniciales
            scale.snapTo(0f)
            alpha.snapTo(1f)

            // Al iniciar animacion de expansion
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 400) // 0.4 segundos de expandido
                )
            }

            // Al terminar animacion de expansion, iniciar desvanecimiento
            launch {
                delay(300)
                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 300) // 0.3 segundos de desvanecimiento
                )
            }

            // Esperar antes de resetear el estado 'visible'
            delay(600)
            onAnimationFinished()
        }
    }

    // Logica para dibujar el circulo
    Canvas(modifier = modifier.fillMaxSize()) {
        if (scale.value > 0f) { // Solo dibujar si es visible
            drawCircle(
                color = SuccessGreen,
                // Radio basado en el tamanio del Canvas
                radius = size.minDimension / 2 * scale.value,
                center = center,
                // Aplicar transparencia animada
                alpha = alpha.value
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionSuccessAnimationPreview() {
    // Estado intermedio para previsualizar la animacion
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = SuccessGreen,
            radius = size.minDimension / 2 * 0.5f, // Mitad de tamaño
            center = center,
            alpha = 0.8f // Ligeramente transparente
        )
    }
}
