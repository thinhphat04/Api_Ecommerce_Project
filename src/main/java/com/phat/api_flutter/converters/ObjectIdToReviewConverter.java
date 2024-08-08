package com.phat.api_flutter.converters;

import com.phat.api_flutter.models.Review;
import com.phat.api_flutter.repository.ReviewRepository;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class ObjectIdToReviewConverter implements Converter<ObjectId, Review> {

    private  ReviewRepository reviewRepository  = new ReviewRepository() {
        @Override
        public <S extends Review> S insert(S entity) {
            return null;
        }

        @Override
        public <S extends Review> List<S> insert(Iterable<S> entities) {
            return List.of();
        }

        @Override
        public <S extends Review> List<S> findAll(Example<S> example) {
            return List.of();
        }

        @Override
        public <S extends Review> List<S> findAll(Example<S> example, Sort sort) {
            return List.of();
        }

        @Override
        public <S extends Review> List<S> saveAll(Iterable<S> entities) {
            return List.of();
        }

        @Override
        public List<Review> findAll() {
            return List.of();
        }

        @Override
        public List<Review> findAllById(Iterable<Object> objects) {
            return List.of();
        }

        @Override
        public <S extends Review> S save(S entity) {
            return null;
        }

        @Override
        public Optional<Review> findById(Object o) {
            return Optional.empty();
        }

        @Override
        public boolean existsById(Object o) {
            return false;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(Object o) {

        }

        @Override
        public void delete(Review entity) {

        }

        @Override
        public void deleteAllById(Iterable<?> objects) {

        }

        @Override
        public void deleteAll(Iterable<? extends Review> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public List<Review> findAll(Sort sort) {
            return List.of();
        }

        @Override
        public Page<Review> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public <S extends Review> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends Review> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends Review> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends Review> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends Review, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }
    };

    public ObjectIdToReviewConverter() {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review convert(ObjectId source) {
        return reviewRepository.findById(source.toHexString()).orElse(null);
    }
}
