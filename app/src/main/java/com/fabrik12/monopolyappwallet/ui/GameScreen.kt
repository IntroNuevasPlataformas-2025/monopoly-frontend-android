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
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.draw.drawBehind
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
val Slate200 = Color(0xFFE2E8F0)
val Slate400 = Color(0xFF94A3B8)
val Slate500 = Color(0xFF64748B)
val Slate700 = Color(0xFF334155)
val Slate800 = Color(0xFF1E293B)
val Slate900 = Color(0xFF0F172A)

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
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val cardBg = if (isDark) Slate800 else Slate100
    val labelColor = if (isDark) Slate400 else Slate500
    val amountColor = if (isDark) Color.White else Slate800

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardBg, shape = RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SALDO DISPONIBLE",
            color = labelColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$${String.format("%,d", balance)}",
            color = amountColor,
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 4.dp),
            letterSpacing = (-2).sp
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val textColor = if (isDark) Slate200 else Slate700

    Text(
        text = title,
        color = textColor,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun PropertyCard(property: PropertyUiModel) {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val cardBg = if (isDark) Slate800 else Slate100
    val titleColor = if (isDark) Color.White else Slate800
    val subtitleColor = if (isDark) Slate400 else Slate500
    val valueColor = if (property.isMortgaged) Color(0xFFEF4444) else Color(0xFF22C55E) // Red or Green

    val opacityModifier = if (property.isMortgaged) Modifier.alpha(0.6f) else Modifier

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(opacityModifier)
            .background(cardBg, shape = RoundedCornerShape(12.dp))
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
                    color = titleColor,
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
                        tint = subtitleColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = text,
                        color = subtitleColor,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Text(
            text = "$${property.value}",
            fontWeight = FontWeight.Bold,
            color = valueColor,
            fontSize = 16.sp
        )
    }
}

@Composable
fun TransactionCard(transaction: TransactionUiModel) {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val cardBg = if (isDark) Slate800 else Slate100
    val titleColor = if (isDark) Color.White else Slate800
    val subtitleColor = if (isDark) Slate400 else Slate500
    val amountColor = if (transaction.isPositive) Color(0xFF22C55E) else Color(0xFFEF4444)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardBg, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Circle
        // Dark mode backgrounds need higher transparency or different shades.
        // Tailwind reference: bg-blue-100 dark:bg-blue-900/50
        val (bgColor, iconColor, icon) = when (transaction.type) {
            TransactionType.PURCHASE -> Triple(
                if (isDark) Color(0x801E3A8A) else Color(0xFFDBEAFE),
                if (isDark) Color(0xFF60A5FA) else Color(0xFF3B82F6),
                Icons.Default.ShoppingCart
            )
            TransactionType.RENT -> Triple(
                if (isDark) Color(0x8014532D) else Color(0xFFDCFCE7),
                if (isDark) Color(0xFF4ADE80) else Color(0xFF22C55E),
                Icons.Default.Paid
            )
            TransactionType.CONSTRUCTION -> Triple(
                if (isDark) Color(0x80581C87) else Color(0xFFF3E8FF),
                if (isDark) Color(0xFFC084FC) else Color(0xFFA855F7),
                Icons.Default.Construction
            )
            TransactionType.EVENT -> Triple(
                if (isDark) Color(0x80713F12) else Color(0xFFFEF9C3),
                if (isDark) Color(0xFFFACC15) else Color(0xFFEAB308),
                Icons.Default.Celebration
            )
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
                color = titleColor,
                fontSize = 16.sp
            )
            Text(
                text = transaction.subtitle,
                color = subtitleColor,
                fontSize = 14.sp
            )
        }

        Text(
            text = "${if (transaction.isPositive) "+" else "-"}$${transaction.amount}",
            fontWeight = FontWeight.Bold,
            color = amountColor,
            fontSize = 16.sp
        )
    }
}

@Composable
fun BottomNavBar() {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val containerColor = if (isDark) Slate800 else Color.White
    val unselectedColor = if (isDark) Slate400 else Slate500
    val borderColor = if (isDark) Slate700 else Slate200

    NavigationBar(
        containerColor = containerColor,
        contentColor = unselectedColor,
        tonalElevation = 8.dp,
        modifier = Modifier.drawBehind {
             val strokeWidth = 1.dp.toPx()
             drawLine(
                 color = borderColor,
                 start = androidx.compose.ui.geometry.Offset(0f, 0f),
                 end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                 strokeWidth = strokeWidth
             )
        }
    ) {
        val items = listOf(
            Triple("Home", Icons.Default.Home, true),
            Triple("Propiedades", Icons.Default.Foundation, false),
            Triple("Acciones", Icons.Default.SwapHoriz, false),
            Triple("Settings", Icons.Default.Settings, false)
        )

        items.forEach { (label, icon, isSelected) ->
            NavigationBarItem(
                selected = isSelected,
                onClick = { },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryGreen,
                    selectedTextColor = PrimaryGreen,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor
                )
            )
        }
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
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) BackgroundDark else BackgroundLight

    Scaffold(
        bottomBar = { BottomNavBar() },
        containerColor = bgColor
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
