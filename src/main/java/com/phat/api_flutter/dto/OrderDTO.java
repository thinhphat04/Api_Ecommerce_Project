package com.phat.api_flutter.dto;

import com.phat.api_flutter.models.Order;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDTO {
    private String id;
    private List<OrderItemDTO> orderItems;
    private String status;
    private double totalPrice;
    private Date dateOrdered;

    // Constructor
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
        this.status = order.getStatus().toString();
        this.totalPrice = order.getTotalPrice();
        this.dateOrdered = order.getDateOrdered();
    }

}
