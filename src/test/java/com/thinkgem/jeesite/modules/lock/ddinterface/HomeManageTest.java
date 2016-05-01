//package com.thinkgem.jeesite.modules.lock.ddinterface;
//
//import static org.junit.Assert.fail;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.google.common.collect.Maps;
//import com.thinkgem.jeesite.common.mapper.JsonMapper;
//import com.thinkgem.jeesite.modules.lock.utils.HttpRequestUtil;
//
//public class HomeManageTest {
//
//	//@Test
//	public void delHome() {
//		String token = readToken();
//		Map<String, Object> map = Maps.newHashMap();
//		map.put("access_token", token);
//		map.put("home_id", "tangchao_test_0929_001");
//			String json = JsonMapper.getInstance().toJson(map);
//		try {
//			String s  = HttpRequestUtil.readContentFromPost("http://115.28.141.204:8090/openapi/v1","del_home", json);
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
//	public void addHome() {
//		String token = readToken();
//		Map<String, Object> map = Maps.newHashMap();
//		map.put("access_token", token);
//		map.put("country", "CHINA");
//		map.put("city", "Shanghai");
//		map.put("zone", "Pudong");
//		map.put("location", "Tangzhen");
//		map.put("home_id", "tangchao_test_0929_001");
//		map.put("home_name", "tangchao_test_0929_name001");
//		map.put("description", "tangchao_test_0929_001");
////		map.put("home_id", "tangchao_test_0929_002");
////		map.put("home_name", "tangchao_test_0929_name002");
////		map.put("description", "tangchao_test_0929_002");
//			String json = JsonMapper.getInstance().toJson(map);
//		try {
//			String s  = HttpRequestUtil.readContentFromPost("http://115.28.141.204:8090/openapi/v1","add_home", json);
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
//	public void addRoom() {
//		String token = readToken();
//		Map<String, Object> map = Maps.newHashMap();
//		map.put("access_token", token);
//		map.put("home_id", "tangchao_test_0929_001");
//		map.put("room_id", "001_A");
//		map.put("room_name", "ROOM A");
//		map.put("room_description", "ROOM A desc");
//			String json = JsonMapper.getInstance().toJson(map);
//		try {
//			String s  = HttpRequestUtil.readContentFromPost("http://115.28.141.204:8090/openapi/v1","add_room", json);
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
//
//
//
//	public String readToken() {
//		  try {
//		   FileInputStream fs = new FileInputStream("token.ser");//("foo.ser");
//		   ObjectInputStream ois = new ObjectInputStream(fs);
//		   Map tokenMap = (Map) ois.readObject();
//
//		   System.out.println(tokenMap);
//
//		   ois.close();
//		   return (String) tokenMap.get("access_token");
//		  } catch (Exception ex) {
//		   ex.printStackTrace();
//		  }
//		return null;
//		 }
//}
