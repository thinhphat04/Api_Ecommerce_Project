package com.phat.api_flutter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartResponse {
    private String id;
    private String product;
    private int quantity;
    private String selectedSize;
    private String selectedColour;
    private String productName;
    private String productImage;
    private double productPrice;
    private boolean reserved;
    private Date reservationExpiry = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
}
