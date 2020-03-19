package com.cooperative.conf.ch7;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
public class MobileEncryptCondition {
    @Bean
    @Conditional(MobileEncryptCondition.EncryptCondition.class) //@Conditional按照条件注册bean
    public MobileEncryptBean mobileEncryptBean() {
        return new MobileEncryptBean();
    }

    static class EncryptCondition implements Condition {

        public boolean matches(ConditionContext ctx, AnnotatedTypeMetadata metadata) {

            //获取ioc使用的beanFactory
            ConfigurableListableBeanFactory beanFactory = ctx.getBeanFactory();
            //获取类加载器
            ClassLoader classLoader = ctx.getClassLoader();
            //获取当前环境信息
            Environment environment = ctx.getEnvironment();
            //获取bean定义的注册类
            BeanDefinitionRegistry registry = ctx.getRegistry();


            Resource res = ctx.getResourceLoader().getResource("encrypt.txt");
            Environment env = ctx.getEnvironment();
            return res.exists() && env.containsProperty("mobile.encrypt.enable");
        }

    }
}
