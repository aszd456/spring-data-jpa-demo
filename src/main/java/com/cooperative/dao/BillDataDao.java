package com.cooperative.dao;


import com.cooperative.entity.BillData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDataDao extends JpaRepository<BillData, Long> {

    BillData findByWechatOrderNo(String wechatOrderNo);
}
