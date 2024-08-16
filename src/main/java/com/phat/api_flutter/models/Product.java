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

    private Category category;

    private GenderAgeCategory genderAgeCategory;
    private int countInStock;
    private Date dateAdded ;

    private int __v;

    public enum GenderAgeCategory {
        men, women, unisex, kids
    }

    public static GenderAgeCategory convertStringtoEnums(String value){
        try {
            return GenderAgeCategory.valueOf(value);

        } catch (IllegalArgumentException e) {
            // Handle the case when the input String doesn't match any enum constant
            // For example, we might want to return a default value or throw a more meaningful exception.
            System.out.println("Invalid GenderAgeCategory: " + value);
            return null;
        }
    }
}
