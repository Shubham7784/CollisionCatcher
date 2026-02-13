package com.collisioncatcher.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotScreen(onBack: () -> Unit) {
    Scaffold(topBar = { 
        TopAppBar(
            title = { Text("Forgot Password") }, 
            navigationIcon = { 
                IconButton(onClick = onBack) { 
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) 
                } 
            }
        ) 
    }) { inner ->
        Column(modifier = Modifier.padding(inner).padding(16.dp)) {
            var contact by remember { mutableStateOf("") }
            var otp by remember { mutableStateOf("") }
            var newPass by remember { mutableStateOf("") }
            var step by remember { mutableStateOf(1) }
            when (step) {
                1 -> {
                    Text("Enter registered Email or Phone", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("Email / Phone") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { step = 2 }, modifier = Modifier.fillMaxWidth()) { Text("Send OTP") }
                }
                2 -> {
                    Text("Verify OTP", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = otp, onValueChange = { otp = it }, label = { Text("OTP") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { step = 3 }, modifier = Modifier.fillMaxWidth()) { Text("Verify") }
                }
                else -> {
                    Text("Reset Password", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = newPass, onValueChange = { newPass = it }, label = { Text("New Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Update Password") }
                }
            }
        }
    }
}
