package com.phat.api_flutter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//login dto
@Data
public class AuthenticationRequest {
//    Day la cua Phat
//    private String username;
    @NotBlank
    private String email;

    private String name;
    @NotBlank
    private String password;

    private String phone;


}
