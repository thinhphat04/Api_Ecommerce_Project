package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.dto.OrderResponseDTO;
import com.phat.api_flutter.dto.OrdersResponseDTO;
import com.phat.api_flutter.models.Order;

import java.util.List;

public interface IOrderService {
    Order addOrder(Order order);
    OrdersResponseDTO showOrderByUserId(String userId);
    Order OrderDetails(String orderId);
    boolean DeleteOrderByCusID(String CusID);
    List<Order> ShowAllOrder();
    Order UpdateOrder(Order order);
    Order getOrderById(String orderId);
    long orderCount();
    List<Order> findByUserId(String id);

    boolean deleteOrderById(String orderId);
}
