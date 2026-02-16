package com.collisioncatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.collisioncatcher.ui.screens.AccidentAlertScreen

class AlertActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setShowWhenLocked(true)
        setTurnScreenOn(true)

        setContent {
            AccidentAlertScreen(LocalContext.current, onFinish = {
                finish()
            })
        }
    }
}