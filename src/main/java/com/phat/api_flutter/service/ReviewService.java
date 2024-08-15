package com.phat.api_flutter.service;

import com.phat.api_flutter.models.Review;
import com.phat.api_flutter.repository.ReviewRepository;
import com.phat.api_flutter.service.impl.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void deleteReview(String id) {
         reviewRepository.deleteById(id);
    }
}
