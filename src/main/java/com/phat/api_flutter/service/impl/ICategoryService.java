package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.models.Category;
import com.phat.api_flutter.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> getCategories();

    Optional<Category> getCategoryById(String id);
}
