package com.phat.api_flutter.service;

import com.phat.api_flutter.models.Order;
import com.phat.api_flutter.repository.OrderRepository;
import com.phat.api_flutter.service.impl.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;


    @Override
    public Order AddOrder(Order order) {
        return null;
    }

    @Override
    public List<Order> ShowOrderByUserId(String userId) {
        return List.of();
    }

    @Override
    public Order OrderDetails(String orderId) {
        return null;
    }

    @Override
    public boolean DeleteOrderByCusID(String CusID) {
        return false;
    }

    @Override
    public List<Order> ShowAllOrder() {
        return List.of();
    }

    @Override
    public Order UpdateOrder(Order order) {
        return null;
    }

    @Override
    public Order GetOneOrder(String orderId) {
        return null;
    }

    @Override
    public List<Order> findByUserId(String id) {
        return List.of();
    }
}
