package com.phat.api_flutter.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminOrderResponseDTO {
    private String id;
    private List<OrderItemResponseDTO> orderItems;
    private String shippingAddress1;
    private String city;
    private String postalCode;
    private String country;
    private String phone;
    private String paymentId;
    private String status;
    private List<String> statusHistory;
    private Double totalPrice;
    private String userId;
    private Data dateOrdered;
    private int version;
}
