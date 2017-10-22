package com.thinkgem.jeesite.modules.app.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
	private static Logger log = LoggerFactory.getLogger(JsonUtil.class);
	
	public static String object2Json(Object object) {
		String json = "";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			json = objectMapper.writeValueAsString(object);
			log.info(json);
		} catch (Exception e) {
			log.error("parse json error:",e);
		}
		return  json;
	}

	public static <T> T jsonToCollection(String src,Class<?> collectionClass, Class<?>... valueType)
			throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType javaType= objectMapper.getTypeFactory().constructParametricType(collectionClass, valueType);
		return (T)objectMapper.readValue(src, javaType);
	}
}
