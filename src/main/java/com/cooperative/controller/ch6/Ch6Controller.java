package com.cooperative.controller.ch6;

import com.cooperative.entity.user.User;
import com.cooperative.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/ch6")
public class Ch6Controller {
    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper mapper;

    @RequestMapping("/finduser.html")
    public @ResponseBody
    String findUser(int userId) throws JsonProcessingException {
        User user =  userService.findUser(userId);
        return mapper.writeValueAsString(user);
    }
    @RequestMapping("/adduser.html")
    public @ResponseBody String addUser(@RequestBody User user){
        int id =   userService.addUser(user);
        return String.valueOf(id);
    }

    /**
     *
     * @param page 0开始
     * @param size
     * @return
     */
    @RequestMapping("/alluser.html")
    public @ResponseBody String alluser(int page,int size) throws JsonProcessingException {
        List<User> list =  userService.getAllUser(page,size);
        return mapper.writeValueAsString(list);
    }

    @RequestMapping("/getuser.html")
    public @ResponseBody String getUser(String name) throws JsonProcessingException {
        User user=  userService.getUser(name);
        return mapper.writeValueAsString(user);
    }

    @RequestMapping("/getdepartuser.html")
    public @ResponseBody String getDepartmentUser(String name,Integer deptId) throws JsonProcessingException {
        User user=  userService.getUser(name, deptId);
        return user==null?"":mapper.writeValueAsString(user);
    }

    @RequestMapping("/pagequery.html")
    public @ResponseBody String pageQuery(Integer deptId,int page,int size) throws JsonProcessingException {
        PageRequest pr = PageRequest.of(page,size);
//		Page<User> users =  userService.queryUser(deptId, pr);
        Page<User> users =  userService.queryUser2(deptId, pr);
        return mapper.writeValueAsString(users);
    }

    @RequestMapping("/example.html")
    public @ResponseBody String example(String name) throws JsonProcessingException {
        List<User> users = userService.getByExample(name);
        return mapper.writeValueAsString(users);
    }


    @RequestMapping("/test.html")
    public @ResponseBody String test(){
        userService.updateUser();
        return "success";
    }
}
