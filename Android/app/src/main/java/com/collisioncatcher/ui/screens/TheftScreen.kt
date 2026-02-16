package com.collisioncatcher.ui.screens

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.collisioncatcher.retrofit.entity.User
import com.collisioncatcher.viewmodel.HardwareViewModel
import com.collisioncatcher.viewmodel.UserViewModel

@Composable
fun TheftScreen(hardwareViewModel: HardwareViewModel = viewModel(),userViewModel: UserViewModel = viewModel(),context : Context) {
    val user = remember { mutableStateOf<User?>(null) }
    val isArmed by hardwareViewModel.isVehicleArmed.collectAsState()
    var lastArmedTime by remember { mutableStateOf("2 hours ago") }
    var batteryLevel by remember { mutableIntStateOf(85) }
    var signalStrength by remember { mutableStateOf("Strong") }

    LaunchedEffect(Unit) {
        userViewModel.getUserDetails(context).let {
            user.value = it
        }
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Theft Protection",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Advanced vehicle security monitoring",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        item {
            // Security Status Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = if (isArmed) Color(0xFFE8F5E8) else Color(0xFFFFEBEE)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isArmed) Icons.Default.Security else Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = if (isArmed) Color(0xFF4CAF50) else Color(0xFFD32F2F)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (isArmed) "Security System ARMED" else "Security System DISARMED",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isArmed) Color(0xFF4CAF50) else Color(0xFFD32F2F)
                    )
                    Text(
                        text = "Last armed: $lastArmedTime",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        item {
            // Security Metrics
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SecurityMetricCard(
                    title = "Battery",
                    value = "$batteryLevel%",
                    icon = Icons.Default.Settings,
                    color = if (batteryLevel > 20) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                    modifier = Modifier.weight(1f)
                )
                SecurityMetricCard(
                    title = "Signal",
                    value = signalStrength,
                    icon = Icons.Default.LocationOn,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SecurityMetricCard(
                    title = "Status",
                    value = if (isArmed) "ACTIVE" else "INACTIVE",
                    icon = Icons.Default.Security,
                    color = if (isArmed) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            // Security Controls
            Text(
                text = "Security Controls",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        item {
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Arm/Disarm System",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (isArmed) "System is currently armed" else "System is currently disarmed",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isArmed,
                            onCheckedChange = {
                                user.value?.hardware?.hardwareId.let {
                                    if(!isArmed)
                                        hardwareViewModel.disableMotor(it!!)
                                    else
                                        hardwareViewModel.enableMotor(it!!)
                                }
                            }
                        )
                    }
                }
            }
        }
        
//        item {
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//                Button(
//                    onClick = { },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text("Panic Button")
//                }
//                Button(
//                    onClick = { },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text("Silent Alert")
//                }
//            }
//        }
        
//        item {
//            // Recent Alerts
//            Text(
//                text = "Recent Security Alerts",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
//
//        item {
//            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    SecurityAlertItem(
//                        title = "Motion Detected",
//                        description = "Vehicle movement detected in parking lot",
//                        time = "2 hours ago",
//                        severity = "HIGH"
//                    )
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//                    SecurityAlertItem(
//                        title = "System Armed",
//                        description = "Security system activated successfully",
//                        time = "3 hours ago",
//                        severity = "LOW"
//                    )
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//                    SecurityAlertItem(
//                        title = "Battery Low",
//                        description = "Device battery level below 20%",
//                        time = "1 day ago",
//                        severity = "MEDIUM"
//                    )
//                }
//            }
//        }
        
//        item {
//            // Security Features
//            Text(
//                text = "Security Features",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
//
//        item {
//            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    SecurityFeatureItem(
//                        title = "Motion Detection",
//                        description = "Detects unauthorized vehicle movement",
//                        isEnabled = true
//                    )
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//                    SecurityFeatureItem(
//                        title = "GPS Tracking",
//                        description = "Real-time location monitoring",
//                        isEnabled = true
//                    )
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//                    SecurityFeatureItem(
//                        title = "Remote Lock",
//                        description = "Remotely lock/unlock vehicle",
//                        isEnabled = false
//                    )
//                }
//            }
//        }
    }
}

@Composable
private fun SecurityMetricCard(
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
private fun SecurityAlertItem(
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
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = severity,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = when (severity) {
                    "HIGH" -> Color(0xFFD32F2F)
                    "MEDIUM" -> Color(0xFFFF9800)
                    else -> Color(0xFF4CAF50)
                }
            )
        }
    }
}

@Composable
private fun SecurityFeatureItem(
    title: String,
    description: String,
    isEnabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = if (isEnabled) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
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
            text = if (isEnabled) "ON" else "OFF",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = if (isEnabled) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
        )
    }
}
