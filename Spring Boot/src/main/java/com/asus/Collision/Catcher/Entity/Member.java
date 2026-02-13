package com.asus.Collision.Catcher.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

@Document(collection = "Member")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    private ObjectId memberId;

    @NonNull
    private String name;

    @NonNull
    private String phoneNo;

    private String relation;
}
