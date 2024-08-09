package com.phat.api_flutter.controllers;

import com.phat.api_flutter.dto.AuthenticationRequest;
import com.phat.api_flutter.dto.AuthenticationResponse;
import com.phat.api_flutter.service.AuthService;
import com.phat.api_flutter.service.JwtService;
import com.phat.api_flutter.models.CustomUser;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@Valid @RequestBody AuthenticationRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(loginRequest.getUsername());
                return ResponseEntity.ok(new AuthenticationResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (BadCredentialsException e) {
            // Xử lý khi thông tin đăng nhập không hợp lệ
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (DisabledException e) {
            // Xử lý khi tài khoản bị vô hiệu hóa
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is disabled");
        } catch (UsernameNotFoundException e) {
            // Xử lý khi không tìm thấy người dùng
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            // Xử lý các lỗi khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authenticationRequest) {
       User existingUser = userService.findByEmail(authenticationRequest.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(409).body("Email already exists");
        }

        User user = new User();
        user.setUsername(authenticationRequest.getUsername());
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