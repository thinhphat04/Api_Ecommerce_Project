package com.phat.api_flutter.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
//    @JsonSerialize(using= ToStringSerializer.class)
    private String id;

    private String name;
    private String description;
    private double price;
    private double rating = 0.0;
    private List<String> colours;
    private String image;
    private List<String> images;

    private List<String> reviews;
    private int numberOfReviews = 0;
    private List<String> sizes;

    private String category;

    private GenderAgeCategory genderAgeCategory;
    private int countInStock;
    private Date dateAdded ;

    private int __v;

    public enum GenderAgeCategory {
        men, women, unisex, kids
    }
}
