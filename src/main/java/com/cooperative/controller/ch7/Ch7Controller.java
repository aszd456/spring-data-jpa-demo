package com.cooperative.controller.ch7;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("ch7")
public class Ch7Controller {

    /**
     * 通过value注解注入配置信息到spring管理的BEAN
     * @param port
     * @return
     */
    @RequestMapping("/showvalue.html")
    @ResponseBody
    public String value(@Value("${server.port}") int port) {
        return "port:" + port;
    }
}
