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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.RealEstateAgent // Requires extended icons
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabrik12.monopolyappwallet.data.models.Player
import com.fabrik12.monopolyappwallet.ui.models.PropertyUiModel
import com.fabrik12.monopolyappwallet.ui.theme.*
import com.fabrik12.monopolyappwallet.viewmodel.GameViewModel
import com.fabrik12.monopolyappwallet.viewmodel.PropertyUI
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

// Enum to identify which action is selected
enum class ActionType {
    PAY_PLAYER,
    BUY_PROPERTY,
    BUILD,
    MORTGAGE,
    UNMORTGAGE,
    NONE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsScreen(
    gameViewModel: GameViewModel
) {
    Log.d("ViernesDebug", "MainScreen - VM Hash: ${System.identityHashCode(gameViewModel)}")

    var showBottomSheet by remember { mutableStateOf(false) }
    var currentAction by remember { mutableStateOf(ActionType.NONE) }
    var showSuccessAnimation by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // Observar eventos para mostrar animacion cuando recibimos success
    LaunchedEffect(Unit) {
        gameViewModel.uiEvents.collectLatest { ev ->
            if (ev.contains("SUCESS", ignoreCase = true) || ev.contains("EXITO", ignoreCase = true)) {
                showSuccessAnimation = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Panel de Acciones") })
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
                // Seccion de dinero
                item { CabeceraSeccion("Finanzas") }

                // NUEVO: Tarjeta de Identidad
                item {
                    val name by gameViewModel.currentPlayerName.collectAsState()
                    val balance by gameViewModel.currentBalance.collectAsState()

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Jugador: $name", style = MaterialTheme.typography.titleMedium)
                            Text("Saldo: $${balance ?: "---"}", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }

                item {
                    ActionCard(
                        title = "Pagar a Jugador",
                        icon = Icons.Default.Groups,
                        colors = ActionColors(
                            ActionBlueBgLight,
                            ActionBlueTextLight,
                            ActionBlueBgDark,
                            ActionBlueTextDark
                        ),
                        onClick = {
                            currentAction = ActionType.PAY_PLAYER
                            showBottomSheet = true
                        }
                    )
                }

                item {
                    ActionCard(
                        title = "Comprar Propiedades", // Reemplaza a "Operaciones con Banca"
                        icon = Icons.Default.Storefront, // Icono de tienda
                        colors = ActionColors(
                            ActionGreenBgLight,
                            ActionGreenTextLight,
                            ActionGreenBgDark,
                            ActionGreenTextDark
                        ),
                        onClick = {
                            currentAction = ActionType.BUY_PROPERTY
                            showBottomSheet = true
                        }
                    )
                }

                // SECCION DE GESTION
                item { CabeceraSeccion("Mis Propiedades") }

                item {
                    ActionCard(
                        title = "Construir (Casas/Hoteles)",
                        icon = Icons.Default.AddHome,
                        colors = ActionColors(
                            ActionYellowBgLight,
                            ActionYellowTextLight,
                            ActionYellowBgDark,
                            ActionYellowTextDark
                        ),
                        onClick = {
                            currentAction = ActionType.BUILD
                            showBottomSheet = true
                        }
                    )
                }

                // Botones Hipotecar/Deshipotecar (Placeholders por ahora)
                item {
                    ActionCard(
                        title = "Hipotecar",
                        icon = Icons.Default.Gavel,
                        colors = ActionColors(
                            ActionRedBgLight,
                            ActionRedTextLight,
                            ActionRedBgDark,
                            ActionRedTextDark
                        ),
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
                        colors = ActionColors(
                            ActionPurpleBgLight,
                            ActionPurpleTextLight,
                            ActionPurpleBgDark,
                            ActionPurpleTextDark
                        ),
                        onClick = {
                            currentAction = ActionType.UNMORTGAGE
                            showBottomSheet = true
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }

            // Animación de éxito superpuesta
            if (showSuccessAnimation) {
                TransactionSuccessAnimation(
                    visible = showSuccessAnimation,
                    onAnimationFinished = { showSuccessAnimation = false },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
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
                            SheetHeader("Realizar Pago")
                            PayPlayerForm(
                                playersFlow = gameViewModel.players,
                                onPayClicked = { targetId, amount ->
                                    gameViewModel.sendPayment(amount, targetId)
                                    scope.launch { sheetState.hide() }.invokeOnCompletion { showBottomSheet = false }
                                }
                            )
                        }
                        ActionType.BUY_PROPERTY -> {
                            SheetHeader("Comprar Propiedad")
                            BuyPropertyForm(
                                availablePropertiesFlow = gameViewModel.availablePropertiesUI,
                                onBuyClicked = { propertyId ->
                                    gameViewModel.requestBuyProperty(propertyId)
                                    scope.launch { sheetState.hide() }.invokeOnCompletion { showBottomSheet = false }
                                }
                            )
                        }
                        ActionType.BUILD -> {
                            SheetHeader("Construir Mejoras")
                            BuildHouseForm(
                                myPropertiesFlow = gameViewModel.myPropertiesUi, // Solo MIS propiedades
                                onBuildClicked = { propertyId ->
                                    gameViewModel.requestBuildHouse(propertyId)
                                    scope.launch { sheetState.hide() }.invokeOnCompletion { showBottomSheet = false }
                                }
                            )
                        }
                        else -> PlaceholderActionContent("En construcción...")
                    }
                }
            }
        }
    }
}

// --- COMPONENTES AUXILIARES ---

@Composable
fun CabeceraSeccion(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun SheetHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

// Clase auxiliar para pasar colores más limpio
data class ActionColors(val bgLight: Color, val textLight: Color, val bgDark: Color, val textDark: Color)

@Composable
fun ActionCard(title: String, icon: ImageVector, colors: ActionColors, onClick: () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val bg = if (isDark) colors.bgDark else colors.bgLight
    val textC = if (isDark) colors.textDark else colors.textLight

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)) // Fondo sutil
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = textC, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
    }
}

// --- FORMULARIOS ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayPlayerForm(
    playersFlow: StateFlow<List<Player>>,
    onPayClicked: (String?, Int) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var selectedPlayerId by remember { mutableStateOf<String?>(null) }

