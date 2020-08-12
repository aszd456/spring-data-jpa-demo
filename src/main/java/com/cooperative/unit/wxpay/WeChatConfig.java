package com.cooperative.unit.wxpay;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Data
@Slf4j
@ConfigurationProperties(prefix = "wxpayconfig")
@Component(value = "weChatConfig")
public class WeChatConfig extends WXPayConfig {
    private String appID; // appID

    private String mchID; // 商户号ID

    private String appSecret;//小程序 appSecret

    private String key; // 商户的key【API密匙】

    private String notify_url; // 支付完成后的异步通知地址

    private String refund_url; //退款回调地址

    private String apiv3; //apiv3是微信回调通知

    private String certPath; //证书路径
    /**
     * 证书内容
     */
    private byte[] certData;

    private static WeChatConfig INSTANCE;

    public WeChatConfig() {

    }

    @PostConstruct
    public void initCertStream() {

        if (StringUtils.isNotBlank(this.certPath)) {
            InputStream certStream = this.getClass().getResourceAsStream(this.certPath);
            int fileLen = 0;
            if (certStream != null) {
                try {
                    fileLen = certStream.available();
                    this.certData = new byte[fileLen];
                    certStream.read(this.certData, 0, fileLen);
                    certStream.close();
                } catch (IOException e) {
                    log.error("cert not find,", e);
                }
            }

        }
    }


    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return IWXPayDomainImpl.instance();
    }


}
