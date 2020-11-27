package com.cooperative.ch7.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
public class MyConfiguration {
    @Bean
    public URLTestBean getURLTestBean() {
        return new URLTestBean();
    }
}
