package com.cooperative.controller.bili;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName BiliController
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/5/27 16:24
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("bili")
public class BiliController {

    @Autowired
    ObjectMapper mapper;

    @GetMapping("/getNavList")
    public String getNavList() throws JsonProcessingException {
        List<String> navList = Lists.newLinkedList();
        navList.add("首页");
        navList.add("动画");
        navList.add("番剧");
        navList.add("国创");
        navList.add("音乐");
        return mapper.writeValueAsString(navList);
    }
}
