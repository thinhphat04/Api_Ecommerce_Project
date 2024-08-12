package com.phat.api_flutter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private boolean isAdmin;
}