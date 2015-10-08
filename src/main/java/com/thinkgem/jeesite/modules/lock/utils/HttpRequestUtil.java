package com.thinkgem.jeesite.modules.lock.utils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;

public class HttpRequestUtil {
	public static final String GET_URL = "http://115.28.141.204:8090/openapi/v1";

	public static final String POST_URL = "http://115.28.141.204:8090/openapi/v1";
	
	private static Logger logger = Logger.getLogger(HttpRequestUtil.class);

	public static String readContentFromGet(String url, String method,  Map<String, Object> paramsMap) throws IOException {
		logger.debug("=============" + method);
		logger.debug("Get params:" + paramsMap);
		// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
		//String getURL = GET_URL + "&activatecode=" + URLEncoder.encode("aaa", "utf-8");
		String fullUrl = url + "/" + method + "/" + "?random="+Math.random();
		for (String key : paramsMap.keySet()) {
			  fullUrl = fullUrl + "&" + key + "=" + paramsMap.get(key);
		}
		URL getUrl = new URL(fullUrl);
		// 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
		// 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
		HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
		// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
		// 服务器
		connection.connect();
		// 取得输入流，并使用Reader读取
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));// 设置编码,否则中文乱码
		
		String lines;
		if ((lines = reader.readLine()) != null) {
			logger.debug("Get response:" + lines);
		}
		reader.close();
		// 断开连接
		connection.disconnect();
		
		return lines;
	}

	public static String readContentFromPost(String url, String method, String content) throws IOException {
		logger.debug("==============" + method);
		logger.debug("Post params:" + content);
		// Post请求的url，与get不同的是不需要带参数
		
		URL postUrl = new URL(url + "/" + method + "/");
		// 打开连接
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();		
		// connection
		// 设置是否向connection输出，因为这个是post请求，参数要放在
		// http正文内，因此需要设为true
		connection.setDoOutput(true);
		// Read from the connection. Default is true.
		connection.setDoInput(true);
		// Set the post method. Default is GET
		connection.setRequestMethod("POST");
		// Post cannot use caches
		// Post 请求不能使用缓存
		connection.setUseCaches(false);
		// This method takes effects to
		// every instances of this class.
		// URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
		// connection.setFollowRedirects(true);

		// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
		connection.setInstanceFollowRedirects(true);
		
		// 配置本次连接的Content-type，
		connection.setRequestProperty("Content-Type", "application/json");
		// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
		// 要注意的是connection.getOutputStream会隐含的进行connect。
		connection.connect();
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		// The URL-encoded contend
		// 正文，正文内容其实跟get的URL中'?'后的参数字符串一致
		//String content = "{\"client_id\": \"4641dc274ed14ff9184755d9\",\"client_secret\": \"698d57e3c491c4f1c7176481eff94793\"}";

		// DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
		out.writeBytes(content);
		out.flush();
		out.close(); // flush and close
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));// 设置编码,否则中文乱码
		String line = "";

		if ((line = reader.readLine()) != null) {
			logger.debug("Post response:" + line);
		}
		
		reader.close();
		connection.disconnect();
		return line;
	}
	
	
}