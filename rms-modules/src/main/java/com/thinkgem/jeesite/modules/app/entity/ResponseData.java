package com.thinkgem.jeesite.modules.app.entity;

import java.io.Serializable;

public class ResponseData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7718224551196012271L;
	private String code;
	private String msg;
	private Object data;

	public ResponseData(){
		this.code = "200";
		this.msg = "success";
	}

	public ResponseData(String code,String message){
		this.code = code;
		this.msg = message;
	}

	public ResponseData(Object data){
		this.code = "200";
		this.msg = "success";
		this.data = data;
	}

	public static ResponseData success(){
		return new ResponseData();
	}

	public static ResponseData failure(String code){
		return new ResponseData().code(code);
	}

	public ResponseData message(String message){
		this.msg = message;
		return this;
	}

	public ResponseData code(String  code){
		this.code = code;
		return this;
	}

	public ResponseData data(Object data){
		this.data = data;
		return this;
	}
	
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
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}


}
