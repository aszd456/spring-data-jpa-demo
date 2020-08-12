package com.cooperative.ch7.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {
    @Bean
    public URLTestBean getURLTestBean() {
        return new URLTestBean();
    }
}
