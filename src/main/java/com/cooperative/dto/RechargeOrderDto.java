package com.cooperative.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RechargeOrderDto implements Serializable {

    private static final long serialVersionUID = -8390791479272750629L;
    /**
     * 充值金额
     */
    private BigDecimal rechargeMoney;

    /**
     * 临时凭证code
     * 小程序支付调用wx.login();获取到登录临时凭证code
     */
    private String code;

}
