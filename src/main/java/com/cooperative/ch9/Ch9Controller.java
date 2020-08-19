package com.cooperative.ch9;

import com.cooperative.entity.user.User;
import com.cooperative.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhouliansheng
 * @Date: 2020/8/19 23:24
 */
@Slf4j
@Controller
@RequestMapping("/ch9")
public class Ch9Controller {
    @Autowired
    UserService userService;

    @RequestMapping("/user/{id}")
    @ResponseBody
    public String getUser(@PathVariable Integer id) {
        return String.valueOf(userService.getCredit(id));
    }

    @RequestMapping("/user/{id}/{name}")
    @ResponseBody
    public String updateUser(@PathVariable Integer id, @PathVariable String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        userService.updateUser(user);
        return "{\"success\":true}";
    }
}
