package com.phat.api_flutter.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "categories")
public class Category {
    @Id
//    @JsonSerialize(using= ToStringSerializer.class)
    private String _id;

    private String name;

    private String colour = "#000000";

    private String image;

    private boolean markedForDeletion = false;
}
