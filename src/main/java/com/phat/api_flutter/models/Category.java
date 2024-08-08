package com.phat.api_flutter.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.phat.api_flutter.converters.ObjectIdSerializer;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "categories")
public class Category {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private String name;

    private String colour = "#000000";

    private String image;

    private boolean markedForDeletion = false;
}
