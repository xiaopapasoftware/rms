package com.thinkgem.jeesite.modules.common.web;

/**
 * 页面消息类别
 * 
 * @author wsjerome
 * */
public enum ViewMessageTypeEnum {

	INFO("info"),

	SUCCESS("success"),

	WARNING("warning"),

	ERROR("error"),

	LOADING("loading");

	ViewMessageTypeEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}

}
