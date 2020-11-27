package com.cooperative.ch12.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhouliansheng
 * @Date: 2020/11/28 1:28
 */
@Controller
@RequestMapping("/redisKey")
public class StrKeyRedisTemplateController {

    @Autowired
    @Qualifier("strKeyRedisTemplate")
    private RedisTemplate redisClient;

    @RequestMapping("/simpleSet")
    @ResponseBody
    public String simpleSet() {
        redisClient.opsForValue().set("key-0", "hello");
        return "success";
    }

    @RequestMapping("/simpleGet")
    @ResponseBody
    public String simpleGet() {
        String value = (String) redisClient.opsForValue().get("key-0");
        return value;

    }
}
