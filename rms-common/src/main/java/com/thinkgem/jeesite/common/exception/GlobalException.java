package com.thinkgem.jeesite.common.exception;


/**
 * @author wangganggang
 * @date 2017/04/07
 */
public class GlobalException extends RuntimeException {

    private int code;

    public GlobalException(int code) {
        super("error code:" + code);
    }

    public GlobalException(int code,String message) {
        super(message);
        this.code = code;
    }


    public GlobalException(int code, Throwable cause) {
        super("error code:" + code, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
