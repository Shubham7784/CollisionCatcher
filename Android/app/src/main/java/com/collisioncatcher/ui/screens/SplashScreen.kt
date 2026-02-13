package com.collisioncatcher.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.collisioncatcher.R
import com.collisioncatcher.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: UserViewModel = viewModel(),
    context: Context,
    onLoginSuccess: () -> Unit,
    onLoginFailure : ()-> Unit
) {
    LaunchedEffect(Unit) {
        val token = viewModel.getToken(context)
        if (!token.isNullOrEmpty()) {
            viewModel.isLoggedIn(token)
            delay(3000)
            if(viewModel.isSuccess.value)
            {
                onLoginSuccess()
            }
            else
            {
                onLoginFailure()
            }
        }
        else {
            viewModel.isLoading.value = true
            delay(3000)
            onLoginFailure()
            viewModel.isLoading.value = false
        }
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(R.drawable.logo), contentDescription = "App Logo",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        if(viewModel.isLoading.value){
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}