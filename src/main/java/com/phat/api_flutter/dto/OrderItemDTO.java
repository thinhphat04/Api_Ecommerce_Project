package com.phat.api_flutter.dto;

import com.phat.api_flutter.models.OrderItem;
import lombok.Data;

@Data
public class OrderItemDTO {
    private String id;
    private String product;
    private String productName;
    private String productImage;
    private double productPrice;
    private int quantity;
    private String selectedSize;
    private String selectedColour;
    // Constructor
    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.productName = orderItem.getProductName();
        this.productImage = orderItem.getProductImage();
    }
}
