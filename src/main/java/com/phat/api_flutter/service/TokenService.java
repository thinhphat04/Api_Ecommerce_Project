package com.phat.api_flutter.service;

import com.phat.api_flutter.models.Token;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.repository.TokenRepository;
import com.phat.api_flutter.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

    @Value("${REFRESH_TOKEN_SECRET}")
    private String REFRESH_TOKEN_SECRET;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

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

//    public boolean verifyToken(String accessToken) {
//        Optional<Token> tokenOptional = tokenRepository.findByAccessToken(accessToken);
//        if (tokenOptional.isEmpty()) {
//            return false;
//        }
//
//        Token token = tokenOptional.get();
//        Claims claims;
//        try {
//            claims = Jwts.parser()
//                    .setSigningKey(REFRESH_TOKEN_SECRET)
//                    .parseClaimsJws(token.getRefreshToken())
//                    .getBody();
//        } catch (SignatureException e) {
//            return false;
//        }
//
//        Optional<User> userOptional = userRepository.findById(claims.getSubject());
//        return userOptional.isPresent();
//    }
    public boolean verifyToken(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            return false;
        }

        accessToken = accessToken.replace("Bearer", "").trim();
        Optional<Token> tokenOptional = tokenRepository.findByAccessToken(accessToken);
        if (tokenOptional.isEmpty()) {
            return false;
        }

        Token token = tokenOptional.get();
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(REFRESH_TOKEN_SECRET)
                    .parseClaimsJws(token.getRefreshToken())
                    .getBody();
        } catch (SignatureException e) {
            return false;
        }

        Optional<User> userOptional = userRepository.findById(claims.getSubject());
        return userOptional.isPresent();
    }
}