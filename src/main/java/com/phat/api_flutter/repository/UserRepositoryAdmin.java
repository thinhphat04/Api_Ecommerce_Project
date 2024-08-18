package com.phat.api_flutter.repository;

import com.phat.api_flutter.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepositoryAdmin extends MongoRepository<User, String> {
    User findUserById(String id);
}
