package com.phat.api_flutter.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "token")
public class Token {

    @Id
    private String id;
    private String userId;
    private String refreshToken;
    private String accessToken;
    private Date createdAt = new Date();
}
