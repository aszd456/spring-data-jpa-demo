package com.cooperative.ch11.controller;

import com.cooperative.ch11.dao.MongoBaikeRepository;
import com.cooperative.ch11.entity.Baike;
import com.cooperative.ch11.entity.Comment;
import com.cooperative.ch11.service.BaikeService;
import com.google.common.collect.Lists;
import com.mongodb.client.result.UpdateResult;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhouliansheng
 * @Date: 2020/11/19 0:45
 */
@RestController
@RequestMapping("mg")
public class MongoController {
    @Autowired
    private BaikeService service;

    @Autowired
    private MongoBaikeRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/baike/{id}")
    public Baike findUser(@PathVariable String id) {
        Baike baike = service.findById(id);
        return baike;
    }

    @GetMapping("/baike/add")
    public Baike addBaike() {
        Baike baike = new Baike();
        baike.setId("mybatis");
        baike.setDesc("一款优秀的持久层框架");
        baike.setCrateDate(new Date());
        baike.setStatus(1);
        baike.setTag(Arrays.asList("Mybatis", "Mybatis-plus"));
        Comment comment = new Comment();
        comment.setGood(100.0);
        comment.setBad(2.00);
        baike.setComment(comment);
        return service.insert(baike);
    }

    @GetMapping("/querybad/{bad}")
    public List<Baike> queryBad(@PathVariable int bad) {
        Criteria criteria = Criteria.where("comment.bad").gt(bad);
        List<Baike> list = service.queryBad(criteria);
        return list;
    }

    @GetMapping("/querybad/{good}/{bad}")
    public List<Baike> queryComment(@PathVariable("good") int good, @PathVariable("bad") int bad) {
        Criteria criteria1 = Criteria.where("comment.bad").gt(bad);
        Criteria criteria2 = Criteria.where("comment.good").lt(good);
        List<Baike> list = service.queryComment(criteria1, criteria2);
        return list;
    }

    @GetMapping("/baike/tag/{tag}")
    public @ResponseBody
    String addOne(@PathVariable String tag){
        Criteria criteria = Criteria.where("tag").in(tag);
        Update update = new Update();
        //自增
        update.inc("comment.good", 1);
        UpdateResult result = service.updateMulti(criteria, update);
        return "成功修改 "+result.getModifiedCount() ;
    }
    @GetMapping("/baike/tag/{tag}/{pageNum}")
    public List<Baike> findBaike(@PathVariable String tag,@PathVariable int pageNum){
        Criteria criteria = Criteria.where("tag").in(tag);
        Query query =  Query.query(criteria);
        //查询总数
        long totalCount = mongoTemplate.count(query, Baike.class);
        //每页个数
        int numOfPage = 10;
        //计算总数
        long totalPage = totalCount%numOfPage==0?(totalCount/numOfPage):(totalCount/numOfPage+1);

        int skip = (pageNum-1)*numOfPage;
        query.skip(skip).limit(numOfPage);
        List<Baike> list = mongoTemplate.find(query, Baike.class);
        return list;
    }


    @GetMapping("/updatebaike")
    public Baike updateDict(Baike baike){
        baike.setUpdateDate(new Date());
        service.save(baike);
        return baike ;
    }

    @GetMapping("/deletebaike/{id}")
    public void deleteDict(String id){
        service.remove(id);
    }



}
