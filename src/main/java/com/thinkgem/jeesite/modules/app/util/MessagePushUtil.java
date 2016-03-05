package com.thinkgem.jeesite.modules.app.util;

import com.tencent.xinge.Message;
import com.tencent.xinge.XingeApp;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagePushUtil {

    private static Logger log = LoggerFactory.getLogger(MessagePushUtil.class);
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
		//XingeApp.pushAccountAndroid(androidAccessId, androidSecretKey, title, content, account);
        //发送ios消息
        try {
            XingeApp.pushAccountIos(iosAccessId, iosSecretKey, content, account, XingeApp.IOSENV_PROD);
        }catch(Exception e){
            log.error("pushAccountIos error: " + e);
        }
        //发送android消息
        try {
            Message message = new Message();
            message.setType(2);
            message.setTitle(title);
            message.setContent(content);
            XingeApp xinge = new XingeApp(androidAccessId, androidSecretKey);
            JSONObject ret = xinge.pushSingleAccount(0, account, message);
        }catch(Exception e){
            log.error("pushAccountandroid error: " + e);
        }

	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
