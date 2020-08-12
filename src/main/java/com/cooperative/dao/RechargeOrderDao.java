package com.cooperative.dao;

import com.cooperative.entity.RechargeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeOrderDao extends JpaRepository<RechargeOrder, Long> {

    RechargeOrder findByTransactionId(String transactionId);
}
