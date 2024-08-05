package com.phat.api_flutter.models;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "review")
public class Review {

    @Id
    private String id;

    @DBRef
    private User user;

    private String userName;

    private String comment;

    private double rating;

    private Date date = new Date();
}
