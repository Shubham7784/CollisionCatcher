package com.asus.Collision.Catcher.Repository;

import com.asus.Collision.Catcher.Entity.Member;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, ObjectId> {
    Member findByname(String name);
}
