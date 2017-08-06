package com.thinkgem.jeesite.modules.app.exception;

/**
 * @author wangganggang
 * @date 2017年08月06日 上午10:30
 */
public class SmsValidCodeException extends RuntimeException {
    public SmsValidCodeException(String message) {
        super(message);
    }
}
