package com.tangchao.common.response;

/**
 * @author wangganggang
 * @date 2017年08月30日 15:24
 */
public enum RespEnums {

    SUCCESS(1, "处理成功!"),

    FAILURE(0,"处理失败!"),

    UNAUTHORIZED(401,"回话过期,请重新登录");


    private int code;

    private String message;

    RespEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
