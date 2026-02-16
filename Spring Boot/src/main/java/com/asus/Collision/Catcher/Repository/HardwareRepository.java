package com.asus.Collision.Catcher.Repository;

import com.asus.Collision.Catcher.Entity.Hardware;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HardwareRepository extends MongoRepository<Hardware, String> {
}
