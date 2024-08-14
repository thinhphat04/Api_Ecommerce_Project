package com.phat.api_flutter.controllers;


import com.phat.api_flutter.dto.CategoryDTO;
import com.phat.api_flutter.models.Category;
import com.phat.api_flutter.models.Order;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.service.impl.ICategoryService;
import com.phat.api_flutter.service.impl.IOrderService;
import com.phat.api_flutter.service.impl.IProductService;
import com.phat.api_flutter.service.impl.IUserServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

     ICategoryService categoryService;
     IProductService productService;
     IUserServiceAdmin userService;
     IOrderService orderService;
    private MediaHelper mediaHelper;

     @Autowired
     public AdminController(ICategoryService categoryService,
                            IProductService productService,
                            IUserServiceAdmin userService,
                            MediaHelper mediaHelper) {
         this.categoryService = categoryService;
         this.productService = productService;
         this.userService = userService;
         this.mediaHelper = mediaHelper;
     }
    @GetMapping("/users/count")
    public ResponseEntity<Map<String, String>> getUserCount() {
        long userCount = userService.getUsersCount();
        if (userCount == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Could not count users"));
        }
        return ResponseEntity.ok(Map.of("message", "User count: " + userCount));
    }

//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
//        User user = userService.findById(id);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        List<Order> orders = orderService.findByUserId(id);
//        List<String> orderItemIds = orders.stream()
//                .flatMap(order -> order.getOrderItems().stream())
//                .map(ObjectId::toString)
//                .collect(Collectors.toList());
//
//        cartProductRepository.deleteByIdIn(user.get().getCartProducts());
//        userRepository.deleteById(id);
//        orderRepository.deleteByUserId(id);
//        orderItemRepository.deleteByIdIn(orderItemIds);
//        tokenRepository.deleteByUserId(id);
//
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
    // CATEGORY

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("colour") String colour) {
        try {
            // Upload image
            String imageUrl = mediaHelper.uploadFile(image);

            // Create new Category object
            Category category = new Category();
            category.setName(name);
            category.setColour(colour);
            category.setImage(imageUrl);

            // Save category
            Category savedCategory = categoryService.addCategory(category);
            if (savedCategory == null) {
                return ResponseEntity.status(400).body(null);
            }

            return ResponseEntity.status(201).body(savedCategory);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> editCategory(
            @PathVariable String id,
            @RequestBody CategoryDTO category){
        try {

            // Tìm category theo id
            Category categoryOptional = categoryService.findById(id);
            if (categoryOptional == null) {
                return ResponseEntity.status(404).body(null);
            }


            categoryOptional.setName(category.getName());

            // Lưu category đã chỉnh sửa
            Category updatedCategory = categoryService.updateCategory(categoryOptional);

            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

//    @DeleteMapping("/categories/{id}")
//    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
//        Optional<Category> categoryOpt = categoryRepository.findById(id);
//        if (!categoryOpt.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        Category category = categoryOpt.get();
//        category.setMarkedForDeletion(true);
//        categoryRepository.save(category);
//
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

    // ORDER

//    @GetMapping("/orders")
//    public ResponseEntity<List<Order>> getOrders() {
//        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "dateOrdered"));
//        return ResponseEntity.ok(orders);
//    }
//
//    @GetMapping("/orders/count")
//    public ResponseEntity<Map<String, Long>> getOrdersCount() {
//        long count = orderRepository.count();
//        return ResponseEntity.ok(Map.of("count", count));
//    }

//    @PatchMapping("/orders/{id}/status")
//    public ResponseEntity<Order> changeOrderStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
//        Optional<Order> orderOpt = orderRepository.findById(id);
//        if (!orderOpt.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        Order order = orderOpt.get();
//        String newStatus = body.get("status");
//
//        if (isValidStatusTransition(order.getStatus(), newStatus)) {
//            order.getStatusHistory().add(order.getStatus());
//            order.setStatus(newStatus);
//            return ResponseEntity.ok(orderRepository.save(order));
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
//
//    @DeleteMapping("/orders/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
//        Optional<Order> orderOpt = orderRepository.findById(id);
//        if (!orderOpt.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        Order order = orderOpt.get();
//        for (String orderItemId : order.getOrderItems()) {
//            orderItemRepository.deleteById(orderItemId);
//        }
//        orderRepository.deleteById(id);
//
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//    // PRODUCT
//
//    @GetMapping("/products/count")
//    public ResponseEntity<Map<String, Long>> getProductsCount() {
//        long productCount = productRepository.count();
//        if (productCount == 0) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Could not count products"));
//        }
//        return ResponseEntity.ok(Map.of("productCount", productCount));
//    }
//
//    @PostMapping("/products")
//    public ResponseEntity<Product> addProduct(@RequestParam("image") MultipartFile image, @RequestParam("images") MultipartFile[] images, @RequestBody Map<String, String> body) {
//        try {
//            Category category = categoryRepository.findById(body.get("category")).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Category"));
//
//            if (category.isMarkedForDeletion()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//
//            String imageUrl = saveImage(image);
//            List<String> imagePaths = saveImages(images);
//
//            Product product = new Product(body.get("name"), imageUrl, imagePaths, category);
//            product = productRepository.save(product);
//            return ResponseEntity.status(HttpStatus.CREATED).body(product);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @PutMapping("/products/{id}")
//    public ResponseEntity<Product> editProduct(@PathVariable String id, @RequestParam("images") MultipartFile[] images, @RequestBody Map<String, String> body) {
//        if (!ObjectId.isValid(id) || !productRepository.existsById(id)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        Optional<Category> categoryOpt = categoryRepository.findById(body.get("category"));
//        if (body.containsKey("category") && !categoryOpt.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
//        product.updateProductDetails(body, saveImages(images));
//        return ResponseEntity.ok(productRepository.save(product));
//    }
//
    // Helper methods
//    private String saveImage(MultipartFile image) {
//        // Save image and return its URL
//    }
//
//    private List<String> saveImages(MultipartFile[] images) {
//        // Save multiple images and return their URLs
//    }
//
//    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
//        // Validate status transition
//    }


}
