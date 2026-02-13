package com.collisioncatcher.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetyTipsScreen(onBack: () -> Unit) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Safety Tips") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
    }) { inner ->
        Column(modifier = Modifier.padding(inner).padding(16.dp)) {
            DrivingSafetyCard(tips = mockTips)
        }
    }
}
data class SafetyTip(val title: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrivingSafetyCard(
    tips: List<SafetyTip>,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false
) {
    val expandedState = remember { mutableStateOf(initiallyExpanded) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = "Car icon",
                    modifier = Modifier.width(36.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Driving Safety Tips",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Quick reminders to keep you safe on the road",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(onClick = { expandedState.value = !expandedState.value }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = if (expandedState.value) "Collapse tips" else "Expand tips"
                    )
                }
            }

            if (expandedState.value) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                // List tips
                tips.forEach { tip ->
                    TipRow(tip = tip)
                }
            }
        }
    }
}

@Composable
private fun TipRow(tip: SafetyTip) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Icon(
            imageVector = Icons.Default.PhoneAndroid,
            contentDescription = "Tip icon",
            modifier = Modifier.width(28.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(text = tip.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = tip.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Mock data
private val mockTips = listOf(
    SafetyTip(
        title = "Always wear your seatbelt",
        description = "Seatbelts reduce the risk of death by 45% in a crash. Buckle up every trip, even short ones."
    ),
    SafetyTip(
        title = "Avoid distractions",
        description = "Keep your phone out of reach. Use Do Not Disturb while driving and set navigation before you start."
    ),
    SafetyTip(
        title = "Obey speed limits",
        description = "Drive according to posted limits and adjust speed for weather and traffic conditions."
    ),
    SafetyTip(
        title = "Maintain safe following distance",
        description = "Use the 3-second rule in good conditions; increase it in poor weather."
    ),
    SafetyTip(
        title = "Check mirrors and blind spots",
        description = "Always glance over your shoulder when changing lanes in addition to using mirrors."
    )
)
