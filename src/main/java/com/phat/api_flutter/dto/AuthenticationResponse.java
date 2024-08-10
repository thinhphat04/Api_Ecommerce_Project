package com.phat.api_flutter.dto;

import com.phat.api_flutter.models.CartProduct;
import com.phat.api_flutter.models.WishlistItem;
import com.phat.api_flutter.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private boolean isAdmin;
    private List<WishlistItem> wishlist;
    private String accessToken;
    private String refreshToken;

    public AuthenticationResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.isAdmin = user.isAdmin();
        this.wishlist = user.getWishlist();
    }

    public AuthenticationResponse(User user, String accessToken, String refreshToken) {
        this(user);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}