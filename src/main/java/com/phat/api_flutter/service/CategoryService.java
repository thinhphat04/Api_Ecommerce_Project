package com.phat.api_flutter.service;

import com.phat.api_flutter.models.Category;
import com.phat.api_flutter.repository.CategoryRepository;
import com.phat.api_flutter.service.impl.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category findById(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category updateCategory(Category category) {
        Category existingCategory = categoryRepository.findById(category.get_id()).orElse(null);
        if (existingCategory != null) {
            existingCategory.set_id(category.get_id()); ;
            existingCategory.setName(category.getName());
            existingCategory.setImage(category.getImage());
            existingCategory.setColour(category.getColour());
            existingCategory.setMarkedForDeletion(category.isMarkedForDeletion());
            return categoryRepository.save(existingCategory);
        }
        return existingCategory;
    }

}
