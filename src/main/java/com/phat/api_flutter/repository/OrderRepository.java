package com.phat.api_flutter.repository;

import com.phat.api_flutter.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository  extends MongoRepository<Order, String> {
//    List<Order> findByCustomer_Id(String customerId);
}
