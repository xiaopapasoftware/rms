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
    public static final int ERROR_CODE_103 = 103;
    public static final String ERROR_MSG_103 = "手机号码不能为空";
    public static final int ERROR_CODE_104 = 104;
    public static final String ERROR_MSG_104 = "缺少锁平台账号,请联系客服";
    public static final int ERROR_CODE_105 = 105;
    public static final String ERROR_MSG_105 = "您预定的房源已签约";
    public static final int ERROR_CODE_106 = 106;
    public static final String ERROR_MSG_106 = "本合同已被续签";
    public static final int ERROR_CODE_107 = 107;
    public static final String ERROR_MSG_107 = "当前合同还有未结清的款项,暂不能续签";
    public static final int ERROR_CODE_108 = 108;
    public static final String ERROR_MSG_108 = "因当前合同状态非法,暂不支持续签,请联系客服";
    public static final int ERROR_CODE_109 = 109;
    public static final String ERROR_MSG_109 = "当前信息不存在,查询后在操作";
    public static final int ERROR_CODE_110 = 110;
    public static final String ERROR_MSG_110 = "您已预约该房间,不能重复预约";



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
    public static final int ERROR_CODE_405 = 405;
    public static final String ERROR_MSG_405 = "当前用户已存在";
    public static final int ERROR_CODE_406 = 406;
    public static final String ERROR_MSG_406 = "用户名/密码有误";


    public static final int ERROR_CODE_500 = 500;
    public static final String ERROR_MSG_500 = "服务器异常，稍后再试";


}
