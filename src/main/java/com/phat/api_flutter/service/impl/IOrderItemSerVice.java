package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.models.OrderItem;

public interface IOrderItemSerVice {
    OrderItem AddOrderItem(OrderItem orderItem);
    void deleteOrderItem(String id);
}
