package com.phat.api_flutter.converters;

import com.phat.api_flutter.models.Review;
import com.phat.api_flutter.repository.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ObjectIdToReviewConverter implements Converter<ObjectId, Review> {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review convert(ObjectId source) {
        return reviewRepository.findById(source.toHexString()).orElse(null);
    }
}
