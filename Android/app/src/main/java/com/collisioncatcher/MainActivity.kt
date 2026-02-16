package com.collisioncatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.collisioncatcher.ui.navigation.AppNavHost
import com.collisioncatcher.ui.theme.CollisionCatcherTheme
import com.collisioncatcher.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollisionCatcherTheme {
                val navController = rememberNavController()
                val userViewModel : UserViewModel = viewModel()
                AppNavHost(navController = navController)
            }
        }
    }
}