package com.cooperative.unit.wxpay;

/**
 * @ClassName RefundType
 * @Description TODO
 * @Author zhouliansheng
 * @Date 2020/6/23 10:52
 * @Version 1.0
 **/
public enum RefundType {
    ORDER("退款处理中"),
    REFUND_SUCCESS("退款成功"),
    REFUND_FAIL("退款失败");

    private String text;

    private RefundType(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
