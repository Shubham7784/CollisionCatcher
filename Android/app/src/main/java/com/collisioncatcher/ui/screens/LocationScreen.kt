package com.collisioncatcher.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.unit.dp

@Composable
fun LocationScreen() {
    var currentLocation by remember { mutableStateOf("123 Main St, City") }
    var isTracking by remember { mutableStateOf(true) }
    var lastUpdate by remember { mutableStateOf("2 minutes ago") }
    var batteryLevel by remember { mutableStateOf(85) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Live Location Tracking",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Real-time vehicle location monitoring",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        item {
            // Location Status Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = if (isTracking) Color(0xFFE8F5E8) else Color(0xFFFFEBEE)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isTracking) Icons.Default.LocationOn else Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = if (isTracking) Color(0xFF4CAF50) else Color(0xFFD32F2F)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (isTracking) "Location Tracking Active" else "Tracking Disabled",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isTracking) Color(0xFF4CAF50) else Color(0xFFD32F2F)
                    )
                    Text(
                        text = "Last update: $lastUpdate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        item {
            // Map Placeholder
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color(0xFFE3F2FD),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF1976D2)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Interactive Map",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Tap to view full map",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        item {
            // Location Details
            Text(
                text = "Location Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        item {
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    LocationDetailItem(
                        label = "Current Address",
                        value = currentLocation,
                        icon = Icons.Default.LocationOn
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    LocationDetailItem(
                        label = "Coordinates",
                        value = "40.7128° N, 74.0060° W",
                        icon = Icons.Default.Settings
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    LocationDetailItem(
                        label = "Accuracy",
                        value = "±3 meters",
                        icon = Icons.Default.CheckCircle
                    )
                }
            }
        }
        
        item {
            // Tracking Controls
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { isTracking = !isTracking },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isTracking) "Stop Tracking" else "Start Tracking")
                }
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Share Location")
                }
            }
        }
        
        item {
            // Route History
            Text(
                text = "Recent Routes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        item {
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    RouteHistoryItem(
                        route = "Home → Office",
                        distance = "12.5 km",
                        duration = "25 min",
                        time = "Today, 8:30 AM"
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    RouteHistoryItem(
                        route = "Office → Mall",
                        distance = "8.2 km",
                        duration = "18 min",
                        time = "Yesterday, 6:15 PM"
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    RouteHistoryItem(
                        route = "Mall → Home",
                        distance = "15.8 km",
                        duration = "32 min",
                        time = "Yesterday, 8:45 PM"
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationDetailItem(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF1976D2)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun RouteHistoryItem(
    route: String,
    distance: String,
    duration: String,
    time: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF1976D2)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = route,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$distance • $duration",
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
