package com.cooperative.service.ch5;

import com.cooperative.dao.ch5.Ch5Dao;
import com.cooperative.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "ch5_user_service")
public class UserServiceImpl {
    @Autowired
    Ch5Dao ch5Dao;

    public User geUserById(Integer id) {
        User user = ch5Dao.findUserById(id);
        return user;
    }
}
