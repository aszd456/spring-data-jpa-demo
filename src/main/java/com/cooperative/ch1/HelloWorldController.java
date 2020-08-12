package com.cooperative.ch1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhouliansheng
 */
@Controller
public class HelloWorldController {


    @RequestMapping("/sayhello.html")
    @ResponseBody
    @CustomFunction("test")
    public String say(String name) {
        return "hello " + name;
    }
}

