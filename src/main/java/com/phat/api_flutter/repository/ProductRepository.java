package com.phat.api_flutter.repository;

import com.phat.api_flutter.models.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {
    Page<Product> findByCategory(ObjectId category, Pageable pageable);
    Page<Product> findByCategoryAndDateAddedAfter(ObjectId category, Date dateAdded, Pageable pageable);
    Page<Product> findByDateAddedAfter(Date dateAdded, Pageable pageable);
    Page<Product> findByRatingGreaterThanEqual(double rating, Pageable pageable);
    Page<Product> findByCategoryAndGenderAgeCategoryAndNameContainingIgnoreCase(String category, String genderAgeCategory, String name, Pageable pageable);
    Page<Product> findByCategoryAndNameContainingIgnoreCase(ObjectId category, String name, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
