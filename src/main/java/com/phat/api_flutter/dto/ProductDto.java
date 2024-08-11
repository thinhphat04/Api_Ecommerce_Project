package com.phat.api_flutter.dto;

import com.phat.api_flutter.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private double price;
    private double rating = 0.0;
    private List<String> colours;
    private String image;
    private int numberOfReviews = 0;
    private String category;
    private Product.GenderAgeCategory genderAgeCategory;
    private int countInStock;
    private Date dateAdded = new Date();
    public enum GenderAgeCategory {
        men, women, unisex, kids
    }
}