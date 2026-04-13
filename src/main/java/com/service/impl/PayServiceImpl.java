package com.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.config.PayConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.service.ChongwuyongpinOrderService;
import com.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付服务实现类
 */
@Service
public class PayServiceImpl implements PayService {

    private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private ChongwuyongpinOrderService chongwuyongpinOrderService;

    private WXPay wxPay;

    /**
     * 初始化微信支付客户端
     */
    private WXPay getWxPay() {
        if (wxPay == null) {
            Map<String, String> config = new HashMap<>();
            config.put("appid", payConfig.getWechatAppId());
            config.put("mch_id", payConfig.getWechatMchId());
            config.put("key", payConfig.getWechatKey());
            config.put("notify_url", payConfig.getWechatNotifyUrl());
            wxPay = new WXPay(config);
        }
        return wxPay;
    }

    /**
     * 微信支付统一下单
     */
    @Override
    public Map<String, String> wechatPayUnifiedOrder(String orderNo, int amount, String body, String openid) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("appid", payConfig.getWechatAppId());
            params.put("mch_id", payConfig.getWechatMchId());
            params.put("nonce_str", WXPayUtil.generateNonceStr());
            params.put("body", body);
            params.put("out_trade_no", orderNo);
            params.put("total_fee", String.valueOf(amount));
            params.put("spbill_create_ip", "127.0.0.1");
            params.put("notify_url", payConfig.getWechatNotifyUrl());
            params.put("trade_type", "JSAPI");
            params.put("openid", openid);

            String sign = WXPayUtil.generateSignature(params, payConfig.getWechatKey());
            params.put("sign", sign);

            Map<String, String> result = getWxPay().unifiedOrder(params);
            logger.info("微信支付统一下单结果: {}", result);

