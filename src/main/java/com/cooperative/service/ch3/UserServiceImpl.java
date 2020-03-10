package com.cooperative.service.ch3;

import com.cooperative.dao.user.UserDao;
import com.cooperative.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
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

    public User saveUser(User user) {
        return userDao.save(user);
    }

    @Override
    public void updateUser(Integer id, Integer type) {

    }
}
