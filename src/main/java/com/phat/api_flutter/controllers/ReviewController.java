package com.phat.api_flutter.controllers;

import com.phat.api_flutter.dto.ReviewDTO;
import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.models.Review;
import com.phat.api_flutter.service.ProductService;
import com.phat.api_flutter.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/products")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<?> leaveReview(@PathVariable String productId, @RequestBody ReviewDTO reviewDTO) {
        try {
            // Set the productId manually from the path variable
            reviewDTO.setProductId(productId);

            // Proceed with the review creation process
            Review review = reviewService.leaveReview(reviewDTO);

            // Retrieve the updated product and add the review to it
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));


            productService.updateProduct(product);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("product", product, "review", review));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }



//    @GetMapping("/{productId}/reviews")
//    public ResponseEntity<List<Review>> getProductReviews(@PathVariable String productId) {
//        try {
//            List<Review> reviews = reviewService.getReviewsByProductId(productId);
//            if (reviews.isEmpty()) {
//                return ResponseEntity.status(200).body(List.of());
//            }
//            return ResponseEntity.ok(reviews);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(null);
//        }
//    }
}
