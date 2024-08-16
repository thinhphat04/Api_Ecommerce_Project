package com.phat.api_flutter.dto;

import com.phat.api_flutter.models.WishlistItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private boolean isAdmin;
    private List<WishlistItem> wishlist;
    private Integer resetPasswordOtp;

    // Constructor for the getUserById method
    public UserDTO(String id, String name, String email, String phone, boolean isAdmin, List<WishlistItem> wishlist, Integer resetPasswordOtp) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isAdmin = isAdmin;
        this.wishlist = wishlist;
        this.resetPasswordOtp = resetPasswordOtp;
    }

}