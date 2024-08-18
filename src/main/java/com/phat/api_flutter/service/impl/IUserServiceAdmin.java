package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.models.User;

import java.util.List;

public interface IUserServiceAdmin {

     long getUsersCount();

     User findById(String id);

     User updateUser(User user);

     User removeCartItems(String userId, List<String> itemsToRemove);

     User deleteById(String id);
}
