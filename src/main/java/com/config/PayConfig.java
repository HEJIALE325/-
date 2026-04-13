package com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 支付配置类
 */
@Component
public class PayConfig {

    // 微信支付配置
    @Value("${wechat.pay.appid:}")
    private String wechatAppId;

    @Value("${wechat.pay.mchId:}")
    private String wechatMchId;

    @Value("${wechat.pay.key:}")
    private String wechatKey;

    @Value("${wechat.pay.notifyUrl:}")
    private String wechatNotifyUrl;

    // 支付宝支付配置
    @Value("${alipay.appid:}")
    private String alipayAppId;

    @Value("${alipay.privateKey:}")
    private String alipayPrivateKey;

    @Value("${alipay.publicKey:}")
    private String alipayPublicKey;

    @Value("${alipay.notifyUrl:}")
    private String alipayNotifyUrl;

    @Value("${alipay.returnUrl:}")
    private String alipayReturnUrl;

    // 获取微信支付配置
    public String getWechatAppId() {
        return wechatAppId;
    }

    public String getWechatMchId() {
        return wechatMchId;
    }

    public String getWechatKey() {
        return wechatKey;
    }

    public String getWechatNotifyUrl() {
        return wechatNotifyUrl;
    }

    // 获取支付宝支付配置
    public String getAlipayAppId() {
        return alipayAppId;
    }

    public String getAlipayPrivateKey() {
        return alipayPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public String getAlipayNotifyUrl() {
        return alipayNotifyUrl;
    }

    public String getAlipayReturnUrl() {
        return alipayReturnUrl;
    }
}
