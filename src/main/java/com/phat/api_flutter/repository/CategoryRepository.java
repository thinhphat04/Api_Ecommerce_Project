package com.phat.api_flutter.repository;

import com.phat.api_flutter.models.Category;
import com.phat.api_flutter.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
