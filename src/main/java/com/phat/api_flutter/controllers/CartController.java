package com.phat.api_flutter.controllers;

import com.phat.api_flutter.dto.AddToCartRequest;
import com.phat.api_flutter.dto.AddToCartResponse;
import com.phat.api_flutter.dto.CartProductDto;
import com.phat.api_flutter.service.impl.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/{userId}")
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping("/cart")
    public ResponseEntity<?> getUserCart(@PathVariable String userId) {
        try {
            List<CartProductDto> cart = cartService.getUserCart(userId);
            if (cart.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("type", e.getClass().getSimpleName(), "message", e.getMessage()));
        }
    }

    @GetMapping("/cart/count")
    public ResponseEntity<?> getUserCartCount(@PathVariable("userId") String userId) {
        try {
            return ResponseEntity.ok(cartService.getUserCartCount(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("type", e.getClass().getSimpleName(), "message", e.getMessage()));
        }
    }

    @PostMapping("/cart")
    public ResponseEntity<?> addToCart(@PathVariable("userId") String userId, @RequestBody AddToCartRequest request) {
        try {
            AddToCartResponse cartProduct = cartService.addToCart(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(cartProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/cart/{cartProductId}")
    public ResponseEntity<?> getCartProductByUserIdAndCartProductId(@PathVariable("userId") String userId, @PathVariable("cartProductId") String cartProductId) {
        try {
            CartProductDto cartProduct = cartService.getCartProductByUserIdAndCartProductId(userId, cartProductId);
            return ResponseEntity.ok(cartProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("type", e.getClass().getSimpleName(), "message", e.getMessage()));
        }
    }


    @PutMapping("/cart/{cartProductId}")
    public ResponseEntity<?> modifyProductQuantity(
            @PathVariable("userId") String userId,
            @PathVariable("cartProductId") String cartProductId,
            @RequestBody Map<String, Integer> requestBody) {
        try {
            // Extract the quantity from the request body
            Integer quantity = requestBody.get("quantity");

            // Validate that quantity > 0
            if (quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Quantity must be greater than zero"));
            }

            CartProductDto updatedCartProduct = cartService.modifyProductQuantity(userId, cartProductId, quantity);
            return ResponseEntity.ok(updatedCartProduct);

        }  catch (RuntimeException e) {
            // Handle other runtime exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("type", e.getClass().getSimpleName(), "message", e.getMessage()));
        }
    }

    @DeleteMapping("/cart/{cartProductId}")
    public ResponseEntity<?> removeCartProduct(
            @PathVariable("userId") String userId,
            @PathVariable("cartProductId") String cartProductId) {
        try {
            cartService.removeCartProduct(userId, cartProductId);
            return ResponseEntity.noContent().build(); // Return 204 No Content on successful deletion
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("type", e.getClass().getSimpleName(), "message", e.getMessage()));
        }
    }


}