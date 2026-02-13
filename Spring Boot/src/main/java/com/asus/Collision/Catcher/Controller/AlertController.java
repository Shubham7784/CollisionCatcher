package com.asus.Collision.Catcher.Controller;

import com.asus.Collision.Catcher.Entity.Alert;
import com.asus.Collision.Catcher.Service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping
    public ResponseEntity<?> getAlerts()
    {
        List<Alert> alerts = alertService.getAlerts();
        if(!alerts.isEmpty())
            return new ResponseEntity<>(alerts, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAlertByUserName(@PathVariable String userName)
    {
        List<Alert> alertList = alertService.getAlerts().stream().filter(x -> x.getUserName().equals(userName)).toList();
        if(!alertList.isEmpty())
            return new ResponseEntity<>(alertList,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping
    public ResponseEntity<?> saveAlert(@RequestBody Alert alert)
    {
        alert.setDateTime(LocalDateTime.now());
        Alert alert1 = alertService.saveAlert(alert);
        return new ResponseEntity<>(alert1,HttpStatus.CREATED);
    }
}
