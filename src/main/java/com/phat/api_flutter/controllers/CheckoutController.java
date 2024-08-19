package com.phat.api_flutter.controllers;


import com.phat.api_flutter.dto.CheckoutRequestDTO;
import com.phat.api_flutter.dto.CheckoutResponseDTO;
import com.phat.api_flutter.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<CheckoutResponseDTO> checkout(@RequestBody CheckoutRequestDTO checkoutRequest, HttpServletRequest request) {
        try {
            String accessToken = request.getHeader("Authorization").replace("Bearer", "").trim();
            Session session = checkoutService.createCheckoutSession(checkoutRequest, accessToken);
            return ResponseEntity.status(201).body(new CheckoutResponseDTO(session.getUrl()));
        } catch (StripeException e) {
            return ResponseEntity.status(500).body(new CheckoutResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            checkoutService.handleWebhook(payload, sigHeader);
            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Webhook Error: " + e.getMessage());
        }
    }
}