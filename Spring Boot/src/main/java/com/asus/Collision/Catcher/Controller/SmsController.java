package com.asus.Collision.Catcher.Controller;

import com.asus.Collision.Catcher.Service.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@RequestParam String to, @RequestParam String message) {
        String result = smsService.sendSms(message, to);
        return ResponseEntity.ok(result);
    }
}
