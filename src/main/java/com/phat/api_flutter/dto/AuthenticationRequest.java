package com.phat.api_flutter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthenticationRequest {
//    Day la cua Phat
//    private String username;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
