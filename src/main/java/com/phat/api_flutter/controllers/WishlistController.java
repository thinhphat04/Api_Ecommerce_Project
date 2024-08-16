package com.phat.api_flutter.controllers;

import com.phat.api_flutter.dto.WishlistItemDto;
import com.phat.api_flutter.models.WishlistItem;
import com.phat.api_flutter.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/{userId}/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<?> getUserWishlist(@PathVariable String userId) {
        try {
            List<WishlistItemDto> wishlist = wishlistService.getUserWishlist(userId);
            return ResponseEntity.ok(wishlist);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addToWishlist(@PathVariable String userId, @RequestBody Map<String, String> requestBody) {
        String productId = requestBody.get("productId");
        try {
            wishlistService.addToWishlist(userId, productId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable String userId, @PathVariable String productId) {
        try {
            wishlistService.removeFromWishlist(userId, productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

}
