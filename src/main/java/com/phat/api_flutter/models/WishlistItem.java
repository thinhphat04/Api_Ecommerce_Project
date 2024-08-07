package com.phat.api_flutter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class WishlistItem {
    private String productId;
    private String productName;
    private String productImage;
    private double productPrice;
}