package com.phat.api_flutter.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order")
public class Order {

    @Id
    private String id;

    @DBRef
    private List<OrderItem> orderItems;

    private String shippingAddress;
    private String city;
    private String postalCode;
    private String country;
    private String phone;
    private String paymentId;

    private String status = "pending";
    private List<String> statusHistory = List.of("pending");

    private Double totalPrice;

    @DBRef
    private User user;

    private Date dateOrdered = new Date();
}
