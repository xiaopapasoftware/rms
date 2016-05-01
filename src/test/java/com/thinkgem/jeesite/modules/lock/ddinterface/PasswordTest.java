//package com.thinkgem.jeesite.modules.lock.ddinterface;
//
//import static org.junit.Assert.fail;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.google.common.collect.Maps;
//import com.thinkgem.jeesite.common.mapper.JsonMapper;
//import com.thinkgem.jeesite.modules.lock.utils.HttpRequestUtil;
//
//public class PasswordTest {
//
//	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//	//@Test
//	public void addPassword() throws ParseException {
//		String token = readToken();
//		Map<String, Object> map = Maps.newHashMap();
//		map.put("access_token", token);
//		map.put("home_id", "tangchao_test_0929_001");
//		map.put("room_id", "tangchao_test_0929_001");
//		map.put("phonenumber", "15618820709");
//		map.put("is_default", 0);
//		map.put("password", "098765");
//		map.put("name", "newPwd3");
//		map.put("description", "customerpwd");
//		String start = "2015-10-02 13:45:45";
//		String end = "2015-10-03 13:45:45";
//
//		map.put("permission_begin", sdf.parse(start).getTime());
//		map.put("permission_end", sdf.parse(end).getTime());
//
//		String json = JsonMapper.getInstance().toJson(map);
//		try {
//			String s  = HttpRequestUtil.readContentFromPost("http://115.28.141.204:8090/openapi/v1" ,"add_password", json);
//			Map res = (Map) JsonMapper.fromJsonString(s, Map.class);
//			if(0==(Integer)res.get("ErrNo")){
//
//			}else
//			{
//				fail((String)res.get("ErrMsg"));
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			fail(e.getMessage());
//		}
//	}
//
//	//@Test
//	public void getLockEvents() {
//		String token = readToken();
//		Map<String, Object> map = Maps.newHashMap();
//		map.put("access_token", token);
//		map.put("home_id", "tangchao_test_0929_001");
//		map.put("room_id", "tangchao_test_0929_001");
//		try {
//			String s  = HttpRequestUtil.readContentFromGet("http://115.28.141.204:8090/openapi/v1","get_lock_events", map);
//			Map res = (Map) JsonMapper.fromJsonString(s, Map.class);
//			if(0==(Integer)res.get("ErrNo")){
//
//			}else
//			{
//				fail((String)res.get("ErrMsg"));
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			fail(e.getMessage());
//		}
//	}
//
//
//	public String readToken() {
//		  try {
//		   FileInputStream fs = new FileInputStream("token.ser");//("foo.ser");
//		   ObjectInputStream ois = new ObjectInputStream(fs);
//		   Map tokenMap = (Map) ois.readObject();
//
//		   //System.out.println(tokenMap);
//
//		   ois.close();
//		   return (String) tokenMap.get("access_token");
//		  } catch (Exception ex) {
//		   ex.printStackTrace();
//		   fail(ex.getMessage());
//		  }
//		return null;
//		 }
//}
