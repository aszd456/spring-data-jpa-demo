package com.test.ch9.db;

import com.cooperative.dao.user.UserDao;
import com.cooperative.entity.user.User;
import com.cooperative.service.user.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: zhouliansheng
 * @Date: 2020/8/19 23:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserDbTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Test
    @Sql({"classpath:com/test/ch9/db/user.sql"}) //初始化一条主键为1的用户数据
    public void upateNameTest() {
        User user = new User();
        user.setId(1);
        user.setName("hello123");
        boolean success = userService.updateUser(user);
        User dbUser = userDao.getOne(1);
        Assert.assertEquals(dbUser.getName(), "hello123");

    }
}
