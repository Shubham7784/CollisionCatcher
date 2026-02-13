package com.collisioncatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.collisioncatcher.ui.navigation.AppNavHost
import com.collisioncatcher.ui.theme.CollisionCatcherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollisionCatcherTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}