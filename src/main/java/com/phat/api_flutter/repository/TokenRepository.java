package com.phat.api_flutter.repository;

import com.phat.api_flutter.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {
    Optional<Token> findByAccessToken(String accessToken);
    Optional<Token> findByUserId(String userId);
}