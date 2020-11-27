package com.cooperative.ch12.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: zhouliansheng
 * @Date: 2020/11/28 1:11
 */
@Controller
@RequestMapping("/redis")
public class RedisTemplateController {

    /**
     * 必须使用@Qualifier("redisTemplate")，否则springBoot会误认为可能是StringRedisTemplate
     */
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisClient;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/simpleSet")
    @ResponseBody
    public String simpleSet() {
        redisClient.opsForValue().set("key-0", "hello");
        redisClient.opsForValue().set("key-1", User.getSampleUser());
        return "success";
    }

    @RequestMapping("/simpleGet")
    @ResponseBody
    public String simpleGet() throws JsonProcessingException {
        String value = (String) redisClient.opsForValue().get("key-0");
        User uer = (User) redisClient.opsForValue().get("key-1");
        return "value:"+value+"~~"+"user:"+objectMapper.writeValueAsString(uer);

    }

    public static class User implements java.io.Serializable {
        int id;
        String name;
        Date date = new Date();

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public static User getSampleUser() {
            User user = new User();
            user.setId(123);
            user.setName("abc");
            return user;
        }

    }
}
