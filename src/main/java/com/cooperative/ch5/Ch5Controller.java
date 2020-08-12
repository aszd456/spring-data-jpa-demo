package com.cooperative.ch5;

import com.cooperative.entity.user.User;
import com.cooperative.service.ch5.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("ch5")
public class Ch5Controller {

    @Autowired
    @Qualifier("ch5_user_service")
    UserServiceImpl userService;

    @RequestMapping("/user/{id}")
    public @ResponseBody
    User say(@PathVariable Integer id) {
        User user = userService.geUserById(id);
        return user;
    }
}
