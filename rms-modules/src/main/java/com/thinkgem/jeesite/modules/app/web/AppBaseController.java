package com.thinkgem.jeesite.modules.app.web;

import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.app.exception.ParamsException;
import com.thinkgem.jeesite.modules.app.exception.SmsValidCodeException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wangganggang
 * @date 2017年08月05日 下午2:55
 */
public class AppBaseController {

    /**
     * token验证失效异常
     */
    @ExceptionHandler({ExpiredCredentialsException.class})
    @ResponseBody
    public ResponseData bindException(ExpiredCredentialsException exception) {
        ResponseData data = new ResponseData();
        data.setCode("401");
        data.setMsg(exception.getMessage());
        return data;
    }

    /**
     * 短信验证异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({SmsValidCodeException.class})
    @ResponseBody
    public ResponseData bindException(SmsValidCodeException exception) {
        ResponseData data = new ResponseData();
        data.setCode("402");
        data.setMsg(exception.getMessage());
        return data;
    }

    /**
     * 参数绑定异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({ParamsException.class})
    @ResponseBody
    public ResponseData bindException(ParamsException exception) {
        ResponseData data = new ResponseData();
        data.setCode(exception.getCode());
        data.setMsg(exception.getMessage());
        return data;
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseData bindException(Exception exception) {
        ResponseData data = new ResponseData();
        data.setCode("500");
        data.setMsg("服务器异常，稍后再试");
        return data;
    }
}
