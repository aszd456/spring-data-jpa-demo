package com.cooperative.service.ch3;

import com.cooperative.entity.user.User;

import java.util.List;

public interface UserService {
    List<User> allUser();

    User getUserById(Integer id);

    void updateUser(Integer id, Integer type);

    User saveUser(User user);
}
