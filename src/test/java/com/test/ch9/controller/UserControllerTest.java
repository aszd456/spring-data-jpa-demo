package com.test.ch9.controller;

import com.cooperative.ch9.Ch9Controller;
import com.cooperative.entity.user.User;
import com.cooperative.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @Author: zhouliansheng
 * @Date: 2020/8/20 0:02
 */
@RunWith(SpringRunner.class)
//需要模拟测试的Controller
@WebMvcTest(Ch9Controller.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    @Test
    public void testMvc() throws Exception {
        int userId = 10;
        int expectedCredit = 100;
        //模拟userService
        BDDMockito.given(this.userService.getCredit(Mockito.anyInt())).willReturn(100);
        //http 调用
        mvc.perform(MockMvcRequestBuilders.get("/user/{id}", userId))
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(expectedCredit)));
    }

    @Test
    public void updateUser() throws Exception {
        int userId = 1;
        String name = "hilijz";
        int expectedCredit = 100;
        BDDMockito.given(this.userService.updateUser(Mockito.any(User.class))).willReturn(true);
        String path = "$.success";
        mvc.perform(MockMvcRequestBuilders.get("/user/{id}/{name}", userId, name))
                .andExpect(MockMvcResultMatchers.jsonPath(path).value(true));
    }
}
