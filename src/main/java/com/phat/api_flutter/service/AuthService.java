package com.phat.api_flutter.service;

import com.phat.api_flutter.models.Token;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.repository.TokenRepository;
import com.phat.api_flutter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String login(String email, String password) {
        User user = findByEmail(email).orElseThrow(() ->
                new RuntimeException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = generateToken(user);
        String refreshToken = generateRefreshToken(user);

        Token token = new Token();
        token.setUserId(user.getId());
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setCreatedAt(new Date());
        tokenRepository.save(token);

        return accessToken;
    }

    public boolean verifyToken(String token) {
        return tokenRepository.findByAccessToken(token).isPresent();
    }

    private String generateToken(User user) {
        return UUID.randomUUID().toString();
    }

    private String generateRefreshToken(User user) {
        return UUID.randomUUID().toString();
    }

    // Other methods for forgotPassword, resetPassword, etc.
}
