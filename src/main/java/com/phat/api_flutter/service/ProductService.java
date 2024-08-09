package com.phat.api_flutter.service;

import com.phat.api_flutter.ImplementServices.IProductService;
import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.repository.CategoryRepository;
import com.phat.api_flutter.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    LocalDateTime now = LocalDateTime.now();
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        try{
    List<Product> products = productRepository.findAll();
            return products;
        }
        catch (Exception e){
            throw new IllegalArgumentException("Error getting products: " + e.getMessage());
        }
    }

    @Override
    public Product getOneProduct(String id) {
        return productRepository.findById(id).orElse(null);
    }


    @Override
    public Product createProduct(Product product) {
        // Check if category is valid
        if (product.getCategory() == null || !categoryRepository.existsById(product.getCategory().get_id())) {
            throw new IllegalArgumentException("Invalid category.");
        }
//        product.setDateAdded(now);
        product.setDateAdded(now);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String id, Product product) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product updatedProduct  = existingProduct.get();
            // Check if category is valid
            if (updatedProduct.getCategory() == null || !categoryRepository.existsById(updatedProduct.getCategory().get_id())) {
                throw new IllegalArgumentException("Invalid category.");
            }

            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setRating(product.getRating());
            updatedProduct.setColours(product.getColours());
            updatedProduct.setImage(product.getImage());
            updatedProduct.setImages(product.getImages());
            updatedProduct.setReviews(product.getReviews());
            updatedProduct.setNumberOfReviews(product.getNumberOfReviews());
            updatedProduct.setSizes(product.getSizes());
            updatedProduct.setCategory(product.getCategory());
            updatedProduct.setGenderAgeCategory(product.getGenderAgeCategory());
            updatedProduct.setCountInStock(product.getCountInStock());
            updatedProduct.setDateAdded(LocalDateTime.now());

            return productRepository.save(product);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteProduct(String id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            productRepository.delete(existingProduct.get());
            return true;
        } else {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
    }
    @Override
    public List<Product> findByProductName(String productName){
        return productRepository.findByName(productName);
    }
}

