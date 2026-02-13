package com.collisioncatcher.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.LaunchedEffect
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
import com.collisioncatcher.retrofit.entity.User
import com.collisioncatcher.viewmodel.UserViewModel

@Composable
fun ProfileScreen(viewModel: UserViewModel = viewModel(), context: Context) {

    var user by remember { mutableStateOf<User?>(null) }

    var totalTrips by remember { mutableIntStateOf(127) }
    var safetyScore by remember { mutableIntStateOf(95) }

    var pushNotiEnable by remember { mutableStateOf(true) }
    var locationEnable by remember { mutableStateOf(true) }
    var speedAlertEnable by remember { mutableStateOf(false) }

    // Call the suspend function inside LaunchedEffect
    LaunchedEffect(Unit) {
        val userDetails = viewModel.getUserDetails(context)
        if (userDetails != null) {
            user = userDetails
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Manage your account and preferences",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            // User Profile Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color(0xFFE3F2FD)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                color = Color(0xFF1976D2),
                                shape = RoundedCornerShape(40.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user?.name?.take(2)?.uppercase() ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = user?.name ?: "Loading...",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = user?.userName ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    if (user?.memberSince != null) {
                        Text(
                            text = "Member since ${user?.memberSince?.take(4)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        item {
            // User Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileStatCard(
                    title = "Total Trips",
                    value = "$totalTrips",
                    icon = Icons.Default.DirectionsCar,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.weight(1f)
                )
                ProfileStatCard(
                    title = "Safety Score",
                    value = "$safetyScore%",
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            // Personal Information
            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileInfoItem(
                        label = "Full Name",
                        value = user?.name ?: "",
                        icon = Icons.Default.Person
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProfileInfoItem(
                        label = "Email Address",
                        value = user?.userName ?: "",
                        icon = Icons.Default.Settings
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProfileInfoItem(
                        label = "Phone Number",
                        value = user?.phoneNo ?: "",
                        icon = Icons.Default.Phone
                    )
                }
            }
        }
        item {
            // App Settings
            Text(
                text = "App Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingItem(
                        title = "Push Notifications",
                        description = "Receive alerts and updates",
                        isEnabled = pushNotiEnable,
                        onCheckChange = {
                            pushNotiEnable = !pushNotiEnable
                        }
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    SettingItem(
                        title = "Location Services",
                        description = "Allow location tracking",
                        isEnabled = locationEnable,
                        onCheckChange = {
                            locationEnable = !locationEnable
                        }
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    SettingItem(
                        title = "Auto Speed Alerts",
                        description = "Get notified when speeding",
                        isEnabled = speedAlertEnable,
                        onCheckChange = {
                            speedAlertEnable = !speedAlertEnable
                        }
                    )
                }
            }
        }

        item {
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit Profile")
                }
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Settings")
                }
            }
        }

        item {
            // Additional Options
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileOptionItem(
                        title = "Help & Support",
                        icon = Icons.Default.Phone,
                        onClick = { }
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProfileOptionItem(
                        title = "Privacy Policy",
                        icon = Icons.Default.Security,
                        onClick = { }
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProfileOptionItem(
                        title = "Terms of Service",
                        icon = Icons.Default.Settings,
                        onClick = { }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileStatCard(
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
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(
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
private fun SettingItem(
    title: String,
    description: String,
    isEnabled: Boolean,
    onCheckChange :()->Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        Switch(
            checked = isEnabled,
            onCheckedChange = {
                onCheckChange()
            }
        )
    }
}

@Composable
private fun ProfileOptionItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF1976D2)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
