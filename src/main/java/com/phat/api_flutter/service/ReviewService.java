package com.phat.api_flutter.service;

import com.phat.api_flutter.dto.ReviewDTO;
import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.models.Review;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.repository.ProductRepository;
import com.phat.api_flutter.repository.ReviewRepository;
import com.phat.api_flutter.repository.UserRepository;
import com.phat.api_flutter.service.impl.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void deleteReview(String id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public Review leaveReview(ReviewDTO reviewDTO) {
        if (reviewDTO.getProductId() == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }
        if (reviewDTO.getUser() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User user = userRepository.findById(reviewDTO.getUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setUser(user);
        review.setUserName(user.getName());
        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());
        review.setProduct(product);
        review.setDate(LocalDateTime.now());

        review = reviewRepository.save(review);

        product.getReviews().add(review.getId());
        productRepository.save(product);

        return review;
    }


}

