package com.fabrik12.monopolyappwallet.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.RealEstateAgent // Requires extended icons
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fabrik12.monopolyappwallet.ui.theme.*
import kotlinx.coroutines.launch

// Enum to identify which action is selected
enum class ActionType {
    PAY_PLAYER,
    BANK_OPERATIONS,
    BUILD,
    MORTGAGE,
    UNMORTGAGE,
    NONE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsScreen() {
    var showBottomSheet by remember { mutableStateOf(false) }
    var currentAction by remember { mutableStateOf(ActionType.NONE) }
    var showSuccessAnimation by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Acciones") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Transacciones",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                item {
                    ActionCard(
                        title = "Pagar a Jugador",
                        icon = Icons.Default.Groups,
                        backgroundColorLight = ActionBlueBgLight,
                        iconColorLight = ActionBlueTextLight,
                        backgroundColorDark = ActionBlueBgDark,
                        iconColorDark = ActionBlueTextDark,
                        onClick = {
                            currentAction = ActionType.PAY_PLAYER
                            showBottomSheet = true
                        }
                    )
                }
                item {
                    ActionCard(
                        title = "Operaciones con la Banca",
                        icon = Icons.Default.AccountBalance,
                        backgroundColorLight = ActionGreenBgLight,
                        iconColorLight = ActionGreenTextLight,
                        backgroundColorDark = ActionGreenBgDark,
                        iconColorDark = ActionGreenTextDark,
                        onClick = {
                            currentAction = ActionType.BANK_OPERATIONS
                            showBottomSheet = true
                        }
                    )
                }

                item {
                    Text(
                        text = "Propiedades",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                item {
                    ActionCard(
                        title = "Construir",
                        // Fallback to Home if AddHome is missing, but it should be in extended
                        icon = Icons.Default.AddHome,
                        backgroundColorLight = ActionYellowBgLight,
                        iconColorLight = ActionYellowTextLight,
                        backgroundColorDark = ActionYellowBgDark,
                        iconColorDark = ActionYellowTextDark,
                        onClick = {
                            currentAction = ActionType.BUILD
                            showBottomSheet = true
                        }
                    )
                }
                item {
                    ActionCard(
                        title = "Hipotecar",
                        icon = Icons.Default.Gavel,
                        backgroundColorLight = ActionRedBgLight,
                        iconColorLight = ActionRedTextLight,
                        backgroundColorDark = ActionRedBgDark,
                        iconColorDark = ActionRedTextDark,
                        onClick = {
                            currentAction = ActionType.MORTGAGE
                            showBottomSheet = true
                        }
                    )
                }
                item {
                    ActionCard(
                        title = "Deshipotecar",
                        icon = Icons.Default.RealEstateAgent,
                        backgroundColorLight = ActionPurpleBgLight,
                        iconColorLight = ActionPurpleTextLight,
                        backgroundColorDark = ActionPurpleBgDark,
                        iconColorDark = ActionPurpleTextDark,
                        onClick = {
                            currentAction = ActionType.UNMORTGAGE
                            showBottomSheet = true
                        }
                    )
                }

                // TODO: Implement Community Chest and Chance screens later
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Nota: Arca Comunal y Casualidad se implementarán próximamente.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                     Spacer(modifier = Modifier.height(80.dp)) // Bottom padding
                }
            }

            // Animation Overlay
            TransactionSuccessAnimation(
                visible = showSuccessAnimation,
                onAnimationFinished = {
                    showSuccessAnimation = false
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Content of Bottom Sheet
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 48.dp) // Padding for navigation bar area
                        .fillMaxWidth()
                ) {
                    when (currentAction) {
                        ActionType.PAY_PLAYER -> {
                            Text(
                                "Pagar a Jugador",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            PayPlayerForm(
                                onPayClicked = {
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                            showSuccessAnimation = true
                                        }
                                    }
                                }
                            )
                        }
                        ActionType.BANK_OPERATIONS -> {
                             PlaceholderActionContent("Operaciones con la Banca")
                        }
                        ActionType.BUILD -> {
                             PlaceholderActionContent("Construir Casa/Hotel")
                        }
                        ActionType.MORTGAGE -> {
                             PlaceholderActionContent("Hipotecar Propiedad")
                        }
                        ActionType.UNMORTGAGE -> {
                             PlaceholderActionContent("Deshipotecar Propiedad")
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    icon: ImageVector,
    backgroundColorLight: Color,
    iconColorLight: Color,
    backgroundColorDark: Color,
    iconColorDark: Color,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) backgroundColorDark else backgroundColorLight
    val iconColor = if (isDark) iconColorDark else iconColorLight
    val borderColor = if (isDark) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.outline

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PlaceholderActionContent(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Funcionalidad no implementada aún.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayPlayerForm(
    onPayClicked: () -> Unit
){
    var selectedPlayer by remember { mutableStateOf("Select Player") }
    var amount by remember { mutableStateOf("")}
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val players = listOf("Player 2", "Player 3", "The Banker") // Datos de ejemplo

    Column( // Changed Row to Column for better mobile layout in bottom sheet if needed, but original was Row.
            // The original PayPlayerForm had a Row with Dropdown and Amount. Let's keep it similar but adapted.
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedPlayer,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true)
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    players.forEach { player ->
                        DropdownMenuItem(
                            text = { Text(player) },
                            onClick = {
                                selectedPlayer = player
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.weight(0.7f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Boton principal
        Button(
            onClick = {
                Log.d("ActionsScreen", "Boton presionado: PAGAR A JUGADOR. Jugador: $selectedPlayer, Cantidad: $amount")
                onPayClicked()
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Pagar Jugador")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionsScreenPreview() {
    ActionsScreen()
}
