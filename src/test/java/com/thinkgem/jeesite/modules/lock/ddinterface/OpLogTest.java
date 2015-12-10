package com.thinkgem.jeesite.modules.lock.ddinterface;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.modules.lock.utils.HttpRequestUtil;

public class OpLogTest {
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//@Test
	public void searchDeviceOpLog() throws ParseException {
		String token = readToken();
		Map<String, Object> map = Maps.newHashMap();
		map.put("access_token", token);
		map.put("home_id", "tangchao_test_0929_001");
		map.put("room_id", "tangchao_test_0929_001");
		
		//String json = JsonMapper.getInstance().toJson(map);
		try {
			String s  = HttpRequestUtil.readContentFromGet("http://115.28.141.204:8090/openapi/v1" ,"search_device_op_log", map);
			Map res = (Map) JsonMapper.fromJsonString(s, Map.class);
			if(0==(Integer)res.get("ErrNo")){
				
			}else
			{
				fail((String)res.get("ErrMsg"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
	
	public String readToken() {
		  try {
		   FileInputStream fs = new FileInputStream("token.ser");//("foo.ser");
		   ObjectInputStream ois = new ObjectInputStream(fs);
		   Map tokenMap = (Map) ois.readObject();
		   
		   //System.out.println(tokenMap);
		   
		   ois.close();
		   return (String) tokenMap.get("access_token");
		  } catch (Exception ex) {
		   ex.printStackTrace();
		   fail(ex.getMessage());
		  }
		return null;
		 }
}
