package com.thinkgem.jeesite.modules.app.exception;

/**
 * @author wangganggang
 * @date 2017年08月06日 下午3:16
 */
public class ParamsException extends RuntimeException {
    private String code;

    public ParamsException(String message) {
        super(message);
    }

    public ParamsException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
