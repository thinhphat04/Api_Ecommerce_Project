package com.phat.api_flutter.dto;

import lombok.Data;

import java.util.List;
@Data
public class OrderRequest {
    private List<OrderItemRequest> orderItems;
    private String shippingAddress1;
    private String city;
    private String postalCode;
    private String country;
    private String phone;
    private String paymentId;
    private double totalPrice;
    private String user;

}
