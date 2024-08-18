package com.phat.api_flutter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isAdmin") //fix error dang nhap lai trong flutter
    private boolean isAdmin;
}