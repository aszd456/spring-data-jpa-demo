package com.cooperative.ch7;

import com.cooperative.ch7.conf.EnvConfig;
import com.cooperative.ch7.conf.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("ch7")
public class Ch7Controller {
    @Autowired
    EnvConfig envConfig;
    @Autowired
    ServerConfig serverConfig;

    /**
     * 通过value注解注入配置信息到spring管理的BEAN
     *
     * @param port
     * @return
     */
    @RequestMapping("/showValue.html")
    @ResponseBody
    public String value(@Value("${server.port}") int port) {
        return "port:" + port;
    }


    @RequestMapping("/sayHello.html")
    @ResponseBody
    public String say() {
        log.info("acess");
        return "hello world";
    }


    @RequestMapping("/showEnv.html")
    @ResponseBody
    public String env() {
        return "port:" + envConfig.getServerPort();
    }


    @RequestMapping("/showServer.html")
    @ResponseBody
    public String value() {
        return "port:" + serverConfig.getPort() + " contxtPath:" + serverConfig.getServlet().getPath();
    }
}
