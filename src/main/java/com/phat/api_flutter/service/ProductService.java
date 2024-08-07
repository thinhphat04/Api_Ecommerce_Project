package com.phat.api_flutter.service;

import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getProducts(Optional<String> criteria, Optional<String> category, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        if (criteria.isPresent()) {
            String crit = criteria.get();
            if (crit.equals("newArrivals")) {
                Date twoWeeksAgo = new Date(System.currentTimeMillis() - 140 * 24 * 60 * 60 * 1000L);
                if (category.isPresent()) {
                    return productRepository.findByCategoryAndDateAddedAfter(category.get(), twoWeeksAgo, pageable);
                } else {
                    return productRepository.findByDateAddedAfter(twoWeeksAgo, pageable);
                }
            } else if (crit.equals("popular")) {
                return productRepository.findByRatingGreaterThanEqual(4.5, pageable);
            }
        } else if (category.isPresent()) {
            return productRepository.findByCategory(category.get(), pageable);
        }

        return productRepository.findAll(pageable);
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Page<Product> searchProducts(String searchTerm, Optional<String> category, Optional<String> genderAgeCategory, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        if (category.isPresent() && genderAgeCategory.isPresent()) {
            return productRepository.findByCategoryAndGenderAgeCategoryAndNameContainingIgnoreCase(
                    category.get(), genderAgeCategory.get(), searchTerm, pageable);
        } else if (category.isPresent()) {
            return productRepository.findByCategoryAndNameContainingIgnoreCase(category.get(), searchTerm, pageable);
        } else {
            return productRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
        }
    }
}