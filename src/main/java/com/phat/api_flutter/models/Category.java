package com.phat.api_flutter.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "category")
public class Category {

    @Id
    private String id;

    private String name;

    private String colour = "#000000";

    private String image;

    private boolean markedForDeletion = false;
}
