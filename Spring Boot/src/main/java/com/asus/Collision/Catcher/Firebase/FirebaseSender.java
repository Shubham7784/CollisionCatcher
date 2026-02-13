package com.asus.Collision.Catcher.Firebase;

import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FirebaseSender {

    private static final String PROJECT_ID = "collision-catcher-c1796";
    private static final String MESSAGING_URL = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";

    private final GoogleCredentials credentials;
    private final ObjectMapper mapper = new ObjectMapper();

    public FirebaseSender(String serviceAccountPath) throws Exception {
        credentials = GoogleCredentials
                .fromStream(new FileInputStream(serviceAccountPath))
                .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));
        credentials.refreshIfExpired();
    }

    public void sendMessage(String token, String title, String body) throws Exception {
        credentials.refreshIfExpired();
        String accessToken = credentials.getAccessToken().getTokenValue();

        URL url = new URL(MESSAGING_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json; UTF-8");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        Map<String, Object> message = Map.of(
                "message", Map.of(
                        "token", token,
                        "notification", Map.of(
                                "title", title,
                                "body", body
                        )
                )
        );

        String json = mapper.writeValueAsString(message);
        connection.getOutputStream().write(json.getBytes());

        int responseCode = connection.getResponseCode();
        Scanner responseScanner;

        if (responseCode >= 200 && responseCode < 300) {
            responseScanner = new Scanner(connection.getInputStream());
            System.out.println("✅ Message sent successfully.");
        } else {
            responseScanner = new Scanner(connection.getErrorStream());
            System.err.println("❌ Error sending message. Response code: " + responseCode);
        }

        while (responseScanner.hasNextLine()) {
            System.out.println(responseScanner.nextLine());
        }
    }
}
