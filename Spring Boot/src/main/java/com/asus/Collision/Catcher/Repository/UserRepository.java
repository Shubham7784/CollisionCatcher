package com.asus.Collision.Catcher.Repository;

import com.asus.Collision.Catcher.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByuserName(String userName);
}
