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
    public static final String ERROR_MSG_101 = "自定义说明";

    public static final int ERROR_CODE_102 = 102;
    public static final String ERROR_MSG_102 = "请填写身份证号码";

    public static final int ERROR_CODE_300 = 300;
    public static final String ERROR_MSG_300 = "短信验证码连续发送次数超限";
    public static final int ERROR_CODE_301 = 301;
    public static final String ERROR_MSG_301 = "短信验证码错误";

    /*权限*/
    public static final int ERROR_CODE_401 = 401;
    public static final String ERROR_MSG_401 = "当前用户未登录";
    public static final int ERROR_CODE_402 = 402;
    public static final String ERROR_MSG_402 = "token失效,请重新登录";
    public static final int ERROR_CODE_403 = 403;
    public static final String ERROR_MSG_403 = "当前用户不存在,请重新注册";


    public static final int ERROR_CODE_500 = 500;
    public static final String ERROR_MSG_500 = "服务器异常，稍后再试";


}
