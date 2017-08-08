package com.thinkgem.jeesite.common.exception;

/**
 * @author wangganggang
 * @date 2017年08月06日 上午10:30
 */
public class SmsValidCodeException extends GlobalException {

    public SmsValidCodeException(int code, String message) {
        super(code, message);
    }
}
