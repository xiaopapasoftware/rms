package com.thinkgem.jeesite.modules.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

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
}
