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
}
