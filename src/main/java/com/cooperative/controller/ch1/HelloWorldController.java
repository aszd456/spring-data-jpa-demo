package com.cooperative.controller.ch1;

import com.cooperative.annotation.CustomFunction;
import jdk.nashorn.internal.objects.annotations.Function;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {


    @RequestMapping("/sayhello.html")
    @CustomFunction()
    @ResponseBody
    public String say(String name) {
        return "hello " + name;
    }
}

