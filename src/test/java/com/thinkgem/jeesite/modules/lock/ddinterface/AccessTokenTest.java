package com.thinkgem.jeesite.modules.lock.ddinterface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.modules.lock.utils.HttpRequestUtil;

public class AccessTokenTest {

	@Test
	public void fetchAccessToken() {
		//List<Map<String, Object>> list = Lists.newArrayList();
		Map<String, Object> map = Maps.newHashMap();
		map.put("client_id", "4641dc274ed14ff9184755d9");
		map.put("client_secret", "698d57e3c491c4f1c7176481eff94793");
		//list.add(map);
		String json = JsonMapper.getInstance().toJson(map);
		System.out.println(json);
		//String content = "{\"client_id\": \"4641dc274ed14ff9184755d9\",\"client_secret\": \"698d57e3c491c4f1c7176481eff94793}");
		try {
			String s  = HttpRequestUtil.readContentFromPost(HttpRequestUtil.POST_URL, json);
			Map res = (Map) JsonMapper.fromJsonString(s, Map.class);
			System.out.println(res);
			FileOutputStream fs = new FileOutputStream("token.ser"); 
			ObjectOutputStream os = new ObjectOutputStream(fs);  
			os.writeObject(res);
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
