package com.cooperative.unit.wxpay;


import com.cooperative.unit.SpringBeanUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class AppletPayUtil {
    /**
     * 根据 临时登录凭证获取openId
     * 文档:https://developers.weixin.qq.com/miniprogram/dev/api/code2Session.html
     *
     */
    public static String getOpenIdByCode(String code) {
        log.info("获取code成功!{}", code);
        //登录凭证校验
        //
        WeChatConfig weChatConfig = (WeChatConfig) SpringBeanUtil.getBean("weChatConfig");
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + weChatConfig.getAppID() + "&secret=" + weChatConfig.getAppSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        //发送请求给微信后端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        InputStream inputStream = null;
        CloseableHttpResponse httpResponse = null;
        StringBuilder result = new StringBuilder();
        String openId = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            inputStream = entity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                JsonObject jsonObject =JsonParser.parseString(line).getAsJsonObject();
//                JSONObject jsonObject = JSON.parseObject(line);
//                openId = jsonObject.getString("openid");
//                String sessionKey = jsonObject.getString("session_key");
                openId = jsonObject.get("openid").getAsString();
                String sessionKey = jsonObject.get("session_key").getAsString();
                log.info("openId={},sessionKey={}", openId, sessionKey);
            }
        } catch (IOException e) {
            log.error("获取openId失败" + e.getMessage());
        }
        return openId;

    }

}
