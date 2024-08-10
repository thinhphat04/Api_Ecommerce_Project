package com.phat.api_flutter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String subject, String body, String successMessage, String errorMessage) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(System.getenv("EMAIL"));
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println(successMessage);
        } catch (Exception e) {
            System.err.println(errorMessage);
            e.printStackTrace();
        }
    }
}