package com.cooperative.ch11.service;

import com.cooperative.ch11.dao.BaikeMongoDao;
import com.cooperative.ch11.entity.Baike;
import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName BaikeService
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/11/19 9:38
 * @Version 1.0
 **/
@Service
public class BaikeService {

    private static final Logger logger = LoggerFactory.getLogger(BaikeService.class);
    @Autowired
    private BaikeMongoDao dao;

    public Baike findById(String id) {
        return dao.findById(id);
    }

    public Baike insert(Baike baike) {
        return dao.insert(baike);
    }

    public List<Baike> queryBad(Criteria criteria) {
        return dao.queryListByCriteria(Query.query(criteria));
    }

    public List<Baike> queryComment(Criteria criteria1, Criteria criteria2) {
        return dao.queryListByCriteria(Query.query(criteria1.andOperator(criteria2)));
    }

    public UpdateResult updateMulti(Criteria criteria, Update update) {
        return dao.updateMulti(Query.query(criteria),update);
    }

    public void save(Baike baike) {
        dao.save(baike);
    }

    public void remove(String id) {
        dao.deleteById(id);
    }
}
