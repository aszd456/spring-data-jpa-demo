package com.cooperative.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

public class ConditionalController {
/**
 * @Conditional 注解详解
 *
 * 一，都可以应用在 TYPE 上，也就是说，Spring 自动扫描的一切类 (@Configuration, @Component, @Service, @Repository, or @Controller)
 *      都可以通过添加相应的 @ConditionalOnXxxx 来判断是否加载
 * 二，都可以应用在 METHOD 上，所以有 @Bean 标记的方法也可以应用这些注解
 *
 */
    /**
     * @ConditionalOnProperty
     * 下例解析：application.properties 或 application.yml 文件中 mybean.enable 为 true 才会加载 MyCondition 这个 Bean，
     *          如果没有匹配上也会加载，因为 matchIfMissing = true，默认值是 false。
     */
//    @Configuration
//    @ConditionalOnProperty(value = "mybean.enabled", havingValue = "true", matchIfMissing = true)
    public class MyBean{}

    /**
     * @ConditionalOnBean 和 ConditionalOnMissingBean
     * 有时候我们需要某个 Bean 已经存在应用上下文时才会加载，那么我们会用到 @ConditionalOnBean 注解:
     * 与之相反，有时候我们需要某个 Bean 不存在于应用上下文时才会加载，那么我们会用到 @ConditionalOnMissingBean 注解
     *
     */


    /**
     * @ConditionalOnClass 和 @ConditionalOnMissingClass
     * 和上面的一样，只不过判断某个类是否存在于 classpath 中，这就不做过多说明了
     *
     */

    /**
     * @ConditionalOnExpression
     *只有当两个属性都为 true 的时候才加载 MyModule
     *
     */
    @ConditionalOnExpression("${mybean.enabled:true} and ${otherbean.enabled:true}")
    class Mybean2{}
}
