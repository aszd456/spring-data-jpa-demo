package com.cooperative.ch11.entity;

/**
 * @ClassName MongoSequence
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/11/20 11:08
 * @Version 1.0
 **/

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 自增序列维护表
 * @author Administrator
 */
@Data
@Document(collection = "___MongoSequence___")
public class MongoSequence {
    /**
     * 表名做主键
     */
    @Id
    private String id;

    /**
     * 对应表的最新自增id
     */
    private int seq;
}
