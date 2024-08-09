package com.phat.api_flutter.repository;

import com.phat.api_flutter.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
   User findUserByEmail(String email);
   User findUserByName(String name);
}