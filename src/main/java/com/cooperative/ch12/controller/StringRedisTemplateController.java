package com.cooperative.ch12.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @Author: zhouliansheng
 * @Date: 2020/11/27 22:07
 */
@RestController
@RequestMapping("/strkeyredis")
public class StringRedisTemplateController {

    @Autowired
    private StringRedisTemplate redisClient;

    @GetMapping("/setget")
    public String env(@RequestParam(defaultValue = "123") String param) {
        redisClient.opsForValue().set("testEnv", param);
        String str = redisClient.opsForValue().get("testEnv");
        return str;
    }

    @GetMapping("/addMessage")
    public String addMessage() {
        redisClient.opsForList().leftPush("platform:message", "hello,xiandafu");
        redisClient.opsForList().leftPush("platform:message", "hello,spring boot");
        return "success";
    }

    @GetMapping("/readMessage")
    public String readMessage() throws Exception {
        String str = redisClient.opsForList().leftPop("platform:message");
        return str;
    }

    @GetMapping("/addCache")
    public String addCache(String key, String value) throws Exception {
        redisClient.opsForHash().put("cache", key, value);
        return "success";
    }

    @GetMapping("/getCache")
    public String getCache(String key) throws Exception {
        String str = (String) redisClient.opsForHash().get("cache", key);
        return str;
    }

    @GetMapping("/boundValue")
    public String boundValue(String key) throws Exception {
        BoundListOperations<String, String> operations = redisClient.boundListOps(key);
        operations.leftPush("a");
        operations.leftPush("b");
        return String.valueOf(operations.size());

    }

//    @GetMapping("/connectionSet")
//    public String connectionSet(final String key, final String value) {
//        redisClient.execute((RedisCallback) connection -> {
//            connection.set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
//            return null;
//        });
//        return "success";
//    }

}
