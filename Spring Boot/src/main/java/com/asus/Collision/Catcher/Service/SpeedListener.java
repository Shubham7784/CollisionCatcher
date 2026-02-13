package com.asus.Collision.Catcher.Service;

import com.asus.Collision.Catcher.Entity.Speed;
import com.asus.Collision.Catcher.Repository.SpeedRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SpeedListener {

    @Autowired
    private SpeedRepository speedRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handle(String message) throws Exception{
        try{
            Speed speedData = mapper.readValue(message, Speed.class);
            speedRepo.save(speedData);
        }
        catch (Exception e){
            log.error("Failed to process MQTT message");
            e.printStackTrace();
        }
    }

}
