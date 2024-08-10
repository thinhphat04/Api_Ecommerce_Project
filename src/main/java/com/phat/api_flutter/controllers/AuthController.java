package com.phat.api_flutter.controllers;

import com.phat.api_flutter.dto.*;
import com.phat.api_flutter.models.Token;
import com.phat.api_flutter.repository.TokenRepository;
import com.phat.api_flutter.service.AuthService;
import com.phat.api_flutter.service.JwtService;
import com.phat.api_flutter.models.CustomUser;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.service.PasswordResetService;
import com.phat.api_flutter.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private UserService userService;
    private TokenRepository tokenRepository;
    private AuthService authService;
    AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private PasswordResetService passwordResetService;


    @Value("${ACCESS_TOKEN_SECRET}")
    private String ACCESS_TOKEN_SECRET;

    @Value("${REFRESH_TOKEN_SECRET}")
    private String REFRESH_TOKEN_SECRET;

    @Autowired
    public AuthController(UserService userService, TokenRepository tokenRepository, AuthService authService, AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder, JwtService jwtService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.passwordResetService = passwordResetService;
    }

//    @PostMapping("/login")
//    public ResponseEntity authenticateUser(@Valid @RequestBody AuthenticationRequest loginRequest) {
//        try {
//            String email = loginRequest.getEmail();
//            String password = loginRequest.getPassword();
//
//            User userOptional = authService.loadUserByEmail(email);
//            if (userOptional == null) {
//                return ResponseEntity.status(404).body("User not found! Check your email and try again.");
//            }
//
//            User user = userOptional;
//            if (passwordEncoder.matches(password, user.getPasswordHash())) {
//                String accessToken = Jwts.builder()
//                        .setSubject(user.getName())
//                        .claim("ROLE", user.getRoles())
//                        .setIssuedAt(new Date())
//                        .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
//                        .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET)
//                        .compact();
//
//                String refreshToken = Jwts.builder()
//                        .setSubject(user.getName())
//                        .claim("ROLE", user.getRoles())
//                        .setIssuedAt(new Date())
//                        .setExpiration(new Date(System.currentTimeMillis() + 5184000000L)) // 60 days
//                        .signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_SECRET)
//                        .compact();
//
//                tokenRepository.deleteByUserId(user.getName());
//                tokenRepository.save(new Token(user.getName(), accessToken, refreshToken));
//
//                user.setPasswordHash(null); // Exclude the password hash
//                return ResponseEntity.ok(new AuthenticationResponse(user, accessToken, refreshToken));
//            }
//
//            return ResponseEntity.status(400).body("Incorrect password!");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found!\nCheck your email and try again");
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("isAdmin", user.isAdmin());

            String accessToken = jwtService.generateToken(user.getId().toString());
            String refreshToken = jwtService.generateToken(user.getId().toString());

            Optional<Token> tokenOptional = tokenRepository.findByUserId(user.getId());
            tokenOptional.ifPresent(tokenRepository::delete);

            Token newToken = new Token(user.getId(), accessToken, refreshToken);
            tokenRepository.save(newToken);

            user.setPasswordHash(null);  // Remove password before sending response
            Map<String, Object> response = new HashMap<>();
            response.put("_id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("phone", user.getPhone());
            response.put("isAdmin", user.isAdmin());
            response.put("wishlist", user.getWishlist());
            response.put("resetPasswordOtp", user.getResetPasswordOtp());
            response.put("resetPasswordOtpExpires", user.getResetPasswordOtpExpires());
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Incorrect password!");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest authenticationRequest) {
        User existingUser = userService.findByEmail(authenticationRequest.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(409).body(null);
        }

        User user = new User();
        user.setName(authenticationRequest.getName());
        user.setEmail(authenticationRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(authenticationRequest.getPassword()));
        user.setPhone(authenticationRequest.getPhone());
        user.setAdmin(false);
        user.setCart(new ArrayList<>());
        user.setWishlist(new ArrayList<>());

        User savedUser = authService.addUser(user);
        AuthenticationResponse response = new AuthenticationResponse(savedUser);
        return ResponseEntity.ok(response);
    }

    // Api /api/random yêu cầu phải xác thực mới có thể request
    @GetMapping("/test")
    public String randomStuff() {
        return "JWT Hợp lệ mới có thể thấy được message này";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequestDTO forgotPasswordRequest) {
        passwordResetService.forgotPassword(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Void> verifyOtp(@RequestBody VerifyOtpRequestDTO verifyOtpRequest) {
        boolean isValid = passwordResetService.verifyOtp(verifyOtpRequest.getEmail(), verifyOtpRequest.getOtp());
        if (isValid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequest) {
        passwordResetService.resetPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/verify-token")
    public ResponseEntity<Map<String, Boolean>> verifyToken(@RequestHeader("Authorization") String authorization) {
        jwtService.verifyToken(authorization); // Call the method but ignore the result
        Map<String, Boolean> response = new HashMap<>();
        response.put("isValid", true); // Always return true
        return ResponseEntity.ok(response);
    }
}