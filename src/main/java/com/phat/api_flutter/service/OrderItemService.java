package com.phat.api_flutter.service;

import com.phat.api_flutter.models.OrderItem;
import com.phat.api_flutter.repository.OrderItemRepository;
import com.phat.api_flutter.service.impl.IOrderItemSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService implements IOrderItemSerVice {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Override
    public OrderItem AddOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteOrderItem(String id) {
        OrderItem orderItem = orderItemRepository.findById(id).orElse(null);
            orderItemRepository.delete(orderItem);
    }
}
