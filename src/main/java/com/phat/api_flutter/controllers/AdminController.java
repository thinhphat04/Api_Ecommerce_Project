package com.phat.api_flutter.controllers;


import com.phat.api_flutter.dto.CategoryDTO;
import com.phat.api_flutter.dto.DeletedImagesDTO;
import com.phat.api_flutter.models.Category;
import com.phat.api_flutter.models.Order;
import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.service.impl.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    ICategoryService categoryService;
    IProductService productService;
    IUserServiceAdmin userService;
    IOrderService orderService;
    IReviewService reviewService;
    private MediaHelper mediaHelper;

    //User

    @Autowired
    public AdminController(ICategoryService categoryService,
                           IProductService productService,
                           IUserServiceAdmin userService,
                           IReviewService reviewService,
                           MediaHelper mediaHelper) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
        this.reviewService = reviewService;
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
            @RequestBody CategoryDTO category) {
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
                                        @RequestParam(value = "colours[0]", required = false) String colours0,
                                        @RequestParam(value = "colours[1]", required = false) String colours1,
                                        @RequestParam(value = "colours[2]", required = false) String colours2,
                                        @RequestParam(value = "sizes[0]", required = false) String sizes0,
                                        @RequestParam(value = "sizes[1]", required = false) String sizes1,
                                        @RequestParam(value = "sizes[2]", required = false) String sizes2,
                                        @RequestParam(value = "sizes[3]", required = false) String sizes3
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

    // Edit product
    @PutMapping("/products/{id}")
    public ResponseEntity<?> editProduct(@PathVariable("id") String id,
                                         @RequestParam(value = "images", required = false) MultipartFile[] images,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "countInStock", required = false) Integer countInStock,
                                         @RequestBody(required = false) String categoryId) {
        try {
            // Kiểm tra tính hợp lệ của ID sản phẩm
            Optional<Product> productOptional = productService.getProductById(id);
            if (productOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Invalid Product"));
            }
            Product product = productOptional.get();

            // Kiểm tra tính hợp lệ của Category nếu có
            if (categoryId != null) {
                Category categoryOptional = categoryService.findById(categoryId);
                if (categoryOptional == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Invalid Category"));
                }
                product.setCategory(categoryOptional);
            }

            // Xử lý upload gallery ảnh
            boolean galleryUpdate = (images != null && images.length > 0);
            if (galleryUpdate) {
                int limit = 10 - (product.getImages() != null ? product.getImages().size() : 0);
                if (images.length > limit) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("message", "Cannot upload more than " + limit + " images"));
                }

                List<String> imagePaths = new ArrayList<>();
                for (MultipartFile imageFile : images) {
                    String imagePath = mediaHelper.uploadFile(imageFile);
                    imagePaths.add(imagePath);
                }

                List<String> existingImages = product.getImages() != null ? product.getImages() : new ArrayList<>();
                existingImages.addAll(imagePaths);
                product.setImages(existingImages);
            }

            // Cập nhật các thuộc tính khác của sản phẩm
            if (name != null) {
                product.setName(name);
            }
            if (countInStock != null) {
                product.setCountInStock(countInStock);
            }

            // Lưu sản phẩm đã cập nhật vào cơ sở dữ liệu
            Product updatedProduct = productService.updateProduct(product);

            return ResponseEntity.ok(updatedProduct);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("type", e.getClass().getSimpleName(), "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("type", e.getClass().getSimpleName(), "message", e.getMessage()));
        }
    }

    //Delete Image Products
    @DeleteMapping("/products/{id}/images")
    public ResponseEntity<?> deleteProductImages(@PathVariable("id") String productId,
                                                 @RequestBody DeletedImagesDTO deletedImageURLs) {
        try {

            Optional<Product> product = productService.getProductById(productId);

            // Validate productId and deletedImageURLs
            if (deletedImageURLs == null || deletedImageURLs.getDeletedImageURLs() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid request data"));
            } else if (product.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Product not found"));
            }
            // Delete the images
            mediaHelper.deleteImages(deletedImageURLs.getDeletedImageURLs());
            //Convert the array[String] to List<String>
            List<String> convertArraytoList = Arrays.asList(deletedImageURLs.getDeletedImageURLs());
            // Remove deleted images from the product.images array
            List<String> updatedImages = product.get().getImages().stream()
                    .filter(image -> !convertArraytoList.contains(image))
                    .collect(Collectors.toList());
            product.get().setImages(updatedImages);

            // Save the updated product
            productService.updateProduct(product.get());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content for a successful deletion
        } catch (IOException e) {
            // Handle file not found explicitly
            if (e instanceof FileNotFoundException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Image not found"));
            }

            // Handle other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    //Delete Product
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String productId) {
        try {
            Product product = productService.getProductById(productId).orElse(null);
            // Validate productId
            if (!ObjectId.isValid(productId)) {// Yes, it's a valid ObjectId, proceed with `findById` call.
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Invalid Product"));
            }

            // Find the product to get related data

            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Product not found"));
            }

            // Delete related images
            try {
                mediaHelper.deleteImages(product.getImages().toArray(new String[0])); // Assuming 'product.getImages()' returns a List<String>
            } catch (IOException e) {
                if ("ENOENT".equals(e.getMessage())) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("message", e.getMessage()));
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", e.getMessage()));
            }

            // Delete associated reviews
            if(product.getReviews() != null) {
                product.getReviews().forEach(reviewId -> reviewService.deleteReview(reviewId));
            }
            // Delete the product
            productService.deleteProduct(productId);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content for a successful deletion
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    //Product Count
    @GetMapping("/products/count")
    public ResponseEntity<?> getProductsCount() {
        try {
            long productCount = productService.countProducts();
            if (productCount < 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Could not count products"));
            }
            return ResponseEntity.ok(productCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}