package com.cooperative.controller;

import com.cooperative.dto.RechargeOrderDto;
import com.cooperative.dto.UserInfoDto;
import com.cooperative.dto.UserLoginDto;
import com.cooperative.entity.RechargeOrder;
import com.cooperative.entity.User;
import com.cooperative.entity.UserInfo;
import com.cooperative.service.PayService;
import com.cooperative.service.RechargeService;
import com.cooperative.service.UserService;
import com.cooperative.unit.*;
import com.cooperative.unit.wxpay.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api")
public class WxPayController {

    @Autowired
    private PayService payService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    @Qualifier(value = "wx_service")
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;


    /**
     * 微信小程序登录
     *
     * @param userLoginDto
     * @return
     */
    @PostMapping("/wxMiniLogin")
    public Object wxMiniLogin(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        Map<String, Object> rsMap = Maps.newHashMap();
        try {
            String code = userLoginDto.getCode();
            UserInfoDto userInfoDto = userLoginDto.getUserInfo();
            if (StringUtils.isBlank(code)) {
                return Render.failMap("微信登录异常，请联系客服！");
            }
            //调用接口获取openId
            String openId = AppletPayUtil.getOpenIdByCode(code);
            if (openId == null) {
                return Render.failMap("获取openid失败");
            }
            User user = userService.findByOpenId(openId);
            if (user == null) {
                user = new User();
                UserInfo info = new UserInfo();
                String password = Tools.md5(openId + Tools.dateToStr(new Date())).trim().toUpperCase();
                user.setUserInfo(info);
                user.setName(userInfoDto.getNickName());
                user.setCreateTime(new Date());
                user.setOpenId(openId);
                user.setPassword(password);
                user.setRemainMoney(BigDecimal.ZERO);
                user.setRegIp(SpringUtil.clientIp());
                user.setUserWay(UserWay.WX_MINI);

                user = userService.addOrUpdateUser(user, info);
            }
            request.getServletContext().setAttribute("userId", user.getId());
            String userJson = null;
            try {
                userJson = mapper.writeValueAsString(user);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            rsMap.put("user", userJson);
            return Render.okMap(rsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Render.fail("接口出错");
        }
    }

    /**
     * 微信小程序支付
     */
    @PostMapping("/wxMiniRecharge")
    public Object wxMiniRecharge(@RequestBody RechargeOrderDto rechargeOrderDto, HttpServletRequest request) throws Exception {
        //获取code
        String code = rechargeOrderDto.getCode();
        //调用接口获取openId
        String openId = AppletPayUtil.getOpenIdByCode(code);
        if (openId == null) {
            return Render.failMap("获取openid失败");
        }
        /**创建充值订单**/
        RechargeOrder order = new RechargeOrder();
        order.setMoney(rechargeOrderDto.getRechargeMoney());
        order.setDetail("微信小程序充值单");
        order.setCreateTime(new Date());
        order.setRechargeTradeType(RechargeTradeType.WX_MINI);
        order.setRechargeStatus(RechargeStatus.NOTPAY);
        long userId = (Long) request.getServletContext().getAttribute("userId");
        order.setUserId(userId);
        order.setIfCheck(false);
        order.setRefundMoney(BigDecimal.ZERO);
        order = rechargeService.saveRechargeOrder(order);

        String outTradeNo = Constant.WX_MINI_PAY_PREFIX + order.getId();
        Map<String, String> rMap;
        try {
            /**下单接口***/
            rMap = payService.unifiedOrder(outTradeNo, rechargeOrderDto.getRechargeMoney(), openId);
        } catch (Exception e) {
            e.printStackTrace();
            return Render.failMap("下单失败");
        }

        return Render.ok(rMap);
    }

    /**
     * 功能描述: <小程序回调>
     **/
    @PostMapping("/wxMiniCallBack")
    public void wxMiniCallBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("进入微信小程序支付回调");
        String xmlMsg = HttpKit.readData(request);
        log.info("微信小程序通知信息" + xmlMsg);
        Map<String, String> notifyMap = WXPayUtil.xmlToMap(xmlMsg);
        String xmlBack = "";
        try {
            xmlBack = payService.wxMiniCallBack(notifyMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信支付回调通知失败，原因：", e.getMessage());
            xmlBack = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml> ";
        }
        response.getWriter().write(xmlBack);
    }

    /**
     * 小程序订单查询
     */
    @PostMapping("/wxMiniOrderQuery")
    public Object wxMiniOrderQuery(@RequestParam(required = true) Long orderId) {
        RechargeOrder order = rechargeService.getRechargeOrderById(orderId);
        Map<String, String> resultMap = null;
        try {
            resultMap = payService.wxMiniOrderQuery(order);
        } catch (Exception e) {
            e.printStackTrace();
            return Render.failMap("查询订单失败：" + e.getMessage());
        }

        return Render.ok(resultMap);
    }

    /**
     * 申请退款
     *
     * @param orderId 订单号
     */
    @PostMapping("/wxRefundOrder")
    public Object wxRefundOrder(@RequestParam(required = true) Long orderId,
                                @RequestParam(required = true) BigDecimal refundAmount,
                                @RequestParam(required = true) String refund_desc) {
        Map<String, String> resultMap;
        try {
            resultMap = payService.wxMiniRefund(orderId, refundAmount, refund_desc);
        } catch (Exception e) {
            e.printStackTrace();
            return Render.failMap("申请退款失败：" + e.getMessage());
        }

        return Render.ok(resultMap);
    }

    @PostMapping("/wxMiniRefundCallBack")
    public void wxMiniRefundCallBack(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        log.info("开始回调");
        InputStream inStream = httpRequest.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultXml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params = WXPayUtil.xmlToMap(resultXml);
        outSteam.close();
        inStream.close();
        String xmlBack;
        try {
            xmlBack = payService.wxMiniRefundCallBack(params);
        } catch (Exception e) {
            log.error("微信支付退款回调失败，原因：", e.getMessage());
            xmlBack = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml> ";
        }
        httpResponse.getWriter().write(xmlBack);
    }

    /**
     * 查询退款
     *
     * @param orderId
     * @return
     */
    @PostMapping("/wxMiniRefundQuery")
    public Object wxMiniRefundQuery(@RequestParam(required = true) Long orderId) {
        Map<String, String> resultMap = null;
        try {

            resultMap = payService.wxMiniRefundQuery(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return Render.failMap("查询退款失败：" + e.getMessage());
        }

        return Render.ok(resultMap);
    }

    @PostMapping("/wxMiniDownLoadBill")
    public Object wxMiniDownLoadBill(HttpServletResponse response,
                                     @RequestParam(required = true) String bill_date) {

        Map<String, String> resultMap;
        try {
            resultMap = payService.wxMiniDownLoadBill(bill_date);
            resultMap.forEach((key, value) -> {
                System.out.println(key + ":" + value);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Render.failMap("下载对账单失败，原因：" + e.getMessage());
        }
        return Render.ok(resultMap);

    }

}
