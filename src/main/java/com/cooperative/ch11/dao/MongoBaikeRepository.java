package com.cooperative.ch11.dao;

import com.cooperative.ch11.entity.Baike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName MongoRepository
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/11/19 11:39
 * @Version 1.0
 **/
@Repository
public interface MongoBaikeRepository extends MongoRepository<Baike, String> {

    /**
     * 根据姓名，模糊查询
     * @param userName
     * @param pageable
     * @return
     */
    Page<Baike> findByUserNameLike(String userName, Pageable pageable);

}
