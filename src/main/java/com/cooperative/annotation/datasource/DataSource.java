package com.cooperative.annotation.datasource;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    String value();

    /**
     * 默认数据库
     **/
    String core = "core";
    /**
     * OA数据库
     **/
    String oa = "oa";
    /**
     * 云饭堂数据库
     **/
    String ft = "ft";
}
