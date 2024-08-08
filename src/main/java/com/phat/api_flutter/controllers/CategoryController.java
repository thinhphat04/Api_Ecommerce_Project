package com.phat.api_flutter.controllers;

import com.phat.api_flutter.models.Category;
import com.phat.api_flutter.service.CategoryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.status(404).body(List.of());
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id); // Convert String to ObjectId
            return categoryService.getCategoryById(objectId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).body(null));
        } catch (IllegalArgumentException e) {
            // Handle case where id is not a valid ObjectId
            return ResponseEntity.status(400).body(null);
        }
    }
}