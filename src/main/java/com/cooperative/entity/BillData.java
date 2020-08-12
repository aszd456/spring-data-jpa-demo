package com.cooperative.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName BillData
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/6/28 10:18
 * @Version 1.0
 **/
@Data
@Entity
@Table(name = "bill_data")
public class BillData extends BaseMode implements Serializable {

    private static final long serialVersionUID = -1876039821395361709L;

    public BillData() {
    }

    /**
     * 微信交易时间
     */
    @Column(name = "wx_transaction_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date wxTransactionTime;

    /**
     * 公众账号id
     */
    @Column(name = "public_account_id")
    private String publicAccountId;

    /**
     * 商户号
     */
    @Column(name = "merchant_number")
    private String merchantNumber;

    /**
     * 特约商户号
     */
    @Column(name = "special_merchant_no")
    private String specialMerchantNo;


    /**
     * 设备号
     */
    @Column(name = "device_number")
    private String deviceNumber;

    /**
     * 微信订单号
     */
    @Column(name = "wechat_order_no")
    private String wechatOrderNo;

    /**
     * 商户订单号
     */
    @Column(name = "merchant_order_number")
    private String merchantOrderNumber;

    /**
     * 用户标识
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 交易类型
     */
    @Column(name = "transaction_type")
    private String transactionType;

    /**
     * 交易状态
     */
    @Column(name = "trading_status")
    private String tradingStatus;

    /**
     * 付款银行
     */
    @Column(name = "paying_bank")
    private String payingBank;

    /**
     * 货币种类
     */
    @Column(name = "currency_type")
    private String currencyType;

    /**
     * 应结订单金额
     */
    @Column(name = "order_amount_all")
    private BigDecimal orderAmountToBeSettled;

    /**
     * 代金券金额
     */
    @Column(name = "voucher_amount")
    private BigDecimal voucherAmount;

    /**
     * 微信退款单号
     */
    @Column(name = "refund_no")
    private String wechatRefundNo;

    /**
     * 商户退款单号
     */
    @Column(name = "merchant_refund_no")
    private String merchantRefundNo;

    /**
     * 退款金额
     */
    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    /**
     * 充值券退款金额
     */
    @Column(name = "refund_amount_voucher")
    private BigDecimal refundAmountOfRechargeVoucher;

    /**
     * 退款类型
     */
    @Column(name = "refund_type")
    private String refundType;

    /**
     * 退款状态
     */
    @Column(name = "refund_status")
    private String refundStatus;

    /**
     * 商品名称
     */
    @Column(name = "trade_name")
    private String tradeName;

    /**
     * 商户数据包
     */
    @Column(name = "merchant_packet")
    private String merchantPacket;

    /**
     * 手续费
     */
    @Column(name = "service_charge")
    private BigDecimal serviceCharge;

    /**
     * 费率
     */
    @Column(name = "rate")
    private String rate;

    /**
     * 订单金额
     */
    @Column(name = "order_amount")
    private BigDecimal orderAmount;

    /**
     * 申请退款金额
     */
    @Column(name = "apply_refund")
    private BigDecimal applicationForRefundAmount;

    /**
     * 费率备注
     */
    @Column(name = "rate_notes")
    private String rateNotes;
}