    // Lógica del Dropdown simplificada
    val players by playersFlow.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val selectedName = players.find { it.id == selectedPlayerId }?.name ?: "Seleccionar Jugador"

    Column {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selectedName,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                players.forEach { p ->
                    DropdownMenuItem(
                        text = { Text(p.name) },
                        onClick = { selectedPlayerId = p.id; expanded = false }
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = amount,
            onValueChange = { if (it.all { char -> char.isDigit() }) amount = it },
            label = { Text("Monto ($)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onPayClicked(selectedPlayerId, amount.toIntOrNull() ?: 0) },
            modifier = Modifier.fillMaxWidth(),
            enabled = amount.isNotEmpty()
        ) { Text("Enviar Pago") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyPropertyForm(
    availablePropertiesFlow: StateFlow<List<PropertyUI>>,
    onBuyClicked: (String) -> Unit
) {
    val available by availablePropertiesFlow.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    var selectedProp by remember { mutableStateOf<PropertyUI?>(null) }
    Column {
        Text("Selecciona una propiedad disponible para enviar la solicitud de compra al banquero.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                // Mostramos el NOMBRE BONITO, o el placeholder
                value = selectedProp?.let { "${it.name} ($${it.price})" } ?: "Seleccionar Propiedad",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                if (available.isEmpty()) {
                    DropdownMenuItem(text = { Text("No hay propiedades disponibles") }, onClick = {})
                }
                available.forEach { prop ->
                    DropdownMenuItem(
                        // Mostramos Nombre y Precio en la lista
                        text = { Text("${prop.name} - $${prop.price}") },
                        onClick = {
                            selectedProp = prop // Guardamos todo el objeto
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            // Al hacer click, enviamos el ID técnico
            onClick = { selectedProp?.let { onBuyClicked(it.id) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedProp != null
        ) { Text("Solicitar Compra") }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildHouseForm(
    myPropertiesFlow: StateFlow<List<PropertyUiModel>>,
    onBuildClicked: (String) -> Unit
) {
    val myProps by myPropertiesFlow.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedProp by remember { mutableStateOf<PropertyUiModel?>(null) }

    Column {
        Text("Selecciona una de TUS propiedades para añadir una casa.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selectedProp?.let { "${it.name} ($${it.price})" } ?: "Mis Propiedades",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                if (myProps.isEmpty()) {
                    DropdownMenuItem(text = { Text("No tienes propiedades aún") }, onClick = {})
                }
                myProps.forEach { prop ->
                    DropdownMenuItem(
                        text = { Text("${prop.name} - $${prop.price}") },
                        onClick = {
                            selectedProp = prop //Guardamos todo el objeto
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { selectedProp?.let { onBuildClicked(it.id) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedProp != null
        ) { Text("Solicitar Construcción") }
    }
}

@Composable
fun PlaceholderActionContent(msg: String) {
    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
        Text(msg, color = Color.Gray)
    }
}
