package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.models.Order;

import java.util.List;

public interface IOrderService {
    Order AddOrder(Order order);
    List<Order> ShowOrderByUserId(String userId);
    Order OrderDetails(String orderId);
    boolean DeleteOrderByCusID(String CusID);
    List<Order> ShowAllOrder();
    Order UpdateOrder(Order order);
    Order GetOneOrder(String orderId);

    List<Order> findByUserId(String id);
}
