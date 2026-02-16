package com.asus.Collision.Catcher.Service;

import com.asus.Collision.Catcher.Entity.Alert;
import com.asus.Collision.Catcher.Repository.AlertRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    public boolean deleteAlertByUserName(String userName){
        List<Alert> alerts = getAlerts();
        List<Alert> alertStream = alerts.stream().filter(x -> x.getUserName().equals(userName)).toList();
        boolean res = false;
        if(!alertStream.isEmpty()){
           res = deleteAlert(alertStream.getFirst());
        }
        return res;
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
