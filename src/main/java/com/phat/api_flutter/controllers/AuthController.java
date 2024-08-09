package com.phat.api_flutter.controllers;

import com.phat.api_flutter.dto.AuthenticationRequest;
import com.phat.api_flutter.dto.AuthenticationResponse;
import com.phat.api_flutter.models.Token;
import com.phat.api_flutter.repository.TokenRepository;
import com.phat.api_flutter.service.AuthService;
import com.phat.api_flutter.service.JwtService;
import com.phat.api_flutter.models.CustomUser;
import com.phat.api_flutter.models.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private UserService userService;
    private TokenRepository tokenRepository;
    private AuthService authService;
    AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtService jwtService;

    @Value("${ACCESS_TOKEN_SECRET}")
    private String ACCESS_TOKEN_SECRET;

    @Value("${REFRESH_TOKEN_SECRET}")
    private String REFRESH_TOKEN_SECRET;

    @Autowired
    public AuthController(UserService userService, TokenRepository tokenRepository, AuthService authService, AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@Valid @RequestBody AuthenticationRequest loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            User userOptional = authService.loadUserByEmail(email);
            if (userOptional == null) {
                return ResponseEntity.status(404).body("User not found! Check your email and try again.");
            }

            User user = userOptional;
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                String accessToken = Jwts.builder()
                        .setSubject(user.getName())
                        .claim("ROLE", user.getRoles())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                        .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET)
                        .compact();

                String refreshToken = Jwts.builder()
                        .setSubject(user.getName())
                        .claim("ROLE", user.getRoles())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 5184000000L)) // 60 days
                        .signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_SECRET)
                        .compact();

                tokenRepository.deleteByUserId(user.getName());
                tokenRepository.save(new Token(user.getName(), accessToken, refreshToken));

                user.setPasswordHash(null); // Exclude the password hash
                return ResponseEntity.ok(new AuthenticationResponse(user, accessToken, refreshToken));
            }

            return ResponseEntity.status(400).body("Incorrect password!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authenticationRequest) {
       User existingUser = userService.findByEmail(authenticationRequest.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(409).body("Email already exists");
        }

        User user = new User();
        user.setName(authenticationRequest.getName());
        user.setEmail(authenticationRequest.getEmail());
        user.setPasswordHash(authenticationRequest.getPassword());
        User savedUser = authService.addUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Api /api/random yêu cầu phải xác thực mới có thể request
    @GetMapping("/test")
    public String randomStuff(){
        return"JWT Hợp lệ mới có thể thấy được message này";
    }

}