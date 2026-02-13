package com.asus.Collision.Catcher.Entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

@Document(collection = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private ObjectId userId;
    @NonNull
    private String name;
    @NonNull
    private String password;

    private String age;

    @NonNull
    private ArrayList<String> role = new ArrayList<>();

    private String userName;
    @NonNull
    private String phoneNo;

    @NonNull
    private Hardware hardware;

    @DBRef
    private ArrayList<Member> members = new ArrayList<>();
}
