package com.phat.api_flutter.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistItem {
    @NotEmpty(message = "Product ID cannot be empty")
    public String productId;
    public String productName;
    public String productImage;
    public double productPrice;
}