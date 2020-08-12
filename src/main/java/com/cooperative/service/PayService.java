package com.cooperative.service;

import com.cooperative.entity.BillData;
import com.cooperative.entity.RechargeOrder;
import com.cooperative.entity.User;
import com.cooperative.unit.*;
import com.cooperative.unit.wxpay.*;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class PayService {

    @Autowired
    WeChatConfig weChatConfig;

    @Autowired
    RechargeService rechargeService;

    @Autowired
    UserService userService;

    @Autowired
    BillDataService dataService;

    /**
     * 功能描述: <调用统一下单的接口>
     **/
    public Map<String, String> unifiedOrder(String outTradeNo, BigDecimal money, String openid) throws Exception {
        Map<String, String> reqParams = Maps.newLinkedHashMap();
        //微信分配的小程序ID
        reqParams.put("appid", weChatConfig.getAppID());
        //微信支付分配的商户号
        reqParams.put("mch_id", weChatConfig.getMchID());
        //随机字符串
        reqParams.put("nonce_str", WXPayUtil.generateNonceStr());
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "充值订单-微信小程序");
        //商户订单号
        reqParams.put("out_trade_no", outTradeNo);
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
//        InetAddress addr = null;
//        try {
//            addr = InetAddress.getLocalHost();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }

        String spbill_create_ip = SpringUtil.clientIp();
        System.out.println(spbill_create_ip);

        reqParams.put("spbill_create_ip", spbill_create_ip);
        //通知地址
        reqParams.put("notify_url", weChatConfig.getNotify_url());
        //交易类型
        reqParams.put("trade_type", "JSAPI");
        //用户标识
        reqParams.put("openid", openid);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, weChatConfig.getKey());
        reqParams.put("sign", sign);
        /*
            调用支付定义下单API,返回预付单信息 prepay_id
         */
        WXPay wxPay = new WXPay(weChatConfig);
        Map<String, String> result = wxPay.unifiedOrder(reqParams);
