package com.cooperative.ch3.controller;

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
    public User saveUser() {
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
        User user = userService.getUserById(id);
        return user;
    }

    /**
     * produces：它的作用是指定返回值类型，不但可以设置返回值类型还可以设定返回值的字符编码；
     * application/json;charset=utf-8
     *使用@responseBody可以忽略
     * @param userId
     * @return
     */
    @GetMapping(path = "/{userId}.json", produces = "application/json")
    @ResponseBody
    public User getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    /**
     * consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
     * 方法仅处理request Content-Type为“application/json”类型的请求
     * @return
     */
    @GetMapping(value = "/consumes/test.json", consumes = "application/json")
    @ResponseBody
    public User forJson() {
        return userService.getUserById(1);
    }
}
