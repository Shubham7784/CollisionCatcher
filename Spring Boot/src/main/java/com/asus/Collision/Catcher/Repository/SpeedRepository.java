package com.asus.Collision.Catcher.Repository;

import com.asus.Collision.Catcher.Entity.Speed;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpeedRepository extends MongoRepository<Speed, String> {
    List<Speed> findTop10ByHardwareIdOrderByTimestampDesc(String hardwareId);
}
