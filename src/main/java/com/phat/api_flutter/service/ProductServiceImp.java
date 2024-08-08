package com.phat.api_flutter.service;

import com.phat.api_flutter.dto.ProductDto;
import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<ProductDto> getProducts(Optional<String> criteria, Optional<String> category, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> productPage;

        if (criteria.isPresent()) {
            String crit = criteria.get();
            if ("newArrivals".equals(crit)) {
                Date twoWeeksAgo = new Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000L);
                if (category.isPresent()) {
                    productPage = productRepository.findByCategoryAndDateAddedAfter(category.get(), twoWeeksAgo, pageable);
                } else {
                    productPage = productRepository.findByDateAddedAfter(twoWeeksAgo, pageable);
                }
            } else if ("popular".equals(crit)) {
                productPage = productRepository.findByRatingGreaterThanEqual(4.5, pageable);
            } else {
                productPage = productRepository.findAll(pageable);
            }
        } else if (category.isPresent()) {
            productPage = productRepository.findByCategory(category.get(), pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        // Convert Page<Product> to Page<ProductDto>
        List<ProductDto> productDtos = productPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
    }

    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getRating(),
                product.getColours(),
                product.getImage(),
                product.getNumberOfReviews(),
                product.getCategory(),
                product.getGenderAgeCategory(),
                product.getCountInStock(),
                product.getDateAdded()
        );
    }


    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

//    public Page<Product> searchProducts(String searchTerm, Optional<String> category, Optional<String> genderAgeCategory, int page, int pageSize) {
//        Pageable pageable = PageRequest.of(page - 1, pageSize);
//
//        if (category.isPresent() && genderAgeCategory.isPresent()) {
//            return productRepository.findByCategoryAndGenderAgeCategoryAndNameContainingIgnoreCase(
//                    category.get(), genderAgeCategory.get(), searchTerm, pageable);
//        } else if (category.isPresent()) {
//            return productRepository.findByCategoryAndNameContainingIgnoreCase(category.get(), searchTerm, pageable);
//        } else {
//            return productRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
//        }
//    }
}