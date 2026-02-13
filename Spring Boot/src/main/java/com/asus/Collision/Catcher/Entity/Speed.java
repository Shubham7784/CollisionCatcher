package com.asus.Collision.Catcher.Entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Speed")
public class Speed {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String hardwareId;
    private String speed;
    private int timestamp;


    public int getTimestamp(){ return timestamp;}
    public void setTimestamp(int timestamp){
        this.timestamp = timestamp;
    }
    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }
}
