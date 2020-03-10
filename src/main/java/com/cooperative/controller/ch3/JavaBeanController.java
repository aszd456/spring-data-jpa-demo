package com.cooperative.controller.ch3;

import com.cooperative.controller.ch3.form.OrderPostForm;
import com.cooperative.entity.user.User;
import com.cooperative.service.ch3.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/javabean")
public class JavaBeanController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/update.json")
    @ResponseBody
    public String updateUser(User user) {
        System.out.println(user.getName());
        System.out.println(user.getId());
        return "success";
    }

    @GetMapping(path = "/update2.json")
    @ResponseBody
    public String updateUser2(Integer id, String name) {
        System.out.println(id);
        System.out.println(name);
        return "success";
    }


    @GetMapping(path = "/update3.json")
    @ResponseBody
    public String updateUser3(@RequestParam(name = "id", required = true) Integer id, String name) {
        System.out.println(id);
        System.out.println(name);
        return "success";
    }

    @PostMapping(path = "/saveOrder.json")
    @ResponseBody
    public String saveOrder(OrderPostForm form) {
        return "success";
    }

    @PostMapping(path = "/savejsonorder.json")
    @ResponseBody
    public String saveOrderByJson(@RequestBody User user) {
        return user.getName();
    }
}
