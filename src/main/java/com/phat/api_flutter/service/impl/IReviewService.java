package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.dto.ReviewDTO;
import com.phat.api_flutter.models.Review;

import java.util.List;

public interface IReviewService {
    void deleteReview(String id);

    Review leaveReview(ReviewDTO reviewDTO);


}
