package com.asus.Collision.Catcher.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    public void sendAccidentAlert(String token) {

        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle("🚨 Accident Detected")
                        .setBody("Respond within 60 seconds")
                        .build())
                .putData("type", "accident_alert")
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Push sent: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
