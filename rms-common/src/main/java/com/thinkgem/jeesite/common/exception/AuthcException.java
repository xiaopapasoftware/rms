package com.thinkgem.jeesite.common.exception;

/**
 * @author wangganggang
 * @date 2017年08月06日 上午10:30
 */
public class AuthcException extends GlobalException {

    public AuthcException(int code, String message) {
        super(code, message);
    }
}
