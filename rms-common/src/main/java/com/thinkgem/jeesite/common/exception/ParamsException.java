package com.thinkgem.jeesite.common.exception;

import com.thinkgem.jeesite.common.RespConstants;

/**
 * @author wangganggang
 * @date 2017年08月06日 下午3:16
 */
public class ParamsException extends GlobalException {

    public ParamsException() {
        super(RespConstants.ERROR_CODE_101);
    }

    public ParamsException(int code) {
        super(code);
    }

    public ParamsException(String message) {
        super(RespConstants.ERROR_CODE_101, message);
    }


    public ParamsException(int code, String message) {
        super(code, message);
    }

    public ParamsException(Throwable cause) {
        super(RespConstants.ERROR_CODE_101, cause);
    }

}
