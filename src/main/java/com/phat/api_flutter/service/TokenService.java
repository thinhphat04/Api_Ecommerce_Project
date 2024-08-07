package com.phat.api_flutter.service;

import com.phat.api_flutter.models.Token;
import com.phat.api_flutter.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public Optional<Token> findByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken);
    }

    public Optional<Token> findByUserId(String userId) {
        return tokenRepository.findByUserId(userId);
    }

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public void deleteToken(String id) {
        tokenRepository.deleteById(id);
    }
}