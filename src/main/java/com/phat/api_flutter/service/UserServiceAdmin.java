package com.phat.api_flutter.service;

import com.phat.api_flutter.models.User;
import com.phat.api_flutter.repository.UserRepositoryAdmin;
import com.phat.api_flutter.service.impl.IUserServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceAdmin implements IUserServiceAdmin {
    private final UserRepositoryAdmin userRepositoryAdmin;

    @Autowired
    public UserServiceAdmin(UserRepositoryAdmin userRepositoryAdmin) {
            this.userRepositoryAdmin = userRepositoryAdmin;
        }

    @Override
    public long getUsersCount() {
        return userRepositoryAdmin.count();
    }

    @Override
    public User findById(String id) {
        return userRepositoryAdmin.findById(id).orElse(null);
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userRepositoryAdmin.findUserById(user.getId());
        if(existingUser != null) {
            existingUser.setId(user.getId());
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setPasswordHash(user.getPasswordHash());
            existingUser.setPaymentCustomerId(user.getPaymentCustomerId());
            existingUser.setStreet(user.getStreet());
            existingUser.setApartment(user.getApartment());
            existingUser.setCity(user.getCity());
            existingUser.setPostalCode(user.getPostalCode());
            existingUser.setCountry(user.getCountry());
            existingUser.isAdmin(user.isAdmin());
            existingUser.setResetPasswordOtp(user.getResetPasswordOtp());
            existingUser.setResetPasswordOtpExpires(user.getResetPasswordOtpExpires());
            existingUser.setCart(user.getCart());
            existingUser.setWishlist(user.getWishlist());
            return userRepositoryAdmin.save(existingUser);
        }
        return null;
    }
    @Override
    public  User removeCartItems(String userId, List<String> itemsToRemove){
        // Tìm User bằng userId
        User user = userRepositoryAdmin.findById(userId).orElse(null);
        if(user != null) {
            user.getCart().removeAll(itemsToRemove);
            return userRepositoryAdmin.save(user);
        }
        // Lưu lại User với cart đã cập nhật
        return null;
    }

    @Override
    public User deleteById(String id) {
        User user = userRepositoryAdmin.findById(id).orElse(null);
        if(user != null) {
            userRepositoryAdmin.deleteById(id);
            return user;
        }
        return null;
    }
}
