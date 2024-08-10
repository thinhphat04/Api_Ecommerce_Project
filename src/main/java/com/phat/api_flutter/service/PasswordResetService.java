package com.phat.api_flutter.service;

import com.phat.api_flutter.models.User;
import com.phat.api_flutter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int otp = generateOtp();
        user.setResetPasswordOtp(otp);
        user.setResetPasswordOtpExpires(new Date(System.currentTimeMillis() + 600000)); // OTP valid for 10 minutes
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), "Password Reset OTP",
                "Your OTP is: " + otp, "Email sent successfully", "Failed to send email");
    }

    private int generateOtp() {
        return (int) (Math.random() * 9000) + 1000; // Generates a 4-digit OTP
    }

    public boolean verifyOtp(String email, int otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getResetPasswordOtp() != otp || user.getResetPasswordOtpExpires().before(new Date())) {
            return false;
        }

        // If OTP is valid, proceed with further actions
        user.setResetPasswordOtp(1); // Mark OTP as used
        user.setResetPasswordOtpExpires(null); // Clear the OTP expiration field
        userRepository.save(user);
        return true;
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getResetPasswordOtp() != 1) {
            throw new RuntimeException("OTP has not been verified");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setResetPasswordOtp(null); // Clear the OTP field
        user.setResetPasswordOtpExpires(null);
        userRepository.save(user);
    }
}