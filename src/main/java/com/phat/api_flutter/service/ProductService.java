package com.phat.api_flutter.service;

import com.phat.api_flutter.dto.ProductDto;
import com.phat.api_flutter.models.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductService {
    Page<ProductDto> getProducts(Optional<String> criteria, Optional<String> category, int page, int pageSize);
    Optional<Product> getProductById(String id);

}
