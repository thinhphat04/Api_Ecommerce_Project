package com.phat.api_flutter.dto;

import com.phat.api_flutter.models.CartProduct;
import com.phat.api_flutter.models.WishlistItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserDTO {
    private String id;
    private String _id;
    private String name;
    private String email;
    private String passwordHash;
    private String phone;
    private boolean isAdmin;
    private List<CartProduct> cart;
    private List<WishlistItem> wishlist;


}