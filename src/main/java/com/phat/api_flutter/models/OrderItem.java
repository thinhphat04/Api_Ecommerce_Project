package com.phat.api_flutter.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
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

    public OrderItem( Product product, String productName, String productImage, double productPrice, int quantity, String selectedSize, String selectedColour) {
        this.product = product;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.selectedSize = selectedSize;
        this.selectedColour = selectedColour;
    }
}
