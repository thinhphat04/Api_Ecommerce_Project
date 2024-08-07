package com.phat.api_flutter.controllers;

import com.phat.api_flutter.dto.AuthenticationRequest;
import com.phat.api_flutter.dto.AuthenticationResponse;
import com.phat.api_flutter.dto.PasswordResetRequest;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.models.Token;
import com.phat.api_flutter.service.UserService;
import com.phat.api_flutter.service.TokenService;
import com.phat.api_flutter.service.EmailService;
import com.phat.api_flutter.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authenticationRequest) {
        Optional<User> existingUser = userService.findByEmail(authenticationRequest.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(409).body("Email already exists");
        }

        User user = new User();
        user.setEmail(authenticationRequest.getUsername());
        user.setPasswordHash(passwordEncoder.encode(authenticationRequest.getPassword()));
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("Login attempt with email: " + authenticationRequest.getUsername());

        Optional<User> existingUser = userService.findByEmail(authenticationRequest.getUsername());
        if (existingUser.isPresent()) {
            logger.info("User found with email: " + authenticationRequest.getUsername());
            boolean passwordMatches = passwordEncoder.matches(authenticationRequest.getPassword(), existingUser.get().getPasswordHash());
            if (passwordMatches) {
                logger.info("Password matches for user: " + authenticationRequest.getUsername());

                UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(existingUser.get().getEmail())
                        .password(existingUser.get().getPasswordHash())
                        .authorities(new ArrayList<>())
                        .build();
                String accessToken = jwtUtil.generateToken(userDetails);
                String refreshToken = jwtUtil.generateRefreshToken(userDetails);
                Token token = new Token();
                token.setUserId(existingUser.get().getId());
                token.setAccessToken(accessToken);
                token.setRefreshToken(refreshToken);
                tokenService.saveToken(token);
                return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
            } else {
                logger.warning("Password does not match for user: " + authenticationRequest.getUsername());
            }
        } else {
            logger.warning("No user found with email: " + authenticationRequest.getUsername());
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        boolean isValid = jwtUtil.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        Optional<User> user = userService.findByEmail(email);
        if (!user.isPresent()) {
            return ResponseEntity.status(404).body("User not found");
        }

        int otp = (int) (Math.random() * 9000) + 1000;
        user.get().setResetPasswordOtp(otp);
        user.get().setResetPasswordOtpExpires(new Date(System.currentTimeMillis() + 600000));
        userService.saveUser(user.get());

        emailService.sendMail(email, "Password Reset OTP", "Your OTP is: " + otp, "OTP sent to your email", "Error sending OTP email");

        return ResponseEntity.ok("OTP sent to your email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody PasswordResetRequest request) {
        Optional<User> user = userService.findByEmail(request.getEmail());
        if (!user.isPresent() || user.get().getResetPasswordOtpExpires().getTime() < System.currentTimeMillis() || user.get().getResetPasswordOtp() != request.getOtp()) {
            return ResponseEntity.status(401).body("Invalid or expired OTP");
        }

        user.get().setResetPasswordOtp(null);
        user.get().setResetPasswordOtpExpires(null);
        userService.saveUser(user.get());

        return ResponseEntity.ok("OTP verified successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        Optional<User> user = userService.findByEmail(request.getEmail());
        if (!user.isPresent() || user.get().getResetPasswordOtp() != null) {
            return ResponseEntity.status(401).body("OTP verification required before resetting password");
        }

        user.get().setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userService.saveUser(user.get());

        return ResponseEntity.ok("Password reset successfully");
    }
}