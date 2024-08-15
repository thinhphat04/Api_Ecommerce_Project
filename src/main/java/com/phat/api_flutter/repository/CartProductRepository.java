package com.phat.api_flutter.repository;

import com.phat.api_flutter.models.CartProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends MongoRepository<CartProduct, String> {
}
