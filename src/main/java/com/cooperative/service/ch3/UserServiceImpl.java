package com.cooperative.service.ch3;

import com.cooperative.dao.user.UserDao;
import com.cooperative.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public List<User> allUser() {
        return userDao.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        return userDao.getOne(id);
    }

    @Override
    public void updateUser(Integer id, Integer type) {

    }
}
