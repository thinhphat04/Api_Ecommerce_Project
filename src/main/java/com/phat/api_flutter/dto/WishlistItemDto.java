package com.phat.api_flutter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistItemDto {
    public String productId;
    public String productImage;
    public double productPrice;
    public String productName;
    public boolean productExists;
    public boolean productOutOfStock;
}
