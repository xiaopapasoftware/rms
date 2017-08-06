package com.thinkgem.jeesite.modules.app.service;

import com.thinkgem.jeesite.common.utils.EhCacheUtils;
import com.thinkgem.jeesite.common.utils.GenerateCode;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.exception.SmsValidCodeException;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangganggang
 * @date 2017年08月06日 上午10:07
 */
@Service
public class AppSmsMessageService {
    Logger logger = LoggerFactory.getLogger(AppSmsMessageService.class);

    private static String VALIDATE_CODE_TIMES = "VALIDATE_CODE_TIMES_";
    private static String VALIDATE_CODE = "VALIDATE_CODE_";
    private static String VALIDATE_CODE_TEMPLATE = "【唐巢公寓】尊敬的用户，您的验证码是%s，十分钟内有效。";
    private static final int VALIDATE_CODE_REQUEST_MAX_TIMES = 5;

    @Autowired
    private SmsService smsService;

    public void sendValidCode(String telPhone) {
        Object requestTime = EhCacheUtils.get(VALIDATE_CODE_TIMES + telPhone);
        int requestTimes = requestTime == null ? 0 : (int) requestTime;

        if (requestTimes >= VALIDATE_CODE_REQUEST_MAX_TIMES) {
            throw new SmsValidCodeException("短信验证码连续发送次数最多不能超过:" + VALIDATE_CODE_REQUEST_MAX_TIMES + " 次");
        }

        String code = GenerateCode.generateCode();
        String content = String.format(VALIDATE_CODE_TEMPLATE, code);
        smsService.sendSms(telPhone, content);
        /*请求次数加一*/
        requestTimes++;
         /*存放十分钟*/
        EhCacheUtils.put(VALIDATE_CODE_TIMES + telPhone, requestTimes, 10 * 60);
        EhCacheUtils.put(VALIDATE_CODE + telPhone, code, 10 * 60);
        logger.info("短信发送验证码:{}",code);
    }

    public void verifyCode(String telPhone, String validcode) {
        String code = (String) EhCacheUtils.get(VALIDATE_CODE + telPhone);
        if (!StringUtils.equals(code, validcode)) {
            throw new SmsValidCodeException("短信验证码错误，请重新获取");
        }
        EhCacheUtils.remove(VALIDATE_CODE + telPhone);
    }

}
