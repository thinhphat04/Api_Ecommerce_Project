package com.phat.api_flutter.controllers;

import com.phat.api_flutter.dto.ProductDto;
import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.service.ProductService;
import com.phat.api_flutter.service.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(
            @RequestParam Optional<String> criteria,
            @RequestParam Optional<String> category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            List<ProductDto> products = productService.getProducts(criteria, category, page, pageSize).getContent();
            if (products.isEmpty()) {
                return ResponseEntity.status(404).body(List.of());
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String q,
            @RequestParam Optional<String> category,
            @RequestParam Optional<String> genderAgeCategory,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<Product> searchResults = productService.searchProducts(q, category, genderAgeCategory, page, pageSize).getContent();
        return ResponseEntity.ok(searchResults);
    }
}