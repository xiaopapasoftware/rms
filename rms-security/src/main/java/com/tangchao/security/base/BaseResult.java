package com.tangchao.security.base;

/**
 * @author wangganggang
 * @date 2017年08月30日 15:24
 */
public class BaseResult {

    /**
     * 状态码：1成功，其他为失败
     **/
    protected int code;

    /**
     * 成功为success，其他为失败原因
     **/
    protected String message;

    /**
     * 数据结果集
     **/
    protected Object data;

    public BaseResult() {
        this.code = RespEnums.SUCCESS.code();
        this.message = "success";
    }

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResult(Object data) {
        this.code = RespEnums.SUCCESS.code();
        this.message = "success";
        this.data = data;
    }

    public static BaseResult success() {
        return new BaseResult();
    }

    public static BaseResult success(String message) {
        return new BaseResult().message(message);
    }

    public static BaseResult failure() {
        return new BaseResult().code(RespEnums.FAILURE.code());
    }

    public static BaseResult failure(String message) {
        return new BaseResult().code(RespEnums.FAILURE.code()).message(message);
    }

    public BaseResult message(String message) {
        this.message = message;
        return this;
    }

    public BaseResult code(int code) {
        this.code = code;
        return this;
    }

    public BaseResult data(Object data) {
        this.data = data;
        return this;
    }
}
