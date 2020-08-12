package com.cooperative.dao;

import com.cooperative.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "wx_userDao")
public interface UserDao extends JpaRepository<User, Long> {

    /**
     * 自定义sql写法
     * nativeQuery=true 为sql写法，否则是jpql
     * @param openId
     * @return
     */
    @Query(nativeQuery = true,value = "select * from user u where u.open_id=:openId")
    User getUserByOpenId(@Param(value = "openId") String openId);

    /**
     * 基于方法名字查询，详情看spring-data-jpa教程
     * @param openId
     * @return
     */
    User findByOpenId(String openId);
}
