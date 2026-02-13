package com.collisioncatcher.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.collisioncatcher.retrofit.entity.Hardware
import com.collisioncatcher.retrofit.entity.User
import com.collisioncatcher.viewmodel.UserViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onSignUpSuccess: () -> Unit,
    onBack: () -> Unit,
    context: Context,
    viewModel: UserViewModel = viewModel()
) {
    if(viewModel.isSuccess.value){
        Toast.makeText(context,viewModel.message.value,Toast.LENGTH_SHORT).show()
        onSignUpSuccess()
    }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Register") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
    }) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
        ) {
            var name by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }
            var hardwareNo by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
            var age by remember {mutableStateOf("")}

            Text(text = "Create Account", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = hardwareNo,
                onValueChange = { hardwareNo = it },
                label = { Text("Hardware Serial Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            Button(onClick = {
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val hardware = Hardware(serialNo = hardwareNo, userName = email)
                val user = User(
                    userId = null,
                    name = name,
                    password = password,
                    userName = email,
                    phoneNo = phone,
                    age = age,
                    hardware = hardware,
                    memberSince = LocalDateTime.now().toString(),
                    membersList = null
                )
                viewModel.signup(user) } ,
                modifier = Modifier.fillMaxWidth()
            )
            {
                if(viewModel.isLoading.value)
                {
                    CircularProgressIndicator(color =  Color.Blue,
                        modifier = Modifier.size(14.dp))
                }
                else if(viewModel.isFailure.value)
                {
                    Toast.makeText(context, viewModel.message.value, Toast.LENGTH_SHORT).show()
                    viewModel.isFailure.value = false
                }
                else
                {
                    Text(text = "Register")
                }
            }
        }
    }
}
