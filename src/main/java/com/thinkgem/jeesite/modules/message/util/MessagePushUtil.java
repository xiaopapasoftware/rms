package com.thinkgem.jeesite.modules.message.util;

import com.tencent.xinge.XingeApp;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;

public class MessagePushUtil {

	private static long androidAccessId;
	private static String androidSecretKey;
	private static long iosAccessId;
	private static String iosSecretKey;
	
	static{
		PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
		androidAccessId = Long.parseLong(proper.getProperty("app.android.accessid"));
		androidSecretKey = proper.getProperty("app.android.secretkey");
		iosAccessId = Long.parseLong(proper.getProperty("app.ios.accessid"));
		iosSecretKey = proper.getProperty("app.ios.secretkey");
	}
	
	public static void pushAccount(String title, String content, String account){
		XingeApp.pushAccountAndroid(androidAccessId, androidSecretKey, title, content, account);
		XingeApp.pushAccountIos(iosAccessId, iosSecretKey, content, account, XingeApp.IOSENV_PROD);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
