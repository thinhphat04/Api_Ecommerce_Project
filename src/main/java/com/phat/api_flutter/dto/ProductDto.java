package com.phat.api_flutter.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.phat.api_flutter.converters.ObjectIdSerializer;
import com.phat.api_flutter.models.Category;
import com.phat.api_flutter.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private String name;
    private String description;
    private double price;
    private double rating = 0.0;
    private List<String> colours;
    private String image;
    private int numberOfReviews = 0;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId category;
    private Product.GenderAgeCategory genderAgeCategory;
    private int countInStock;
    private Date dateAdded = new Date();
    public enum GenderAgeCategory {
        men, women, unisex, kids
    }
}