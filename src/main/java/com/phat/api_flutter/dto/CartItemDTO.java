package com.phat.api_flutter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private String productId;
    private String name;
    private List<String> images;
    private int quantity;
    private double price;
    private String selectedSize;
    private String selectedColour;
}