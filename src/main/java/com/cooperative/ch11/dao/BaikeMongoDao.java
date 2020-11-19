package com.cooperative.ch11.dao;

import com.cooperative.ch11.entity.Baike;
import org.springframework.stereotype.Repository;

/**
 * @ClassName BaikeMongoDao
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/11/19 11:03
 * @Version 1.0
 **/
@Repository
public class BaikeMongoDao extends MongoDao<Baike> {

    @Override
    protected Class<Baike> getEntityClass() {
        return Baike.class;
    }
}
