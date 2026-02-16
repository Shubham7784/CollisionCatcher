package com.collisioncatcher.firebase

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.collisioncatcher.AlertActivity
import com.collisioncatcher.retrofit.instance.RetrofitService
import com.collisioncatcher.viewmodel.UserViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", token)
        // Send this token to your backend
        TokenManager.fcmToken = token
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM_SERVICE", "Received message: ${remoteMessage.data}")
        if (remoteMessage.data["type"] == "accident_alert") {
            showAccidentNotification()
        }
    }

    private fun showAccidentNotification() {

        val intent = Intent(this, AlertActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "accident_alert_channel"

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.stat_sys_warning)
            .setContentTitle("🚨 Accident Detected")
            .setContentText("Tap to respond within 60 seconds")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent, true) // POPUP
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            channelId,
            "Accident Alerts",
            NotificationManager.IMPORTANCE_HIGH
        )

        manager.createNotificationChannel(channel)
        manager.notify(1, notification)
    }
}
