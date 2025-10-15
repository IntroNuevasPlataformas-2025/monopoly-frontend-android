package com.fabrik12.monopolyappwallet.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * Composable que muestra animacion de exito
 *
 * @param visible Control si la animacion se debe mostrar o no
 * @param modifier Personalizar el layout
 */
@Composable
fun TransactionSuccessAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier
){
    // Logica para dibujar el circulo
    /* TO-DO */
}

@Preview(showBackground = true)
@Composable
fun TransactionSuccessAnimationPreview() {
    // Previsualizar como visible
    TransactionSuccessAnimation(visible = true)
}
