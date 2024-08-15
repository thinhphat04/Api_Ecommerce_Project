package com.phat.api_flutter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cartproducts")
public class CartProduct {

    @Id
    private String id;

    private String product;

    private int quantity = 1;
    private String selectedSize;
    private String selectedColour;
    private String productName;
    private String productImage;
    private double productPrice;
    private Date reservationExpiry = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
    private boolean reserved = true;
}
