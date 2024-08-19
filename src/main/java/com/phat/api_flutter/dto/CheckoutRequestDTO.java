package com.phat.api_flutter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequestDTO {
    private List<CartItemDTO> cartItems;
    private String theme;
}