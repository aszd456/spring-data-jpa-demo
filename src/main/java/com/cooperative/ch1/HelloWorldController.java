package com.cooperative.ch1;

import com.cooperative.ch1.CustomFunction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {


    @RequestMapping("/sayhello.html")
    @CustomFunction("test")
    @ResponseBody
    public String say(String name) {
        return "hello " + name;
    }
}

