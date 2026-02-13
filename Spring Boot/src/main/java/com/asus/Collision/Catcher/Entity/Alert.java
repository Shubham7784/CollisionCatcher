package com.asus.Collision.Catcher.Entity;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Data
@Document("Alert")
public class Alert {

    @Id
    private ObjectId alertId;

    @NonNull
    private String hardwareId;

    private String userName;

    @NonNull
    private String status;
    @NonNull
    private String latitude;
    @NonNull
    private String longitude;
    @NonNull
    private LocalDateTime dateTime = LocalDateTime.now();
}
