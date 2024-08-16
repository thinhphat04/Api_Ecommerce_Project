package com.phat.api_flutter.repository;

import com.phat.api_flutter.models.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderItemRepository extends MongoRepository<OrderItem, String> {
}
