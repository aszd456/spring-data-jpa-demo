package com.cooperative.controller.bili;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            return "上传成功";
        }
        return "上传失败";
    }
}
