package com.cooperative.service;


import com.cooperative.dao.RechargeOrderDao;
import com.cooperative.entity.RechargeOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class RechargeService {
    @Autowired
    RechargeOrderDao orderDao;

    public RechargeOrder saveRechargeOrder(RechargeOrder order) {
        return orderDao.save(order);
    }


    public RechargeOrder getRechargeOrderById(Long id) {
        return orderDao.getOne(id);
    }

    public RechargeOrder findByTransactionId(String transactionId) {
        return orderDao.findByTransactionId(transactionId);
    }
}
