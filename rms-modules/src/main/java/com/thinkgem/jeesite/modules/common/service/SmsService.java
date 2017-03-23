package com.thinkgem.jeesite.modules.common.service;

import java.security.MessageDigest;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.utils.PropertiesLoader;

@Service
public class SmsService {
    Logger log = LoggerFactory.getLogger(SmsService.class);

    public String sendSms(String phones, String content) {
	try {
	    PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
	    String sms_url = proper.getProperty("sms.url");
	    String account = proper.getProperty("sms.account");
	    String password = proper.getProperty("sms.password");
	    DefaultHttpClient httpClient = new DefaultHttpClient();
	    HttpPost post = new HttpPost(sms_url);
	    JSONObject jsonParam = new JSONObject();
	    jsonParam.put("account", account);
	    jsonParam.put("password", md5(password));
	    jsonParam.put("msgid", UUID.randomUUID().toString().replaceAll("-", ""));
	    jsonParam.put("phones", phones);
	    jsonParam.put("content", content);
	    jsonParam.put("sign", "【唐巢公寓】");
	    log.debug("签名之后发送内容:" + jsonParam.toString());
	    StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
	    entity.setContentEncoding("UTF-8");
	    entity.setContentType("application/json");
	    post.setEntity(entity);
	    HttpResponse result = httpClient.execute(post);
	    String resData = EntityUtils.toString(result.getEntity(), "UTF-8");
	    log.info("resData:" + resData);
	} catch (Exception e) {
	    log.error("send sms error:", e);
	}
	return "SMS";
    }

    private String md5(String password) throws Exception {
	String result = "";
	MessageDigest md = MessageDigest.getInstance("MD5");
	byte bytes[] = md.digest(password.getBytes());
	for (int i = 0; i < bytes.length; i++) {
	    String str = Integer.toHexString(bytes[i] & 0xFF);
	    if (str.length() == 1) {
		str += "F";
	    }
	    result += str;
	}
	return result;
    }
}
