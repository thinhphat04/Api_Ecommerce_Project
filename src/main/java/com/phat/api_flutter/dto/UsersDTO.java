package com.phat.api_flutter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UsersDTO {
    private String id;
    private String _id;
    private String name;
    private String email;
    private boolean isAdmin;

    // Constructor for the getUsers method
    public UsersDTO(String id, String _id, String name, String email, boolean isAdmin) {
        this.id = id;
        this._id = _id;
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;
    }
}