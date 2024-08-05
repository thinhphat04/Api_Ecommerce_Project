package com.phat.api_flutter.controllers;

import com.phat.api_flutter.models.Token;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            result.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User with that email already exists.");
        }

        User newUser = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. Check your email and try again.");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect password!");
        }

        String accessToken = jwtTokenUtil.generateToken(user.getId(), user.isAdmin());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), user.isAdmin());

        Token token = userService.findTokenByUserId(user.getId());
        if (token != null) {
            userService.deleteToken(user.getId());
        }

        token = new Token(user.getId(), accessToken, refreshToken);
        userService.saveToken(token);

        user.setPasswordHash(null); // Hide password hash
        return ResponseEntity.ok(new LoginResponse(user, accessToken));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> emailRequest) {
        String email = emailRequest.get("email");

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with that email does NOT exist!");
        }

        User user = userOpt.get();
        int otp = new Random().nextInt(8999) + 1000;
        user.setResetPasswordOtp(otp);
        user.setResetPasswordOtpExpires(new Date(System.currentTimeMillis() + 600000)); // 10 minutes

        userService.saveUser(user);

        mailSender.sendMail(email, "Password Reset OTP", "Your OTP for password reset is: " + otp);
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> otpRequest) {
        String email = otpRequest.get("email");
        int otp = Integer.parseInt(otpRequest.get("otp"));

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        User user = userOpt.get();
        if (user.getResetPasswordOtp() != otp || user.getResetPasswordOtpExpires().before(new Date())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
        }

        user.setResetPasswordOtp(1); // Mark OTP as confirmed
        user.setResetPasswordOtpExpires(null);

        userService.saveUser(user);
        return ResponseEntity.ok("OTP confirmed successfully.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> resetRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            result.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        String email = resetRequest.get("email");
        String newPassword = resetRequest.get("newPassword");

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        User user = userOpt.get();
        if (user.getResetPasswordOtp() != 1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Confirm OTP before resetting password.");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setResetPasswordOtp(null);

        userService.saveUser(user);
        return ResponseEntity.ok("Password reset successfully.");
    }
}

