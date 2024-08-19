package com.phat.api_flutter.service;

import com.phat.api_flutter.dto.CartItemDTO;
import com.phat.api_flutter.dto.CheckoutRequestDTO;
import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.repository.ProductRepository;
import com.phat.api_flutter.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CheckoutService {

    @Value("${stripe.key}")
    private String stripeKey;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }

    public Session createCheckoutSession(CheckoutRequestDTO checkoutRequest, String accessToken) throws StripeException {
        // Decode JWT and find user
        Claims claims = Jwts.parser()
                .setSigningKey("your_secret_key") // Replace with your actual secret key
                .parseClaimsJws(accessToken)
                .getBody();
        String userId = claims.getSubject();
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found!");
        }
        User user = userOptional.get();

        // Validate cart items
        for (CartItemDTO cartItem : checkoutRequest.getCartItems()) {
            Optional<Product> productOptional = productRepository.findById(cartItem.getProductId());
            if (!productOptional.isPresent()) {
                throw new RuntimeException(cartItem.getName() + " not found!");
            }
            Product product = productOptional.get();
            if (product.getCountInStock() < cartItem.getQuantity()) {
                throw new RuntimeException(product.getName() + " - Order for " + cartItem.getQuantity() + ", but only " + product.getCountInStock() + " left in stock");
            }
        }

        // Create Stripe session
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://your-success-url.com")
                .setCancelUrl("https://your-cancel-url.com")
                .setCustomerEmail(user.getEmail());

        for (CartItemDTO cartItem : checkoutRequest.getCartItems()) {
            paramsBuilder.addLineItem(SessionCreateParams.LineItem.builder()
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount((long) (cartItem.getPrice() * 100))
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(cartItem.getName())
                                    .addImage(cartItem.getImages().get(0))
                                    .build())
                            .build())
                    .setQuantity((long) cartItem.getQuantity())
                    .build());
        }

        SessionCreateParams params = paramsBuilder.build();
        return Session.create(params);
    }

    public void handleWebhook(String payload, String sigHeader) {
        // Handle Stripe webhook events
        // Similar to the provided JavaScript code
    }
}