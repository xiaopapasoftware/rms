package com.thinkgem.jeesite.common;

/**
 * @author wangganggang
 * @date 2017年08月07日 17:31
 */
public class RespConstants {

    public static final int SUCCESS_CODE_200 = 200;
    public static final String SUCCESS_MSG_200 = "操作成功";
    /*参数不对错误码*/
    public static final int ERROR_CODE_101 = 101;
    public static final String ERROR_MSG_101 = "传递参数不对";

    public static final int ERROR_CODE_400 = 400;
    public static final String ERROR_MSG_400 = "请填写身份证号码";

    public static final int ERROR_CODE_300 = 300;
    public static final String ERROR_MSG_300 = "短信验证码连续发送次数超限";
    public static final int ERROR_CODE_301 = 301;
    public static final String ERROR_MSG_301 = "短信验证码错误";


    public static final int ERROR_CODE_500 = 500;
    public static final String ERROR_MSG_500 = "服务器异常，稍后再试";


}
