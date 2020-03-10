package com.cooperative.controller.ch3;

import com.cooperative.entity.user.User;
import com.cooperative.service.ch3.UserService;
import com.cooperative.service.ch3.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/urlmapper")
public class URLMapperController {

    @Autowired
    UserService userService;

    @GetMapping("saveUser")
    @ResponseBody
    public User saveUser(){
        User user = new User();
        user.setName("莎莉B");
        user.setPassword("123456");
        user.setCreateTime(new Date());
        return userService.saveUser(user);
    }

    @RequestMapping(path = "/user/all/*.json", method = RequestMethod.POST)
    @ResponseBody
    public List<User> allUser() {
        return userService.allUser();
    }

    @RequestMapping(path = "/user/{id}.json", method = RequestMethod.GET)
    @ResponseBody
    public User getById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping(path = "/{userId}.json", produces = "application/json")
    @ResponseBody
    public User getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/consumes/test.json", consumes = "application/json")
    @ResponseBody
    public User forJson() {
        return userService.getUserById(1);
    }
}
