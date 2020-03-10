package com.cooperative.service.ch3;

import com.cooperative.entity.user.User;

import java.util.List;

public interface UserService {
    public List<User> allUser();

    public User getUserById(Integer id);

    public void updateUser(Integer id, Integer type);
}
