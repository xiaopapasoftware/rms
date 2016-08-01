package com.thinkgem.jeesite.modules.app.alipay;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AlipayUtil {
	static Logger log = LoggerFactory.getLogger(AlipayUtil.class);
	
	public static final String PARTNER = "2088811545435611"; 
	private static final String SELLER_EMAIL = "tcrcgy@126.com";
	private static final String NOTIFY_URL = "http://rms.tangroom.com:12301/rms-api/house/alipaynNotify";
	
	public static void main(String[] args) throws Exception {
		System.out.println(buildRequest("57620160105-2234532","0.01"));
	}
	
	public static String buildRequest(String orderId,String totalFee) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("partner", PARTNER);
		map.put("seller_id", SELLER_EMAIL);
		map.put("out_trade_no", orderId);
		map.put("subject", "唐巢人才公寓");
		map.put("body", "唐巢人才公寓APP支付");
		map.put("total_fee", totalFee);
		map.put("notify_url", NOTIFY_URL);
		map.put("service", "mobile.securitypay.pay");
		map.put("payment_type", "1");
        map.put("_input_charset", "utf-8");
        map.put("it_b_pay", "10m");
        map.put("show_url", "m.alipay.com");
		
        String params = AlipayCore.createLinkString(map);
        String sign = URLEncoder.encode(RSA.sign(params, AlipayConfig.private_key, AlipayConfig.input_charset),AlipayConfig.input_charset);
        map.put("sign", sign);
		map.put("sign_type", "RSA");
        String result = AlipayCore.createLinkString(map);
        return result;
	}
}