package com.cooperative.conf.ch7;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {
    @Bean
    public URLTestBean getURLTestBean() {
        return new URLTestBean();
    }
}
