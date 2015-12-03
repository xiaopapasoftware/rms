package com.thinkgem.jeesite.modules.app.alipay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class AlipayUtil {
	private static final String PARTNER = "2088811545435611"; 
	private static final String SELLER_EMAIL = "tangchaotouzi@126.com";
	private static final String NOTIFY_URL = "http://218.80.0.218:12301/rms-api/house/alipaynNotify";
	private static final String RETURN_URL = "http://218.80.0.218:12301/rms-api/house/alipayReturn";

	public static String buildRequest(String orderId,String totalFee) {
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "mobile.securitypay.pay");
        sParaTemp.put("partner", PARTNER);
        sParaTemp.put("seller_email", SELLER_EMAIL);
        sParaTemp.put("_input_charset", "utf-8");
		sParaTemp.put("payment_type", "1");
		sParaTemp.put("notify_url", NOTIFY_URL);
		sParaTemp.put("return_url", RETURN_URL);
		sParaTemp.put("out_trade_no", orderId);
		sParaTemp.put("subject", "唐巢人才公寓");
		sParaTemp.put("total_fee", totalFee);
		sParaTemp.put("body", "唐巢人才公寓APP支付");
		
		Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
		String mysign = buildRequestMysign(sPara);
		sPara.put("sign", mysign);
        sPara.put("sign_type", AlipayConfig.sign_type);
        
        String signStr = "";
        List<String> keys = new ArrayList<String>(sPara.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);
            signStr += name + "=" + value + "&";
        }
        if(StringUtils.endsWith(signStr,"&"))
        	signStr = StringUtils.substringBeforeLast(signStr, "&");
        
        return signStr;
	}
	
	private static String buildRequestMysign(Map<String, String> sPara) {
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(AlipayConfig.sign_type.equals("MD5") ) {
        	mysign = MD5.sign(prestr, AlipayConfig.key, AlipayConfig.input_charset);
        }
        return mysign;
    }
}
