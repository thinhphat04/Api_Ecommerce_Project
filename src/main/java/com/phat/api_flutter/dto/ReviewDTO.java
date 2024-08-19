package com.phat.api_flutter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
//    @JsonProperty("user")
    private String user;
    private String productId;
    private String comment;
    private int rating;
}