package com.cooperative.ch11.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhouliansheng
 * @Date: 2020/11/19 0:42
 * @Document(collection = "test")指定集合，不指定会新建跟类名一样的集合baike
 * @CompoundIndex(def = "{'userId':1,'desc':-1}") 复合索引
 * @
 */
@Data
@Document(collection = "test")
public class Baike {
    /*** 自定义mongo主键 加此注解可自定义主键类型以及自定义自增规则
     *  若不加 插入数据数会默认生成 ObjectId 类型的_id 字段
     *  org.springframework.data.annotation.Id 包下
     *  mongo库主键字段还是为_id 。不必细究(本文实体类中为id）
     */
    @Id
    private String id;
    private String desc;
    private List<String> tag = new ArrayList<String>();
    private Comment comment = null;
    private Date crateDate = null;
    private Date updateDate = null;
    private int status = 0;
    private String userName;

    /**
     * 索引
     */
    @Indexed
    private String userId;
}
