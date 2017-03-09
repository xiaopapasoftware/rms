package com.thinkgem.jeesite.modules.lock.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
	

	
	public static void main(String[] args) {
		String json = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost("https://api.sciener.cn/v1/user/info");
			// 创建参数队列
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("redirect_url", "http://218.80.0.218:12301/rms/lock"));
                formparams.add(new BasicNameValuePair("client_id", "9603ccd6414a4a6ebe1f69a20eb455ef"));
                formparams.add(new BasicNameValuePair("response_type", "token"));
                formparams.add(new BasicNameValuePair("username", "15618820709"));
                formparams.add(new BasicNameValuePair("password", "14d5cef31a2fccde671fea8106c27e2e"));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			//StringEntity entity = new StringEntity("abc".toString(),"utf-8");
//			entity.setContentEncoding("UTF-8");    
//			entity.setContentType("application/x-www-form-urlencoded");
			post.setEntity(entity);
			System.out.println(post.getURI());
			HttpResponse result = httpClient.execute(post);
			System.out.println(result); 
			json = EntityUtils.toString(result.getEntity(),"UTF-8");
			log.debug(json);
		} catch (Exception e) {
			System.out.println(e);
			log.error("call dopost error:",e);
		}
		//return json;
		
	}
}
