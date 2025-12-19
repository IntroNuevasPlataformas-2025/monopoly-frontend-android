package com.fabrik12.monopolyappwallet.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabrik12.monopolyappwallet.ui.models.MockData
import com.fabrik12.monopolyappwallet.ui.models.PropertyUiModel
import com.fabrik12.monopolyappwallet.ui.models.TransactionType
import com.fabrik12.monopolyappwallet.ui.models.TransactionUiModel
import com.fabrik12.monopolyappwallet.viewmodel.GameViewModel

// --- Colors ---
val PrimaryGreen = Color(0xFF58CC02)
val BackgroundLight = Color(0xFFFFFFFF)
val BackgroundDark = Color(0xFF131E28)
val Slate100 = Color(0xFFF1F5F9)
val Slate500 = Color(0xFF64748B)
val Slate700 = Color(0xFF334155)
val Slate800 = Color(0xFF1E293B)
val TextDark = Color(0xFF1E293B)
val TextLight = Color(0xFFFFFFFF)

// --- Components ---

@Composable
fun GameHeader(
    playerName: String,
    cash: Int,
    propertyCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryGreen)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Player Info
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar Placeholder (Circle)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .background(Color.LightGray) // Fallback if image not loaded
            ) {
                 // Placeholder for Avatar Image
                 Icon(
                     imageVector = Icons.Default.Person,
                     contentDescription = "Avatar",
                     tint = Color.White,
                     modifier = Modifier.padding(4.dp)
                 )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = playerName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        // Stats
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Cash Small
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(
                    imageVector = Icons.Default.Payments,
                    contentDescription = "Cash",
                    tint = Color(0xFFFDE047), // Yellow-300
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = cash.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            // Properties Count
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Properties",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = propertyCount.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun BalanceCard(balance: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate100, shape = RoundedCornerShape(16.dp)) // Using Slate100 for light mode
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SALDO DISPONIBLE",
            color = Slate500,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$${String.format("%,d", balance)}",
            color = Slate800,
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 4.dp),
            letterSpacing = (-2).sp
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Slate700,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun PropertyCard(property: PropertyUiModel) {
    val opacityModifier = if (property.isMortgaged) Modifier.alpha(0.6f) else Modifier

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(opacityModifier)
            .background(Slate100, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Color Bar
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(40.dp)
                    .background(property.color, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = property.name,
                    fontWeight = FontWeight.Bold,
                    color = Slate800,
                    fontSize = 16.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                     val icon = when {
                         property.isMortgaged -> Icons.Default.NoAccounts
                         property.hotelCount > 0 -> Icons.Default.Hotel
                         else -> Icons.Default.Home
                     }
                    val text = when {
                        property.isMortgaged -> "Hipotecado"
                        property.hotelCount > 0 -> "${property.hotelCount} Hotel"
                        else -> "${property.houseCount} Casas"
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Slate500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = text,
                        color = Slate500,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Text(
            text = "$${property.value}",
            fontWeight = FontWeight.Bold,
            color = if (property.isMortgaged) Color(0xFFEF4444) else Color(0xFF22C55E), // Red or Green
            fontSize = 16.sp
        )
    }
}

@Composable
fun TransactionCard(transaction: TransactionUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate100, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Circle
        val (bgColor, iconColor, icon) = when (transaction.type) {
            TransactionType.PURCHASE -> Triple(Color(0xFFDBEAFE), Color(0xFF3B82F6), Icons.Default.ShoppingCart) // Blue
            TransactionType.RENT -> Triple(Color(0xFFDCFCE7), Color(0xFF22C55E), Icons.Default.Paid) // Green
            TransactionType.CONSTRUCTION -> Triple(Color(0xFFF3E8FF), Color(0xFFA855F7), Icons.Default.Construction) // Purple
            TransactionType.EVENT -> Triple(Color(0xFFFEF9C3), Color(0xFFEAB308), Icons.Default.Celebration) // Yellow
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(bgColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                fontWeight = FontWeight.Bold,
                color = Slate800,
                fontSize = 16.sp
            )
            Text(
                text = transaction.subtitle,
                color = Slate500,
                fontSize = 14.sp
            )
        }

        Text(
            text = "${if (transaction.isPositive) "+" else "-"}$${transaction.amount}",
            fontWeight = FontWeight.Bold,
            color = if (transaction.isPositive) Color(0xFF22C55E) else Color(0xFFEF4444),
            fontSize = 16.sp
        )
    }
}

@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Slate500,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                selectedTextColor = PrimaryGreen,
                indicatorColor = Color.Transparent,
                unselectedIconColor = Slate500,
                unselectedTextColor = Slate500
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Foundation, contentDescription = "Propiedades") },
            label = { Text("Propiedades", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                selectedTextColor = PrimaryGreen,
                indicatorColor = Color.Transparent,
                unselectedIconColor = Slate500,
                unselectedTextColor = Slate500
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.SwapHoriz, contentDescription = "Acciones") },
            label = { Text("Acciones", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                selectedTextColor = PrimaryGreen,
                indicatorColor = Color.Transparent,
                unselectedIconColor = Slate500,
                unselectedTextColor = Slate500
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                selectedTextColor = PrimaryGreen,
                indicatorColor = Color.Transparent,
                unselectedIconColor = Slate500,
                unselectedTextColor = Slate500
            )
        )
    }
}

@Composable
fun GameScreenContent(
    gameId: String?,
    currentBalance: Int?,
    properties: List<PropertyUiModel>,
    transactions: List<TransactionUiModel>,
    onStopGame: () -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            // Mocking name and count for now since it's not fully in VM yet
            GameHeader(
                playerName = "Jugador 1",
                cash = 1500,
                propertyCount = 4
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Balance Section
                item {
                    // Use VM balance if available, else Mock
                    val displayBalance = currentBalance ?: 1500
                    BalanceCard(balance = displayBalance)
                }

                // Properties Section
                item {
                    SectionHeader(title = "Mis Propiedades")
                }
                items(properties) { property ->
                    PropertyCard(property = property)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // History Section
                item {
                    SectionHeader(title = "Historial Reciente")
                }
                items(transactions) { transaction ->
                    TransactionCard(transaction = transaction)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Temporary Stop Game Button for functionality
                 item {
                     Spacer(modifier = Modifier.height(24.dp))
                     Button(
                         onClick = onStopGame,
                         modifier = Modifier.fillMaxWidth(),
                         colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                     ) {
                         Text("Terminar Partida (Stop Timer)")
                     }
                 }
            }
        }
    }
}

@Composable
fun GameScreen(
    gameId: String?,
    gameViewModel: GameViewModel = viewModel()
) {
    val context = LocalContext.current

    // VM State
    val balance by gameViewModel.currentBalance.collectAsState()

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
        currentBalance = balance,
        properties = MockData.properties, // Using Mock Data as requested for design focus
        transactions = MockData.transactions, // Using Mock Data as requested for design focus
        onStopGame = {
            gameViewModel.stopGameSession()
        }
    )
}

@Preview(showBackground = true, heightDp = 884)
@Composable
fun GameScreenPreview() {
    GameScreenContent(
        gameId = "preview-123",
        currentBalance = 1500,
        properties = MockData.properties,
        transactions = MockData.transactions,
        onStopGame = {}
    )
}
