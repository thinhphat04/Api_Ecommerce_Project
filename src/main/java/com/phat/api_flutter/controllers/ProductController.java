package com.phat.api_flutter.controllers;

import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.repository.ProductRepository;
import com.phat.api_flutter.service.ProductService;
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

    @Autowired
    ProductRepository productRepository;

    @GetMapping
//    public ResponseEntity<Page<Product>> getProducts(
//            @RequestParam Optional<String> criteria,
//            @RequestParam Optional<String> category,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "1") int pageSize) {
//        Page<Product> products = productService.getProducts(criteria, category, page, pageSize);
//        if (products.isEmpty()) {
//            return ResponseEntity.status(404).body(null);
//        }
//        return ResponseEntity.ok(products);
//    }
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String q,
            @RequestParam Optional<String> category,
            @RequestParam Optional<String> genderAgeCategory,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int pageSize) {
        Page<Product> searchResults = productService.searchProducts(q, category, genderAgeCategory, page, pageSize);
        return ResponseEntity.ok(searchResults);
    }
}