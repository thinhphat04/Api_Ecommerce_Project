package com.phat.api_flutter.controllers;


import com.phat.api_flutter.dto.CategoryDTO;
import com.phat.api_flutter.models.Category;
import com.phat.api_flutter.models.Order;
import com.phat.api_flutter.models.Product;
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
import java.util.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

     ICategoryService categoryService;
     IProductService productService;
     IUserServiceAdmin userService;
     IOrderService orderService;
    private MediaHelper mediaHelper;

    //User

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

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        try {
            Category categoryOptional = categoryService.findById(id);
            if (categoryOptional == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Category not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            categoryOptional.setMarkedForDeletion(true);
            categoryService.updateCategory(categoryOptional);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("type", e.getClass().getSimpleName());
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //PRODUCT
    @PostMapping("/products/")
    public ResponseEntity<?> addProduct(@RequestParam("name") String name,
                                        @RequestParam("category") String categoryId,
                                        @RequestParam("countInStock") int countInStock,
                                        @RequestParam("description") String description,
                                        @RequestParam("price") double price,
                                        @RequestParam("image") MultipartFile image,
                                        @RequestParam(value = "images", required = false) MultipartFile[] images,
                                        @RequestParam("genderAgeCategory") String genderAgeCategory,
                                        @RequestParam(value = "colours[0]",required = false) String colours0,
                                        @RequestParam(value = "colours[1]",required = false) String colours1,
                                        @RequestParam(value = "colours[2]",required = false) String colours2,
                                        @RequestParam(value = "sizes[0]",required = false) String sizes0,
                                        @RequestParam(value = "sizes[1]",required = false) String sizes1,
                                        @RequestParam(value = "sizes[2]",required = false) String sizes2,
                                        @RequestParam(value = "sizes[3]",required = false) String sizes3
                                            ) {
        try {
            List<String> colours = new ArrayList<>();
            colours.add(colours0);
            colours.add(colours1);
            colours.add(colours2);
            List<String> sizes = new ArrayList<>();
            sizes.add(sizes0);
            sizes.add(sizes1);
            sizes.add(sizes2);
            sizes.add(sizes3);
            // Xử lý upload ảnh chính
            String imageUrl = null;
            if (!image.isEmpty()) {
                imageUrl = mediaHelper.uploadFile(image);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No file found");
            }

            // Xử lý upload gallery ảnh
            List<String> imagePaths = new ArrayList<>();
            if (images != null && images.length > 0) {
                for (MultipartFile galleryImage : images) {
                    String imagePath = mediaHelper.uploadFile(galleryImage);
                    imagePaths.add(imagePath);
                }
            }
            // Kiểm tra category
            Category category = categoryService.findById(categoryId);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Category");
            }

            if (category.isMarkedForDeletion()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Category marked for deletion, you cannot add products to this category");
            }

            // Chuyển đổi GenderAgeCategory từ String sang Enum
            Product.GenderAgeCategory genderAgeCategoryEnum = Product.convertStringtoEnums(genderAgeCategory);
            if (genderAgeCategoryEnum == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid GenderAgeCategory");
            }

            // Tạo đối tượng sản phẩm mới
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setColours(colours);
            product.setImage(imageUrl);
            if (!imagePaths.isEmpty()) {
                product.setImages(imagePaths);
            }
            product.setSizes(sizes);
            product.setCategory(category);
            product.setGenderAgeCategory(genderAgeCategoryEnum);
            product.setCountInStock(countInStock);
            product.setDateAdded(new Date()); // Lưu ngày thêm sản phẩm

            // Lưu sản phẩm vào cơ sở dữ liệu
            Product savedProduct = productService.addProduct(product);
            if (savedProduct == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The product could not be created");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("type", e.getClass().getSimpleName(), "message", e.getMessage()));
        }
    }
}
