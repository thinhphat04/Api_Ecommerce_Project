package com.phat.api_flutter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDto {
    private String id;
    private String product;
    private int quantity;
    private String selectedSize;
    private String selectedColour;
    private String productName;
    private String productImage;
    private double productPrice;
    private boolean productExists;
    private boolean productOutOfStock;

}
