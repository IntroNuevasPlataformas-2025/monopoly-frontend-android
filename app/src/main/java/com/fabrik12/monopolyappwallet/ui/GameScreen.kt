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
import com.fabrik12.monopolyappwallet.ui.theme.*

// --- Components ---

@Composable
fun GameHeader(
    playerName: String,
    cash: Int,
    propertyCount: Int
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.primary)
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
                    .background(MutedLight)
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
                color = colorScheme.onPrimary,
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
                    tint = Color(0xFFFDE047), // Amarillo
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = cash.toString(),
                    color = colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            // Properties Count
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Properties",
                    tint = colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = propertyCount.toString(),
                    color = colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun BalanceCard(balance: Int) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()
    val cardBg = colorScheme.surface
    val labelColor = if (isDark) MutedDark else MutedLight
    val amountColor = colorScheme.onSurface

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
    val colorScheme = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()
    val textColor = colorScheme.onSurface
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
    val colorScheme = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()
    val cardBg = colorScheme.surface
    val titleColor = colorScheme.onSurface
    val subtitleColor = if (isDark) MutedDark else MutedLight
    val valueColor = if (property.isMortgaged) ActionRedTextLight else ActionGreenTextLight
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
    val colorScheme = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()
    val cardBg = colorScheme.surface
    val titleColor = colorScheme.onSurface
    val subtitleColor = if (isDark) MutedDark else MutedLight
    val amountColor = if (transaction.isPositive) ActionGreenTextLight else ActionRedTextLight
    val (bgColor, iconColor, icon) = when (transaction.type) {
        TransactionType.PURCHASE -> Triple(
            if (isDark) ActionBlueBgDark else ActionBlueBgLight,
            if (isDark) ActionBlueTextDark else ActionBlueTextLight,
            Icons.Default.ShoppingCart
        )
        TransactionType.RENT -> Triple(
            if (isDark) ActionGreenBgDark else ActionGreenBgLight,
            if (isDark) ActionGreenTextDark else ActionGreenTextLight,
            Icons.Default.Paid
        )
        TransactionType.CONSTRUCTION -> Triple(
            if (isDark) ActionPurpleBgDark else ActionPurpleBgLight,
            if (isDark) ActionPurpleTextDark else ActionPurpleTextLight,
            Icons.Default.Construction
        )
        TransactionType.EVENT -> Triple(
            if (isDark) ActionYellowBgDark else ActionYellowBgLight,
            if (isDark) ActionYellowTextDark else ActionYellowTextLight,
            Icons.Default.Celebration
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardBg, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
    val colorScheme = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()
    val containerColor = colorScheme.surface
    val unselectedColor = if (isDark) MutedDark else MutedLight
    val borderColor = if (isDark) BorderDark else BorderLight
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
                    selectedIconColor = colorScheme.primary,
                    selectedTextColor = colorScheme.primary,
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
    val colorScheme = MaterialTheme.colorScheme
    val bgColor = colorScheme.background
    Scaffold(
        containerColor = bgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                item {
                    val displayBalance = currentBalance ?: 1500
                    BalanceCard(balance = displayBalance)
                }
                item {
                    SectionHeader(title = "Mis Propiedades")
                }
                items(properties) { property ->
                    PropertyCard(property = property)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    SectionHeader(title = "Historial Reciente")
                }
                items(transactions) { transaction ->
                    TransactionCard(transaction = transaction)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                 item {
                     Spacer(modifier = Modifier.height(24.dp))
                     Button(
                         onClick = onStopGame,
                         modifier = Modifier.fillMaxWidth(),
                         colors = ButtonDefaults.buttonColors(containerColor = colorScheme.error)
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
