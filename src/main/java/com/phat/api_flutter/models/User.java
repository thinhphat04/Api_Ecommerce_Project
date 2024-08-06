package com.phat.api_flutter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {

    @Id
    private String id;

    private String name;

    //tránh bị trùng lặp mail khi đăng ký
    @Indexed(unique = true)
    private String email;

    private String passwordHash;

    private String paymentCustomerId;

    private String street;

    private String apartment;

    private String city;

    private String postalCode;

    private String country;

    private String phone;

    private boolean isAdmin;

    private Integer resetPasswordOtp;

    private Date resetPasswordOtpExpires;

    @DBRef
    private List<CartProduct> cart;


}