//        result.forEach((key,value)->System.out.println(key+":"+value));

        //返回结果
        String appId = result.get("appid");
        String prepay_id = result.get("prepay_id");
        String result_code = result.get("result_code");
        String return_code = result.get("return_code");
        Map<String, String> packageParams = Maps.newHashMap();
        if ("SUCCESS".equals(result_code) && "SUCCESS".equals(return_code)) {
            packageParams.put("appId", appId);
            packageParams.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
            packageParams.put("nonceStr", WXPayUtil.generateNonceStr());
            packageParams.put("package", "prepay_id=" + prepay_id);
            packageParams.put("signType", "MD5");
            String packageSign = WXPayUtil.generateSignature(packageParams, weChatConfig.getKey());
            packageParams.put("paySign", packageSign);
        } else {
            throw new DataException("下单出错：返回信息" + result.get("return_msg")+";错误代码描述:"+result.get("err_code_des"));
        }

        return packageParams;

    }

    /**
     * 小程序支付回调
     *
     * @param notifyMap
     * @return
     * @throws Exception
     */
    public String wxMiniCallBack(Map<String, String> notifyMap) throws Exception {
        String SUCCESS_INFO = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg></return_msg></xml>";
        String FAIL_INFO = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg></return_msg></xml>";
        WXPay wxpay = new WXPay(weChatConfig);
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            log.info("微信支付回调成功订单号: {}", notifyMap);
            String return_code = notifyMap.get("return_code");//状态
            String result_code = notifyMap.get("result_code");
            if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
                String out_trade_no = notifyMap.get("out_trade_no");//订单号
                String transaction_id = notifyMap.get("transaction_id");//微信支付订单号
                String bank_type = notifyMap.get("bank_type");//付款银行
                String total_fee = notifyMap.get("total_fee");//订单金额
                String time_end = notifyMap.get("time_end");//支付完成时间
                log.info("微信小程序支付成功,订单号{}", out_trade_no);
                if (out_trade_no == null) {
                    log.info("微信支付回调失败订单号: {}", notifyMap);
                    return FAIL_INFO;
                }
                String orderId = out_trade_no.replace(Constant.WX_MINI_PAY_PREFIX,"");
                RechargeOrder order = rechargeService.getRechargeOrderById(Long.valueOf(orderId));
                if (null == order) {
                    throw new DataException("订单为空");
                }
                Long userId = order.getUserId();
                User user = userService.getUserById(userId);
                if (null == user) {
                    throw new DataException("用户不存在");
                }
                if (new BigDecimal(total_fee).divide(BigDecimal.valueOf(100L)).compareTo(order.getMoney()) != 0) {
                    log.error("订单金额和数据金额不相符");
                    throw new DataException("订单金额和数据金额不相符");
                }
                log.info("回写订单操作开始");
                synchronized (Constant.CONFIRM_RECHARGE) {
                    if (order.getIfCheck()) {
                        throw new DataException("该充值单已通过");
                    } else {
                        order.setRechargeStatus(RechargeStatus.SUCCESS);
                        order.setIfCheck(true);
                        order.setTransactionId(transaction_id);
                        order.setTimeEnd(DateUtil.strToDate(time_end, "yyyyMMddHHmmss"));
                        rechargeService.saveRechargeOrder(order);
                        userService.userRecharge(user, order, FundMode.IN);
                    }

                }
                log.info("回写订单操作结束");

                /*************/

                return SUCCESS_INFO;

            } else {
                return FAIL_INFO;
            }
        } else {
            log.error("微信支付回调通知签名错误");
            return FAIL_INFO;
        }
    }

    /**
     * 查询订单
     *
     * @param rechargeOrder
     * @return
     * @throws Exception
     */
    public Map<String, String> wxMiniOrderQuery(RechargeOrder rechargeOrder) throws Exception {
        WXPay wxpay = new WXPay(weChatConfig);
        Map<String, String> reqParams = Maps.newLinkedHashMap();
        //微信分配的小程序ID
        reqParams.put("appid", weChatConfig.getAppID());
        //微信支付分配的商户号
        reqParams.put("mch_id", weChatConfig.getMchID());
        //商户订单号
        reqParams.put("out_trade_no", Constant.WX_MINI_PAY_PREFIX + rechargeOrder.getId());
        //随机字符串
        reqParams.put("nonce_str", WXPayUtil.generateNonceStr());
        //签名类型
        reqParams.put("sign_type", "MD5");
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, weChatConfig.getKey());
        reqParams.put("sign", sign);
        Map<String, String> queryResultMap = wxpay.orderQuery(reqParams);
        if (queryResultMap == null || queryResultMap.isEmpty()) {
            throw new DataException("解析内容为空");
        }

        if (!queryResultMap.get("return_code").equals("SUCCESS")) {
            throw new DataException(queryResultMap.get("return_msg"));
        }

        if (!wxpay.isResponseSignatureValid(queryResultMap)) {
            throw new DataException("签名验证失败");
        }

        if (!queryResultMap.get("result_code").equals("SUCCESS")) {
            throw new DataException(queryResultMap.get("err_code_des"));
        }

        if (!queryResultMap.get("trade_state").equals("SUCCESS")) {
            throw new DataException("未支付成功");
        }
        String totalFee = queryResultMap.get("total_fee");
        String transaction_id = queryResultMap.get("transaction_id");
        String time_end = queryResultMap.get("time_end");

        if (BigDecimal.valueOf(Double.valueOf(totalFee) / 100.0).compareTo(rechargeOrder.getMoney()) != 0) {
            throw new DataException("订单金额异常");
        }

        synchronized (Constant.CONFIRM_RECHARGE) {
            if (rechargeOrder.getIfCheck()) {
                throw new DataException("该充值单已通过");
            } else {
                User user = userService.getUserById(rechargeOrder.getUserId());
                rechargeOrder.setRechargeStatus(RechargeStatus.SUCCESS);
                rechargeOrder.setIfCheck(true);
                rechargeOrder.setTransactionId(transaction_id);
                rechargeOrder.setTimeEnd(DateUtil.strToDate(time_end, "yyyyMMddHHmmss"));
                rechargeService.saveRechargeOrder(rechargeOrder);
                userService.userRecharge(user, rechargeOrder, FundMode.IN);
            }

        }

        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("msg", "手动充值成功");
        return resultMap;
    }

    /**
     * 关闭订单
     *
     * @param out_trade_no
     * @return
     * @throws Exception
     */
    public Map<String, String> wxMiniCloseOrder(String out_trade_no) throws Exception {
        WXPay wxpay = new WXPay(weChatConfig);
        Map<String, String> reqParams = Maps.newLinkedHashMap();
        //微信分配的小程序ID
        reqParams.put("appid", weChatConfig.getAppID());
        //微信支付分配的商户号
        reqParams.put("mch_id", weChatConfig.getMchID());
        //商户订单号
        reqParams.put("out_trade_no", out_trade_no);
        //随机字符串
        reqParams.put("nonce_str", WXPayUtil.generateNonceStr());
        //签名类型
        reqParams.put("sign_type", "MD5");
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, weChatConfig.getKey());
        reqParams.put("sign", sign);

        Map<String, String> closeResultMap = wxpay.closeOrder(reqParams);
        String return_code = closeResultMap.get("return_code");
        if (!return_code.equals("SUCCESS")) {
            throw new DataException("关闭订单失败，错误码：" + closeResultMap.get("err_code"));
        }
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("msg", "订单关闭成功");
        return resultMap;
    }

    /**
     * 申请退款
     *
     * @return
     * @throws Exception
     */
    public Map<String, String> wxMiniRefund(Long orderId, BigDecimal refundAmount, String refund_desc) throws Exception {
        RechargeOrder rechargeOrder = rechargeService.getRechargeOrderById(orderId);
        Long userId = rechargeOrder.getUserId();
        User user = userService.getUserById(userId);
        if (rechargeOrder == null || !rechargeOrder.getIfCheck()) {
            throw new DataException("该充值单不存在，或未完成支付");
        }
        if (rechargeOrder.getRefundType() != null) {
            throw new DataException("该充值单已处理过退款，请勿重复提交");
        }
        if (!refundAmount.toString().matches("^[0-9]+(.[0-9]{1,3})?$")) {
            throw new DataException("退款金额不合法");
        }
        if (refundAmount.compareTo(rechargeOrder.getMoney()) > 0) {
            throw new DataException("申请退款金额不能大于选择的充值单的充值金额");
        }
        if (refundAmount.compareTo(user.getRemainMoney()) > 0) {
            throw new DataException("申请退款金额不能大于用户余额");
        }


        WXPay wxpay = new WXPay(weChatConfig);
        Map<String, String> reqParams = Maps.newLinkedHashMap();
        //微信分配的小程序ID
        reqParams.put("appid", weChatConfig.getAppID());
        //微信支付分配的商户号
        reqParams.put("mch_id", weChatConfig.getMchID());
        //商户订单号
        reqParams.put("out_trade_no", Constant.WX_MINI_PAY_PREFIX + rechargeOrder.getId());
        //退款订单号
        reqParams.put("out_refund_no", "refund_" + rechargeOrder.getId());
        //订单金额
        reqParams.put("total_fee", "" + rechargeOrder.getMoney().multiply(BigDecimal.valueOf(100l)).intValue());
        //退款金额
        reqParams.put("refund_fee", "" + refundAmount.multiply(BigDecimal.valueOf(100l)).intValue());
        //随机字符串
        reqParams.put("nonce_str", WXPayUtil.generateNonceStr());
        //签名类型
        reqParams.put("sign_type", "MD5");
        //退款原因
        reqParams.put("refund_desc", refund_desc);
        reqParams.put("notify_url", weChatConfig.getRefund_url());
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, weChatConfig.getKey());
        reqParams.put("sign", sign);
        Map<String, String> refundMap = wxpay.refund(reqParams);
        String result_code = refundMap.get("result_code");
        if (!result_code.equals("SUCCESS")) {
            throw new DataException("申请退款失败，错误码：" + refundMap.get("err_code"));
        }
        rechargeOrder.setRefundType(RefundType.ORDER);
        rechargeOrder.setRefundMoney(refundAmount);
        rechargeOrder.setRefundTime(new Date());
        rechargeService.saveRechargeOrder(rechargeOrder);

        userService.userRecharge(user, rechargeOrder, FundMode.OUT);

        /****记录资金明细省略****/

        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("msg", "申请退款请求成功");

        return resultMap;
    }

    /**
     * 退款回调处理
     *
     * @return
     * @throws Exception
     */
    public String wxMiniRefundCallBack(Map<String, String> refundMap) throws Exception {
        String SUCCESS_INFO = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg></return_msg></xml>";
        String FAIL_INFO = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg></return_msg></xml>";
        if (refundMap.get("return_code").equals("SUCCESS")) {

            String req_info = refundMap.get("req_info");
            log.info(req_info);
            String afterDecrypt = AESUtil.decryptData(req_info, weChatConfig.getKey());
            log.info(afterDecrypt);
            Map<String, String> aesMap = WXPayUtil.xmlToMap(afterDecrypt);
            log.info("退款成功1");
            /** 以下为返回的加密字段： **/
            String refund_status = aesMap.get("refund_status");
            String transaction_id = aesMap.get("transaction_id");
            String total_fee = aesMap.get("total_fee");
            String refund_fee = aesMap.get("refund_fee");
            BigDecimal total = BigDecimal.valueOf(Double.valueOf(total_fee) / 100.0);
            RechargeOrder rechargeOrder = rechargeService.findByTransactionId(transaction_id);
            if (rechargeOrder.getMoney().compareTo(total) != 0) {
                throw new DataException("订单金额不一致");
            }
            User user = userService.getUserById(rechargeOrder.getUserId());
            if (WXPayConstants.SUCCESS.equals(refund_status)) {
                BigDecimal refundFee = BigDecimal.valueOf(Double.valueOf(refund_fee) / 100.0);
                if (refundFee.compareTo(rechargeOrder.getRefundMoney()) == 0) {
                    rechargeOrder.setRefundType(RefundType.REFUND_SUCCESS);
                    rechargeService.saveRechargeOrder(rechargeOrder);
                    log.info("退款成功");
                    return SUCCESS_INFO;
                }
            }
        }
        return FAIL_INFO;
    }

    /**
     * 查询退款
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    public Map<String, String> wxMiniRefundQuery(Long orderId) throws Exception {

        WXPay wxpay = new WXPay(weChatConfig);
        Map<String, String> reqParams = Maps.newLinkedHashMap();
        //微信分配的小程序ID
        reqParams.put("appid", weChatConfig.getAppID());
        //微信支付分配的商户号
        reqParams.put("mch_id", weChatConfig.getMchID());
        //商户订单号
        String out_trade_no = Constant.WX_MINI_PAY_PREFIX + orderId;
        reqParams.put("out_trade_no", out_trade_no);
        //随机字符串
        reqParams.put("nonce_str", WXPayUtil.generateNonceStr());
        //签名类型
        reqParams.put("sign_type", "MD5");
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, weChatConfig.getKey());
        reqParams.put("sign", sign);
        Map<String, String> refundMap = wxpay.refundQuery(reqParams);
        Map<String, String> resultMap = Maps.newHashMap();
        String result_code = refundMap.get("result_code");
        if (!result_code.equals("SUCCESS")) {
            throw new DataException("退款申请接收失败");
        }
        RechargeOrder rechargeOrder = rechargeService.getRechargeOrderById(orderId);
        if (refundMap.get("refund_status_0").equals("SUCCESS")) {
            String refundFeeStr = refundMap.get("refund_fee_0");
            BigDecimal refundFee = BigDecimal.valueOf(Double.valueOf(refundFeeStr) / 100.0);
            if (refundFee.compareTo(rechargeOrder.getRefundMoney()) == 0) {
                rechargeOrder.setRefundType(RefundType.REFUND_SUCCESS);
                rechargeService.saveRechargeOrder(rechargeOrder);
            }
            resultMap.put("msg", "退款成功");
        } else if (refundMap.get("refund_status_0").equals("FAIL")) {
            resultMap.put("msg", "退款失败");
            rechargeOrder.setRefundType(RefundType.REFUND_FAIL);
            rechargeService.saveRechargeOrder(rechargeOrder);
        }
        return resultMap;
    }

    /**
     * 下载交易账单
     *
     * @param bill_date
     * @return
     * @throws Exception
     */
    public Map<String, String> wxMiniDownLoadBill(String bill_date) throws Exception {
        log.info("填写对账单请求参数");
        WXPay wxpay = new WXPay(weChatConfig);
        Map<String, String> reqParams = Maps.newLinkedHashMap();
        //微信分配的小程序ID
        reqParams.put("appid", weChatConfig.getAppID());
        //微信支付分配的商户号
        reqParams.put("mch_id", weChatConfig.getMchID());
        //随机字符串
        reqParams.put("nonce_str", WXPayUtil.generateNonceStr());
        //签名类型
        reqParams.put("bill_type", "ALL");
        //对账单日期
        reqParams.put("bill_date", bill_date);
        //签名
        String sign = WXPayUtil.generateSignature(reqParams, weChatConfig.getKey());
        reqParams.put("sign", sign);
        log.info("发起对账单");
        Map<String, String> billMap = wxpay.downloadBill(reqParams);
        if(null==billMap || billMap.size()==0){
            throw new DataException(bill_date+"的对账单数据为空");
        }
        log.info(bill_date+"对账单返回信息：{}",billMap);
        String data = billMap.get("data");
        if(StringUtils.isEmpty(data)){
            throw new DataException(bill_date+"的对账单数据为空");
        }
        String firstString = "费率备注";
        String tradeMsg = data.substring(data.indexOf(firstString) + firstString.length());
        String secendString = "总交易单数";
        String tradeInfo = tradeMsg.substring(0, tradeMsg.indexOf(secendString));
        String[] str = tradeInfo.split("\\r\\n");
        int len = str.length;
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                if (str[i].length() > 0) {
                    String[] order = str[i].split(",");

                    String wechatOrderNo = order[5].replace("`", "");
                    BillData wxBillData = dataService.findByWechatOrderNo(wechatOrderNo);
                    if(null==wxBillData){
                        wxBillData = new BillData();
                        wxBillData.setCreateTime(new Date());
                    }
                    String trade_time = order[0].replace("`", "");
                    wxBillData.setWxTransactionTime(DateUtil.strToDate(trade_time, "yyyy-MM-dd HH:mm:ss"));
                    wxBillData.setPublicAccountId(order[1].replace("`", ""));
                    wxBillData.setMerchantNumber(order[2].replace("`", ""));
                    wxBillData.setSpecialMerchantNo(order[3].replace("`", ""));
                    wxBillData.setDeviceNumber(order[4].replace("`", ""));
                    wxBillData.setWechatOrderNo(order[5].replace("`", ""));
                    wxBillData.setMerchantOrderNumber(order[6].replace("`", ""));
                    wxBillData.setUserId(order[7].replace("`", ""));
                    wxBillData.setTransactionType(order[8].replace("`", ""));
                    wxBillData.setTradingStatus(order[9].replace("`", ""));
                    wxBillData.setPayingBank(order[10].replace("`", ""));
                    wxBillData.setCurrencyType(order[11].replace("`", ""));
                    String amount = order[12].replace("`", "");
                    wxBillData.setOrderAmountToBeSettled(BigDecimal.valueOf(Double.valueOf(amount)));
                    String voucherAmount = order[13].replace("`", "");
                    wxBillData.setVoucherAmount(BigDecimal.valueOf(Double.valueOf(voucherAmount)));
                    wxBillData.setWechatRefundNo(order[14].replace("`", ""));
                    wxBillData.setMerchantRefundNo(order[15].replace("`", ""));
                    String refundAmount = order[16].replace("`", "");
                    wxBillData.setRefundAmount(BigDecimal.valueOf(Double.valueOf(refundAmount)));
                    String refundAmountVoucher = order[17].replace("`", "");
                    wxBillData
                            .setRefundAmountOfRechargeVoucher(BigDecimal.valueOf(Double.valueOf(refundAmountVoucher)));
                    wxBillData.setRefundType(order[18].replace("`", ""));
                    wxBillData.setRefundStatus(order[19].replace("`", ""));
                    wxBillData.setTradeName(order[20].replace("`", ""));
                    wxBillData.setMerchantPacket(order[21].replace("`", ""));
                    String charge = order[22].replace("`", "");
                    wxBillData.setServiceCharge(BigDecimal.valueOf(Double.valueOf(charge)));
                    wxBillData.setRate(order[23].replace("`", ""));
                    String orderAmount = order[24].replace("`", "");
                    wxBillData.setOrderAmount(BigDecimal.valueOf(Double.valueOf(orderAmount)));
                    String applyAmount = order[25].replace("`", "");
                    wxBillData.setApplicationForRefundAmount(BigDecimal.valueOf(Double.valueOf(applyAmount)));
                    wxBillData.setRateNotes(order[26].replace("`", ""));
                    wxBillData.setUpdateTime(new Date());
                    dataService.saveBillData(wxBillData);
                }
            }
        }
        Map<String,String> resultMap = Maps.newHashMap();
        resultMap.put("msg","已保存到数据库");
        return resultMap;
    }
}
