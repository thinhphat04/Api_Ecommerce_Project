package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.dto.ProductDto;
import com.phat.api_flutter.models.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IProductService {
    Page<ProductDto> getProducts(Optional<String> criteria, Optional<String> category, int page, int pageSize);
    Optional<Product> getProductById(String id);
    Page<Product> searchProducts(String searchTerm, Optional<String> category, Optional<String> genderAgeCategory, int page, int pageSize);
    Product addProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(String id);
    long countProducts();
}
