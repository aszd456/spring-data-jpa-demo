package com.cooperative.ch11;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

/**
 * 此类若不加，那么插入的一行会默认添加一个_class字段来存储实体类类型 如（com.example.demo.entity.Student）
 * @ClassName ApplicationReadyListener
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/11/19 9:34
 * @Version 1.0
 **/
@Configuration
public class ApplicationReadyListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    MongoTemplate oneMongoTemplate;

    private static final String TYPEKEY = "_class";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        MongoConverter converter = oneMongoTemplate.getConverter();
        if (converter.getTypeMapper().isTypeKey(TYPEKEY)) {
            ((MappingMongoConverter) converter).setTypeMapper(new DefaultMongoTypeMapper(null));
        }
    }
}
