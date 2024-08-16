package com.phat.api_flutter.controllers;

import com.phat.api_flutter.dto.UpdateUserDTO;
import com.phat.api_flutter.dto.UserDTO;
import com.phat.api_flutter.dto.UsersDTO;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    //get user by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            Optional<User> userOptional = userService.getUserById(id);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            User user = userOptional.get();
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.isAdmin(),
                    user.getWishlist(),
                    user.getResetPasswordOtp()
            );
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get all users
    @GetMapping("/")
    public ResponseEntity<?> getUsers() {
        try {
            List<User> users = userService.getUsers();
            List<UsersDTO> userDTOs = users.stream()
                    .map(user -> new UsersDTO(
                            user.getId(),
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.isAdmin()
                    ))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User userRequest) {
        try {
            Optional<User> userOptional = userService.getUserById(id);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            User existingUser = userOptional.get();
            existingUser.setName(userRequest.getName());
            existingUser.setEmail(userRequest.getEmail());
            existingUser.setPhone(userRequest.getPhone());
            // Ensure isAdmin remains false, as requested
            existingUser.setAdmin(false);

            // Save the updated user
            User updatedUser = userService.saveUser(existingUser);

            // Create the UpdateUserDTO object to return
            UpdateUserDTO updateUserDTO = new UpdateUserDTO(
                    updatedUser.getId(),
                    updatedUser.getId(),  // Assuming _id is the same as id
                    updatedUser.getName(),
                    updatedUser.getEmail(),
                    updatedUser.getPasswordHash(),
                    updatedUser.getPhone(),
                    updatedUser.isAdmin(),
                    updatedUser.getCart(),
                    updatedUser.getWishlist()
            );

            return new ResponseEntity<>(updateUserDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}