package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.models.User;

import java.util.List;

public interface IUserServiceAdmin {

    public long getUsersCount();

    public User findById(String id);
}
