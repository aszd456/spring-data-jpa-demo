package com.cooperative.ch7.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @ConditionalOnBean(xxx.class) 在当前上下文存在某个对象时，才会实例化一个bean
 * @ConditionalOnMissingBean(xxx.class，...) 当前上下文不存在某个对象时，才会实例化一个bean
 * @ConditionalOnClass(XXX.class) 表示当classpath有指定的类时，配置生效
 * @ConditionalOnProperty(name="message.cnter.enabled",havingValue="true", matchIfMissing=true)
 *      该注解根据name来读取spring boot 的Environment的变量包含的属性，根据其值与havingValue的值比较结果决定配置是否生效
 *      如果没有指定havingValue，只要属性不为false,配置都能生效
 *      matchIfMissing为true意味着如果Environment没有包含“message.center.enabled”,配置也能生效，默认为false
 * @ConditionalOnExpression 当表达式为true，才会实例化一个bean，支持spel
 * @ConditionalOnJava ,当存在指定的java版本的时候
 */
@Configuration
@ConditionalOnBean(DataSource.class)
public class SqlConfig {

    /**
     * 如果没有配置过URLTestBean，则调用myService
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public URLTestBean myService(){
        return new URLTestBean();
    }
}
