package com.asus.Collision.Catcher.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendSms(String message, String mobile) {
        String apiKey = "YOUR_FAST2SMS_API_KEY"; // Replace with actual key
        String url = "https://www.fast2sms.com/dev/bulkV2";

        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        Map<String, Object> body = new HashMap<>();
        body.put("sender_id", "TXTIND");
        body.put("message", message);
        body.put("language", "english");
        body.put("route", "v3");
        body.put("numbers", mobile);
        body.put("flash", 0);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getBody();
        } catch (IllegalArgumentException e) {
            return "Illegal argument: " + e.getMessage();
        } catch (Exception e) {
            return "Error occurred: " + e.getMessage();
        }
    }
}
