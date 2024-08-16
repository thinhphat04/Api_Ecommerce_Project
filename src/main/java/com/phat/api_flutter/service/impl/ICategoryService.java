package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.models.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> getCategories();

    Optional<Category> getCategoryById(String id);

    Category addCategory(Category category);
    Category findById(String id);
    Category updateCategory(Category category);
}
