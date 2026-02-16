package com.collisioncatcher.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.collisioncatcher.viewmodel.HardwareViewModel
import com.collisioncatcher.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

data class TabItem(val title: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    onLogout: () -> Unit,
    onOpenContacts: () -> Unit,
    onOpenTips: () -> Unit,
    userViewModel: UserViewModel = viewModel(),
    context: Context
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        TabItem("Home", Icons.Default.Home),
        TabItem("Speed", Icons.Default.Speed),
        TabItem("Location", Icons.Default.LocationOn),
        TabItem("Theft", Icons.Default.Security),
        TabItem("Profile", Icons.Default.Person)
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collision Catcher") },
                actions = {
                    Button(onClick = {
                        UserViewModel().logout(context)
                        onLogout()
                    }) { Text("Logout") }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { inner ->
        Column(modifier = Modifier
            .padding(inner)
            .padding(16.dp)) {
            when (selectedTab) {
                0 -> HomeDashboard(onOpenContacts = onOpenContacts, onOpenTips = onOpenTips)
                1 -> SpeedScreen(context = context)
                2 -> LocationScreen()
                3 -> TheftScreen(userViewModel = userViewModel, context = context)
                else -> ProfileScreen(viewModel = userViewModel, context = context)
            }
        }
    }
}

@Composable
fun HomeDashboard(
    onOpenContacts: () -> Unit,
    onOpenTips: () -> Unit,
    hardwareViewModel: HardwareViewModel = viewModel()
) {
    val currentSpeed by hardwareViewModel.speed.collectAsState()
    var isMonitoring by remember { mutableStateOf(true) }
    var collisionRisk by remember { mutableStateOf("LOW") }
    var lastUpdate by remember { mutableStateOf("2 min ago") }

    // Simulate real-time data updates
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            collisionRisk = when {
                currentSpeed > 27.7 -> "HIGH"
                currentSpeed > 22.2 -> "MEDIUM"
                else -> "LOW"
            }
            lastUpdate = "Just now"
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Welcome Header
            Text(
                text = "Welcome to Collision Catcher",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Your vehicle safety companion",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            // Main Status Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = when (collisionRisk) {
                        "HIGH" -> Color(0xFFFFEBEE)
                        "MEDIUM" -> Color(0xFFFFF3E0)
                        else -> Color(0xFFE8F5E8)
                    }
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 140.dp)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            when (collisionRisk) {
                                "HIGH" -> Icons.Default.Warning
                                "MEDIUM" -> Icons.Default.Warning
                                else -> Icons.Default.CheckCircle
                            },
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = when (collisionRisk) {
                                "HIGH" -> Color(0xFFD32F2F)
                                "MEDIUM" -> Color(0xFFFF9800)
                                else -> Color(0xFF4CAF50)
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Collision Risk: $collisionRisk",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = when (collisionRisk) {
                                "HIGH" -> Color(0xFFD32F2F)
                                "MEDIUM" -> Color(0xFFFF9800)
                                else -> Color(0xFF4CAF50)
                            }
                        )
                        Text(
                            text = "Last updated: $lastUpdate",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            // Real-time Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModernMetricCard(
                    title = "Current Speed",
                    value = "${currentSpeed.toString().substring(0, 3)} km/h",
                    icon = Icons.Default.Speed,
                    color = Color(0xFF1E3A8A),
                    modifier = Modifier.weight(1f)
                )
                ModernMetricCard(
                    title = "Monitoring",
                    value = if (isMonitoring) "ACTIVE" else "INACTIVE",
                    icon = Icons.Default.Security,
                    color = if (isMonitoring) Color(0xFF059669) else Color(0xFFDC2626),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModernMetricCard(
                    title = "Location",
                    value = "TRACKING",
                    icon = Icons.Default.LocationOn,
                    color = Color(0xFF0EA5E9),
                    modifier = Modifier.weight(1f)
                )
                ModernMetricCard(
                    title = "Battery",
                    value = "85%",
                    icon = Icons.Default.Settings,
                    color = Color(0xFF7C3AED),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            // Quick Actions
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    text = "Emergency\nContacts",
                    icon = Icons.Default.Phone,
                    color = Color(0xFFDC2626),
                    onClick = onOpenContacts,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Safety\nTips",
                    icon = Icons.Default.DirectionsCar,
                    color = Color(0xFF059669),
                    onClick = onOpenTips,
                    modifier = Modifier.weight(1f)
                )
            }
        }

//        item {
//            // Recent Alerts
//            Text(
//                text = "Recent Alerts",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
//
//        item {
//            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    AlertItem(
//                        title = "Speed Alert",
//                        description = "Exceeded speed limit by 15 km/h",
//                        time = "5 minutes ago",
//                        severity = "MEDIUM"
//                    )
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//                    AlertItem(
//                        title = "Location Update",
//                        description = "Vehicle location updated successfully",
//                        time = "12 minutes ago",
//                        severity = "LOW"
//                    )
//                }
//            }
//        }
    }
}

@Composable
private fun ModernMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = color
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = color,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = color
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = color,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun AlertItem(
    title: String,
    description: String,
    time: String,
    severity: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (severity) {
                "HIGH" -> Icons.Default.Warning
                "MEDIUM" -> Icons.Default.Warning
                else -> Icons.Default.CheckCircle
            },
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = when (severity) {
                "HIGH" -> Color(0xFFD32F2F)
                "MEDIUM" -> Color(0xFFFF9800)
                else -> Color(0xFF4CAF50)
            }
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
