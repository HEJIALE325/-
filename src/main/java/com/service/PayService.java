package com.service;

import java.util.Map;

/**
 * 支付服务接口
 */
public interface PayService {

    /**
     * 微信支付统一下单
     * @param orderNo 订单号
     * @param amount 金额（单位：分）
     * @param body 商品描述
     * @param openid 用户openid
     * @return 支付参数
     */
    Map<String, String> wechatPayUnifiedOrder(String orderNo, int amount, String body, String openid);

    /**
     * 处理微信支付回调
     * @param notifyData 回调数据
     * @return 处理结果
     */
    Map<String, String> handleWechatPayNotify(String notifyData);

    /**
     * 支付宝支付下单
     * @param orderNo 订单号
     * @param amount 金额（单位：元）
     * @param subject 商品名称
     * @param body 商品描述
     * @return 支付表单
     */
    String alipayTradePagePay(String orderNo, double amount, String subject, String body);

    /**
     * 处理支付宝支付回调
     * @param params 回调参数
     * @return 处理结果
     */
    boolean handleAlipayNotify(Map<String, String> params);

    /**
     * 验证支付宝回调签名
     * @param params 回调参数
     * @return 是否验证通过
     */
    boolean verifyAlipaySign(Map<String, String> params);
}
