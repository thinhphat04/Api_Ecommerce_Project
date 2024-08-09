package com.phat.api_flutter.ImplementServices;

import com.phat.api_flutter.models.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category getOneCategory(String id);
    Category createCategory(Category category);
    Category updateCategory(String id, Category updatedCategory);
    boolean deleteCategory(String id);
}
