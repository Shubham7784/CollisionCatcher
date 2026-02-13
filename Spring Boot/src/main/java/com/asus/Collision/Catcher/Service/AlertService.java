package com.asus.Collision.Catcher.Service;

import com.asus.Collision.Catcher.Entity.Alert;
import com.asus.Collision.Catcher.Repository.AlertRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public Alert saveAlert(Alert alert)
    {
        return alertRepository.save(alert);
    }

    public boolean deleteAlert(Alert alert)
    {
        alertRepository.delete(alert);
        return true;
    }

    public List<Alert> getAlerts()
    {
        return alertRepository.findAll();
    }

    public Alert getAlertById(ObjectId alertId)
    {
        Optional<Alert> byId = alertRepository.findById(alertId);
        return byId.orElse(null);
    }
}
