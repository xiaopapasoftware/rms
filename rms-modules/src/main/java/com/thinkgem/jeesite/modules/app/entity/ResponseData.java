package com.thinkgem.jeesite.modules.app.entity;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.RespConstants;
import com.thinkgem.jeesite.common.persistence.Page;

import java.io.Serializable;
import java.util.Map;

public class ResponseData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7718224551196012271L;
    private int code;
    private String msg;
    private Object data;

    public ResponseData() {
        this.code = RespConstants.SUCCESS_CODE_200;
        this.msg = RespConstants.SUCCESS_MSG_200;
    }

    public ResponseData(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public ResponseData(Object data) {
        this.code = RespConstants.SUCCESS_CODE_200;
        this.msg = RespConstants.SUCCESS_MSG_200;
        this.data = data;
    }

    public static ResponseData success() {
        return new ResponseData();
    }

    public static ResponseData failure(int code) {
        return new ResponseData().code(code);
    }

    public ResponseData message(String message) {
        this.msg = message;
        return this;
    }

    public ResponseData code(int code) {
        this.code = code;
        return this;
    }

    public ResponseData data(Object data) {
        this.data = data;
        return this;
    }

    public Map page(Page page){
        Map map = Maps.newHashMap();
        map.put("code","0");
        map.put("count",page.getCount());
        map.put("data",page.getList());
        return map;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
