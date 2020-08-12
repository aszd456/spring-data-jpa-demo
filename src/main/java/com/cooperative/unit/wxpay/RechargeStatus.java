package com.cooperative.unit.wxpay;


public enum RechargeStatus {
    SUCCESS ("支付成功"),

    REFUND("转入退款"),

    NOTPAY("未支付"),

    CLOSED("已关闭"),

    REVOKED("已撤销-刷卡支付"),

    USERPAYING("用户支付中"),

    PAYERROR("支付失败");

    private String name;

    RechargeStatus(String name) {
        this.name = name;
    }


}
