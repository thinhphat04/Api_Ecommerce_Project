package com.phat.api_flutter.service;

import com.phat.api_flutter.dto.WishlistItemDto;
import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.models.WishlistItem;
import com.phat.api_flutter.repository.ProductRepository;
import com.phat.api_flutter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WishlistService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<WishlistItemDto> getUserWishlist(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<WishlistItemDto> wishlistDto = new ArrayList<>();
        for (WishlistItem wishProduct : user.getWishlist()) {
            if (wishProduct.getProductId() == null) {
                // Log a warning and continue to the next item
                System.err.println("Warning: Wishlist item with null productId found for user: " + userId);
                continue; // Skip this item or handle it as needed
            }

            Product product = productRepository.findById(wishProduct.getProductId()).orElse(null);

            WishlistItemDto responseItem;
            if (product != null) {
                responseItem = new WishlistItemDto(
                        product.getId(),
                        product.getImage(),
                        product.getPrice(),
                        product.getName(),
                        true,
                        // Check if the product is out of stock
                        product.getCountInStock() < 1
                );
            } else {
                responseItem = new WishlistItemDto(
                        wishProduct.getProductId(),
                        wishProduct.getProductImage(),
                        wishProduct.getProductPrice(),
                        wishProduct.getProductName(),
                        false,
                        false
                );
            }

            wishlistDto.add(responseItem);
        }

        return wishlistDto;
    }


    public void addToWishlist(String userId, String productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));


        boolean exists = user.getWishlist().stream()
                .anyMatch(item -> Objects.equals(productId, item.getProductId()));

        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product already exists in wishlist.");
        }

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setProductId(productId);
        wishlistItem.setProductName(product.getName());
        wishlistItem.setProductImage(product.getImage());
        wishlistItem.setProductPrice(product.getPrice());
        user.getWishlist().add(wishlistItem);
        userRepository.save(user);
    }

    public void removeFromWishlist(String userId, String productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean removed = user.getWishlist().removeIf(item ->  productId.equals(item.getProductId()));

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in wishlist.");
        }

        userRepository.save(user);
    }
}
