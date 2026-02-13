package com.asus.Collision.Catcher.Repository;

import com.asus.Collision.Catcher.Entity.Alert;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlertRepository extends MongoRepository<Alert, ObjectId> {
}
