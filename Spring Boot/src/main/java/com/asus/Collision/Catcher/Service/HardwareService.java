package com.asus.Collision.Catcher.Service;

import com.asus.Collision.Catcher.Entity.Hardware;
import com.asus.Collision.Catcher.Repository.HardwareRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HardwareService {

    @Autowired
    private HardwareRepository hardwareRepository;

    @Autowired
    private UserService userService;

    public Hardware saveHardware(Hardware hardware)
    {
        return hardwareRepository.save(hardware);
    }

    /*public Hardware getHardwareById(String hardwareId)
    {
        Hardware byId = hardwareRepository.findById();
        return byId;
    }*/
}
