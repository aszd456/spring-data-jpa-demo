package com.cooperative.entity;

import com.cooperative.unit.wxpay.RechargeStatus;
import com.cooperative.unit.wxpay.RechargeTradeType;
import com.cooperative.unit.wxpay.RefundType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "recharge_order")
public class RechargeOrder extends BaseMode implements Serializable {

    private static final long serialVersionUID = -786171225081385125L;

    public RechargeOrder() {
    }

    @Column(name = "user_id")
    private Long userId;

    /**
     * 支付类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "recharge_trade_type")
    private RechargeTradeType rechargeTradeType;

    /**
     * 支付状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "recharge_status")
    private RechargeStatus rechargeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_type")
    private RefundType refundType;

    /**
     * 充值金额
     */
    @Column(nullable = false)
    private BigDecimal money;

    /**
     * 退款金额
     */
    @Column
    private BigDecimal refundMoney;

    /**
     * 充值详情
     */
    @Column
    private String detail;

    /**
     * 微信支付订单号
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 支付完成时间
     */
    @Column(name = "time_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeEnd;

    /**
     * 退款时间
     */
    @Column(name = "refund_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date refundTime;

    /**
     * 交易状态描述
     */
    @Column(name = "trade_state_desc")
    private String tradeStateDesc;

    /**
     * 银行类型，采用字符串类型的银行标识
     */
    @Column(name = "bank_type")
    private String bankType;

    @Column(name = "if_check", nullable = false, columnDefinition = "bit(1) default 0")
    private Boolean ifCheck;

    @Version
    @Column(nullable = false)
    private Integer version;
}
