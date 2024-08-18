package com.phat.api_flutter.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private int quantity;
    private String product;
    private String cartProductId;
    private double productPrice;
    private String productName;
    private String productImage;

    private String selectedSize;
    private String selectedColour;
}
