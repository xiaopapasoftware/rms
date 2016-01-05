package com.thinkgem.jeesite.modules.app.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlipayUtil {
    static Logger log = LoggerFactory.getLogger(AlipayUtil.class);

    private static final String PARTNER = "2088811545435611";
    private static final String SELLER_EMAIL = "tangchaotouzi@126.com";
    private static final String NOTIFY_URL = "http://218.80.0.218:12301/rms-api/house/alipaynNotify";
    private static final String RETURN_URL = "http://218.80.0.218:12301/rms-api/house/alipayReturn";

    public static String buildRequest(String orderId, String totalFee) {
	Map<String, String> sParaTemp = new LinkedHashMap<String, String>();
	sParaTemp.put("partner", PARTNER);
	sParaTemp.put("seller_id", SELLER_EMAIL);
	sParaTemp.put("out_trade_no", "" + orderId + "");
	sParaTemp.put("subject", "唐巢人才公寓");
	sParaTemp.put("body", "唐巢人才公寓APP支付");
	sParaTemp.put("total_fee", "" + totalFee + "");
	sParaTemp.put("notify_url", NOTIFY_URL);
	sParaTemp.put("return_url", RETURN_URL);
	sParaTemp.put("service", "mobile.securitypay.pay");
	sParaTemp.put("payment_type", "1");
	sParaTemp.put("_input_charset", "utf-8");

	Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
	return buildRequestMysign(sPara);
	// sPara.put("sign", mysign);
	// sPara.put("sign_type", AlipayConfig.sign_type);
	//
	// String signStr = "";
	// List<String> keys = new ArrayList<String>(sPara.keySet());
	// for (int i = 0; i < keys.size(); i++) {
	// String name = (String) keys.get(i);
	// String value = (String) sPara.get(name);
	// signStr += name + "=" + value + "&";
	// }
	// if(StringUtils.endsWith(signStr,"&"))
	// signStr = StringUtils.substringBeforeLast(signStr, "&");

	// StringBuffer sbHtml = new StringBuffer();

	// sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\"
	// action=\"https://mapi.alipay.com/gateway.do?_input_charset=utf-8\"
	// method=\"POST\">");
	//
	// for (int i = 0; i < keys.size(); i++) {
	// String name = (String) keys.get(i);
	// String value = (String) sPara.get(name);
	//
	// sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\""
	// + value + "\"/>");
	// }
	//
	// //submit按钮控件请不要含有name属性
	// sbHtml.append("<input type=\"submit\" value=\"支付\"
	// style=\"display:none;\"></form>");
	// sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
	//
	// signStr = sbHtml.toString();
    }

    private static String buildRequestMysign(Map<String, String> sPara) {
	String prestr = AlipayCore.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

	try {
	    prestr = URLEncoder.encode(prestr, "utf-8");
	} catch (UnsupportedEncodingException e1) {
	    log.error("encode param error:", e1);
	}
	String mysign = "";
	try {
	    String source = prestr;
	    byte[] encodedData = RSA.encryptByPrivateKey(source.getBytes("utf-8"), AlipayConfig.private_key);
	    mysign = RSA.sign(encodedData, AlipayConfig.private_key);
	} catch (Exception e) {
	    log.error("sign error:", e);
	}
	return prestr + "&sign=\"" + mysign + "\"&sign_type=\"RSA\"";
	// try {
	// prestr = URLEncoder.encode(prestr, "utf-8");
	// } catch (UnsupportedEncodingException e1) {
	// log.error("encode param error:", e1);
	// }
	// String mysign = "";
	//
	// try {
	// String source = prestr;
	// byte[] encodedData = RSA.encryptByPrivateKey(source.getBytes(),
	// AlipayConfig.private_key);
	// mysign = RSA.sign(encodedData, AlipayConfig.private_key);
	// } catch (Exception e) {
	// log.error("sign error:", e);
	// }
	// return mysign;
    }
}
