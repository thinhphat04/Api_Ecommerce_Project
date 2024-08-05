package com.phat.api_flutter.service;

import com.phat.api_flutter.models.Token;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.repository.TokenRepository;
import com.phat.api_flutter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private TokenRepository tokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPasswordHash(userDetails.getPasswordHash());
        user.setPaymentCustomerId(userDetails.getPaymentCustomerId());
        user.setStreet(userDetails.getStreet());
        user.setApartment(userDetails.getApartment());
        user.setCity(userDetails.getCity());
        user.setPostalCode(userDetails.getPostalCode());
        user.setCountry(userDetails.getCountry());
        user.setPhone(userDetails.getPhone());
        user.setAdmin(userDetails.isAdmin());
        user.setResetPasswordOtp(userDetails.getResetPasswordOtp());
        user.setResetPasswordOtpExpires(userDetails.getResetPasswordOtpExpires());
        user.setCart(userDetails.getCart());


        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteToken(String userId) {
        tokenRepository.deleteById(userId);
    }

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public Token findTokenByUserId(String userId) {
        return tokenRepository.findByUserId(userId);
    }

    public Token findTokenByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken);
    }
}