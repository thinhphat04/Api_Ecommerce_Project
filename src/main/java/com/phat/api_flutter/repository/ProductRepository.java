package com.phat.api_flutter.repository;

import com.phat.api_flutter.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findByCategory(String category, Pageable pageable);

    Page<Product> findByCategoryAndDateAddedAfter(String category, Date dateAdded, Pageable pageable);

    Page<Product> findByDateAddedAfter(Date dateAdded, Pageable pageable);

    Page<Product> findByRatingGreaterThanEqual(double rating, Pageable pageable);

    Page<Product> findByCategoryAndGenderAgeCategoryAndNameContainingIgnoreCase(String category, String genderAgeCategory, String name, Pageable pageable);

    Page<Product> findByCategoryAndNameContainingIgnoreCase(String category, String name, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
