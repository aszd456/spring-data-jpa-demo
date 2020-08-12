package com.cooperative.service;

import com.cooperative.dao.BillDataDao;
import com.cooperative.entity.BillData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @ClassName BillDataService
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/6/28 11:35
 * @Version 1.0
 **/
@Slf4j
@Service
@Transactional
public class BillDataService {

    @Autowired
    BillDataDao billDataDao;

    public BillData saveBillData(BillData data) {
        return billDataDao.save(data);
    }

    public BillData findByWechatOrderNo(String wechatOrderNo) {
        return billDataDao.findByWechatOrderNo(wechatOrderNo);
    }
}