            if ("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(result.get("result_code"))) {
                Map<String, String> payParams = new HashMap<>();
                payParams.put("appId", payConfig.getWechatAppId());
                payParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
                payParams.put("nonceStr", WXPayUtil.generateNonceStr());
                payParams.put("package", "prepay_id=" + result.get("prepay_id"));
                payParams.put("signType", "MD5");
                payParams.put("paySign", WXPayUtil.generateSignature(payParams, payConfig.getWechatKey()));
                return payParams;
            } else {
                logger.error("微信支付统一下单失败: {}", result.get("return_msg"));
                return null;
            }
        } catch (Exception e) {
            logger.error("微信支付统一下单异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 处理微信支付回调
     */
    @Override
    public Map<String, String> handleWechatPayNotify(String notifyData) {
        Map<String, String> result = new HashMap<>();
        try {
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);
            logger.info("微信支付回调数据: {}", notifyMap);

            // 验证签名
            if (!WXPayUtil.isSignatureValid(notifyMap, payConfig.getWechatKey())) {
                logger.error("微信支付回调签名验证失败");
                result.put("return_code", "FAIL");
                result.put("return_msg", "签名验证失败");
                return result;
            }

            // 验证支付结果
            if (!"SUCCESS".equals(notifyMap.get("return_code")) || !"SUCCESS".equals(notifyMap.get("result_code"))) {
                logger.error("微信支付失败: {}", notifyMap.get("return_msg"));
                result.put("return_code", "FAIL");
                result.put("return_msg", "支付失败");
                return result;
            }

            // 处理订单
            String orderNo = notifyMap.get("out_trade_no");
            String transactionId = notifyMap.get("transaction_id");
            String totalFee = notifyMap.get("total_fee");
            
            // 更新订单状态为已支付
            List<ChongwuyongpinOrderEntity> orderList = chongwuyongpinOrderService.selectList(
                new com.baomidou.mybatisplus.mapper.EntityWrapper<ChongwuyongpinOrderEntity>()
                    .eq("chongwuyongpin_order_uuid_number", orderNo)
            );
            
            // 验证支付金额
            double actualAmount = Double.parseDouble(totalFee) / 100; // 转换为元
            double orderAmount = 0;
            for (ChongwuyongpinOrderEntity order : orderList) {
                orderAmount += order.getChongwuyongpinOrderTruePrice();
            }
            
            if (Math.abs(actualAmount - orderAmount) > 0.01) {
                logger.error("微信支付金额验证失败，订单号: {}, 实际支付: {}, 订单金额: {}", orderNo, actualAmount, orderAmount);
                result.put("return_code", "FAIL");
                result.put("return_msg", "金额验证失败");
                return result;
            }
            
            // 更新订单状态
            for (ChongwuyongpinOrderEntity order : orderList) {
                order.setChongwuyongpinOrderTypes(101); // 已支付
                chongwuyongpinOrderService.updateById(order);
            }
            logger.info("微信支付成功，订单号: {}, 交易号: {}, 支付金额: {}", orderNo, transactionId, actualAmount);

            result.put("return_code", "SUCCESS");
            result.put("return_msg", "OK");
            return result;
        } catch (Exception e) {
            logger.error("处理微信支付回调异常: {}", e.getMessage(), e);
            result.put("return_code", "FAIL");
            result.put("return_msg", "处理异常");
            return result;
        }
    }

    /**
     * 支付宝支付下单
     */
    @Override
    public String alipayTradePagePay(String orderNo, double amount, String subject, String body) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(
                    "https://openapi.alipaydev.com/gateway.do",
                    payConfig.getAlipayAppId(),
                    payConfig.getAlipayPrivateKey(),
                    "JSON",
                    "UTF-8",
                    payConfig.getAlipayPublicKey(),
                    "RSA2"
            );

            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setNotifyUrl(payConfig.getAlipayNotifyUrl());
            request.setReturnUrl(payConfig.getAlipayReturnUrl());

            Map<String, Object> bizContent = new HashMap<>();
            bizContent.put("out_trade_no", orderNo);
            bizContent.put("total_amount", amount);
            bizContent.put("subject", subject);
            bizContent.put("body", body);
            bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

            request.setBizContent(com.alibaba.fastjson.JSONObject.toJSONString(bizContent));
            String form = alipayClient.pageExecute(request).getBody();
            logger.info("支付宝支付表单: {}", form);
            return form;
        } catch (AlipayApiException e) {
            logger.error("支付宝支付下单异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 处理支付宝支付回调
     */
    @Override
    public boolean handleAlipayNotify(Map<String, String> params) {
        try {
            // 验证签名
            if (!verifyAlipaySign(params)) {
                logger.error("支付宝支付回调签名验证失败");
                return false;
            }

            // 验证支付结果
            if (!"TRADE_SUCCESS".equals(params.get("trade_status")) && !"TRADE_FINISHED".equals(params.get("trade_status"))) {
                logger.error("支付宝支付失败: {}", params.get("trade_status"));
                return false;
            }

            // 处理订单
            String orderNo = params.get("out_trade_no");
            String transactionId = params.get("trade_no");
            String totalAmount = params.get("total_amount");
            
            // 更新订单状态为已支付
            List<ChongwuyongpinOrderEntity> orderList = chongwuyongpinOrderService.selectList(
                new com.baomidou.mybatisplus.mapper.EntityWrapper<ChongwuyongpinOrderEntity>()
                    .eq("chongwuyongpin_order_uuid_number", orderNo)
            );
            
            // 验证支付金额
            double actualAmount = Double.parseDouble(totalAmount);
            double orderAmount = 0;
            for (ChongwuyongpinOrderEntity order : orderList) {
                orderAmount += order.getChongwuyongpinOrderTruePrice();
            }
            
            if (Math.abs(actualAmount - orderAmount) > 0.01) {
                logger.error("支付宝支付金额验证失败，订单号: {}, 实际支付: {}, 订单金额: {}", orderNo, actualAmount, orderAmount);
                return false;
            }
            
            // 更新订单状态
            for (ChongwuyongpinOrderEntity order : orderList) {
                order.setChongwuyongpinOrderTypes(101); // 已支付
                chongwuyongpinOrderService.updateById(order);
            }
            logger.info("支付宝支付成功，订单号: {}, 交易号: {}, 支付金额: {}", orderNo, transactionId, actualAmount);

            return true;
        } catch (Exception e) {
            logger.error("处理支付宝支付回调异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 验证支付宝回调签名
     */
    @Override
    public boolean verifyAlipaySign(Map<String, String> params) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(
                    "https://openapi.alipaydev.com/gateway.do",
                    payConfig.getAlipayAppId(),
                    payConfig.getAlipayPrivateKey(),
                    "JSON",
                    "UTF-8",
                    payConfig.getAlipayPublicKey(),
                    "RSA2"
            );

            // 调用SDK验证签名
            return alipayClient.checkNotifySign(params);
        } catch (Exception e) {
            logger.error("验证支付宝签名异常: {}", e.getMessage(), e);
            return false;
        }
    }
}
