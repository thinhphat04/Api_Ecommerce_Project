package com.phat.api_flutter.dto;


import com.phat.api_flutter.models.User;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private User user;
    private String accessToken;
    private String refreshToken ;

    public AuthenticationResponse(User user, String accessToken, String refreshToken) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
