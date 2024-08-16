package com.phat.api_flutter.models;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@Document(collection = "tokens")
public class Token {

    @Id
    private String id;
    private String userId;
    private String refreshToken;
    private String accessToken;
    private Date createdAt = new Date();

    public Token(String id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
