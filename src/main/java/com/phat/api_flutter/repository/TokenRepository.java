package com.phat.api_flutter.repository;

import com.phat.api_flutter.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    Optional<Token> findByAccessToken(String accessToken);
    Optional<Token> findByUserId(String userId);
void deleteByUserId(String userId);
}