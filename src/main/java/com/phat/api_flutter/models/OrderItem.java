package com.phat.api_flutter.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orderItem")
public class OrderItem {

    @Id
    private String id;

    @DBRef
    private Product product;

    private String productName;
    private String productImage;
    private double productPrice;
    private int quantity = 1;
    private String selectedSize;
    private String selectedColour;
}
