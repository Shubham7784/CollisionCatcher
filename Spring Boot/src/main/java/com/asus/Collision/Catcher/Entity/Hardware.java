package com.asus.Collision.Catcher.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Hardware")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hardware {

    @Id
    private String hardwareId;

    private String hardwareIp;
    private String userName;
}
