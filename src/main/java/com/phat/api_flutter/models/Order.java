package com.phat.api_flutter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @DBRef
    private List<OrderItem> orderItems;

    private String shippingAddress1;
    private String city;
    private String postalCode;
    private String country;
    private String phone;
    private String paymentId;
    private Status status = Status.pending;
    private List<Status> statusHistory = List.of(Status.pending);
    private double totalPrice;

    @DBRef
    private User user;
    private Date dateOrdered = new Date();

    public enum Status {
        pending, processed, shipped, out_for_delivery, delivered, cancelled, on_hold, expired
    }


}
