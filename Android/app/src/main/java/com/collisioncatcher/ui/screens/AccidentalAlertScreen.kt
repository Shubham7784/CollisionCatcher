package com.collisioncatcher.ui.screens

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.collisioncatcher.viewmodel.HardwareViewModel
import com.collisioncatcher.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AccidentAlertScreen(context: Context, onFinish:()->Unit,userViewModel: UserViewModel = viewModel()) {

    var timeLeft by remember { mutableIntStateOf(60) }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }

        if (timeLeft == 0) {
            // Trigger emergency action (call backend / send SMS)
            userViewModel.sendEmergencyAlert(context)
            onFinish()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "🚨 ACCIDENT DETECTED",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Auto alert in $timeLeft seconds",
                fontSize = 20.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                onClick = {
                    //cancelAlertApiCall()
                    userViewModel.cancelAlert(context)
                    (context as Activity).finish()
                }
            ) {
                Text("Cancel Alert", color = Color.Red)
            }
        }
    }
}


