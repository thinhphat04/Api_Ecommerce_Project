package com.phat.api_flutter.service;


import com.phat.api_flutter.models.CustomUser;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }
//v1
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User userDetail = userRepository.findUserByName(username);
//        if (userDetail == null) {
//            throw new UsernameNotFoundException("User name not found: " + username);
//        }
//        // Chuyển đổi từ User thành CustomUser
//        return new CustomUser(userDetail);
//    }

//    v2
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found: " + username));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), new ArrayList<>());
    }

    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        User userDetail = userRepository.findUserByEmail(email);
        if (userDetail == null) {
            throw new UsernameNotFoundException("User email not found: " + email);
        }
        // Chuyển đổi từ User thành CustomUser
        return userDetail;
    }


    public User addUser(User userInfo) {
        userInfo.setPasswordHash(encoder.encode(userInfo.getPasswordHash()));
        if(userInfo.getRoles() == null || userInfo.getRoles().trim().isBlank()) {
            userInfo.setRoles("USER");
        }
        userRepository.save(userInfo);
        return userInfo;
    }

}
