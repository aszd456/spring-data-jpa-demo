package com.test.ch9.service;

import com.cooperative.entity.user.User;
import com.cooperative.service.CreditSystemService;
import com.cooperative.service.user.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author: zhouliansheng
 * @Date: 2020/8/19 23:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    private CreditSystemService creditSystemService;

    @Test
    public void testService() {


        int userId = 10;
        int expectedCredit = 100;
        BDDMockito.given(this.creditSystemService.getUserCredit(ArgumentMatchers.anyInt())).willReturn(expectedCredit);
        int credit = userService.getCredit(10);
        Assert.assertEquals(expectedCredit, credit);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(1);
        user.setName("ok22223343");
        boolean ret = userService.updateUser(user);
        Assert.assertTrue(ret);

    }
}
