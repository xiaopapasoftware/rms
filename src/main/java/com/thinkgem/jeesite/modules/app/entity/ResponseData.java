package com.thinkgem.jeesite.modules.app.entity;

import java.io.Serializable;

public class ResponseData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7718224551196012271L;
	private String code;
	private String msg;
	private String data;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
