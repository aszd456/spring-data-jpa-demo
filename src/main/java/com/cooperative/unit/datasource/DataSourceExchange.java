package com.cooperative.unit.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @ClassName DataSourceExchange
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/8/1 10:04
 * @Version 1.0
 **/

@Slf4j
@Component
public class DataSourceExchange implements MethodBeforeAdvice, AfterReturningAdvice {

    /**
     * 方法结束后
     */
    @Override
    public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
        DynamicDataSource.clearDataSource();
        log.info("数据源已移除！");

    }

    /**
     * 拦截目标方法，获取由@DataSource指定的数据源标识，设置到线程存储中以便切换数据源
     */
    @Override
    public void before(Method method, Object[] arg1, Object arg2) throws Throwable {
        if (method.isAnnotationPresent(DataSource.class)) {
            DataSource dataSource = method.getAnnotation(DataSource.class);
            DynamicDataSource.setDataSource(dataSource.value());
            log.info("数据源切换至：" + DynamicDataSource.getDataSource());
        }

    }


}
