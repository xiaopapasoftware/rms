package com.thinkgem.jeesite.modules.app.web;

import java.security.MessageDigest;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.*;
import com.thinkgem.jeesite.modules.app.service.MessageService;
import com.thinkgem.jeesite.modules.lock.service.ScienerLockService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.thinkgem.jeesite.modules.app.service.AppTokenService;
import com.thinkgem.jeesite.modules.app.service.AppUserService;
import com.thinkgem.jeesite.modules.app.service.TAppCheckCodeService;
import com.thinkgem.jeesite.modules.app.util.JsonUtil;
import com.thinkgem.jeesite.modules.app.util.RandomStrUtil;
import com.thinkgem.jeesite.modules.common.service.SmsService;

@Controller
@RequestMapping("system")
public class SystemController {
    Logger log = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private TAppCheckCodeService tAppCheckCodeService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppTokenService appTokenService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ScienerLockService scienerLockService;

    @RequestMapping(value="checkToken")
    @ResponseBody
    public String checkToken(HttpServletRequest request, HttpServletResponse response, Model model) {
        String res;
        try {
            String token = (String) request.getHeader("token");
            log.debug("in checkToken. token:" + token);
            //验证token是否存在
            AppToken appToken = new AppToken();
            appToken.setToken(token);
            appToken.setExprie(new Date());
            appToken = appTokenService.findByTokenAndExpire(appToken);
            if (appToken != null)
                res = appToken.getPhone();
            else
                res = null;
            log.debug("in chekcToken. return phone:" + res);
        }catch(Exception e){
            res= null;
            log.error("chekcToken error:", e);
        }
        return res;
    }


    @RequestMapping(value="register")
    @ResponseBody
    public ResponseData register(HttpServletRequest request, HttpServletResponse response, Model model) {

        ResponseData data = new ResponseData();
        try {
            String mobile = (String) request.getParameter("mobile");
            String code = (String) request.getParameter("code");
            String password = (String) request.getParameter("password");

            TAppCheckCode tAppCheckCode = new TAppCheckCode();
            tAppCheckCode.setPhone(mobile);
            tAppCheckCode.setCode(code);
            tAppCheckCode.setExprie(new Date());
            boolean isValidCheckCode = tAppCheckCodeService.verifyCheckCode(tAppCheckCode);
            if (isValidCheckCode) {
                //exist?
                AppUser appUser = new AppUser();
                appUser.setPhone(mobile);
                appUser = appUserService.getByPhone(appUser);
                if (appUser != null) {

                    data.setCode("406");
                    data.setMsg("用户已存在");
                    data.setData("");

                } else {
                    //create app user
                    appUser = new AppUser();
                    appUser.setPhone(mobile);
                    appUser.setPassword(password);
                    appUserService.save(appUser);
                    //generate user token
                    AppToken appToken = new AppToken();
                    appToken.setPhone(appUser.getPhone());
                    appToken.setToken(RandomStrUtil.generateCode(false, 32));
                    appToken.setExprie(caculateExpireTime(2592000));
                    appTokenService.save(appToken);

                    Message message = new Message();
                    message.setContent("欢迎使用唐巢APP");
                    message.setReceiver(mobile);
                    message.setSender("system");
                    message.setStatus("00");//新增
                    message.setTitle("注册成功");
                    message.setType("LOGIN");
                    messageService.save(message);

                    //给用户注册科技侠账号
                    try {
                        String scienerPwd = RandomStrUtil.generateCode(false, 16);
                        String scienerPwdMd5 = md5(scienerPwd);
                        Map res = scienerLockService.regUser(mobile, scienerPwdMd5);
                        if (res.get("errcode") == null && res.get("username") != null) {//保存生成的用户记录
                            appUser.setScienerUserName((String) res.get("username"));
                            appUser.setScienerPassword(scienerPwdMd5);
                            appUserService.save(appUser);
                            log.debug("register sciener account success");
                        }
                    }catch(Exception e){
                        log.error("register sciener account error. " + e.getMessage());
                    }

                    data.setCode("200");
                    data.setMsg("注册成功");
                    Map object = new HashMap();
                    object.put("user_id", appUser.getPhone());
                    object.put("token", appToken.getToken());
                    object.put("expire", appToken.getExprie().getTime());
                    data.setData(object);
                }
            } else {
                data.setCode("402");
                data.setMsg("无效验证码");
                data.setData("");
            }
        }catch(Exception e){
            data.setCode("500");
            log.error("register error:", e);
        }
        return data;
    }



    @RequestMapping(value="check_code")
    @ResponseBody
    public ResponseData check_code(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            log.debug("parasMap:" + request.getParameterMap());
            String mobile = (String) request.getParameter("mobile");
            if(StringUtils.isEmpty(mobile)){
                ResponseData data = new ResponseData();
                data.setCode("101");
                data.setMsg("必填参数不能为空 ");
                return data;
            }

            TAppCheckCode tAppCheckCode = new TAppCheckCode();
            tAppCheckCode.setPhone(mobile);
            tAppCheckCode.setCode(RandomStrUtil.generateCode(true, 6));
            Date expiredate = caculateExpireTime(60);
            tAppCheckCode.setExprie(expiredate);
            tAppCheckCodeService.merge(tAppCheckCode);
            smsService.sendSms(mobile, "验证码"+tAppCheckCode.getCode()+",您正在使用唐巢APP,验证码很重要，请勿谢泄露.");
            ResponseData data = new ResponseData();
            data.setCode("200");
            data.setMsg("成功获取验证码");
            data.setData(tAppCheckCode.getCode());
            return data;
        }catch(Exception e){
            log.error("in check_code:"+ e.getMessage());
            ResponseData data = new ResponseData();
            data.setCode("411");
            data.setMsg("获取验证码异常");
            return data;
        }
    }

    @RequestMapping(value="login/pwd")
    @ResponseBody
    public ResponseData loginWithPwd(HttpServletRequest request, HttpServletResponse response, Model model) {

        ResponseData data = new ResponseData();
        try {
            String phone = (String) request.getParameter("mobile");
            String password = (String) request.getParameter("password");
            AppUser appUser = new AppUser();
            appUser.setPhone(phone);
            appUser = appUserService.getByPhone(appUser);
            if (appUser == null) {
                data.setCode("403");
                data.setMsg("用户不存在");
            } else if (!password.equals(appUser.getPassword())) {
                data.setCode("404");
                data.setMsg("用户名/密码有误");
            } else {
                //generate new user token
                AppToken appToken = new AppToken();
                appToken.setPhone(appUser.getPhone());
                appToken.setToken(RandomStrUtil.generateCode(false, 32));
                appToken.setExprie(caculateExpireTime(2592000));
                appTokenService.merge(appToken);
                Map object = new HashMap();
                object.put("user_id", appUser.getPhone());
                object.put("token", appToken.getToken());
                object.put("expire", appToken.getExprie().getTime());
                data.setData(object);
                data.setCode("200");
                data.setMsg("登陆成功");
            }
        }catch (Exception e){
            data.setCode("500");
            log.error("loginWithPwd:", e);
        }
        return data;
    }

    @RequestMapping(value="login/code")
    @ResponseBody
    public ResponseData loginWithCode(HttpServletRequest request, HttpServletResponse response, Model model) {
        ResponseData data = new ResponseData();
        try{

            String mobile = (String) request.getParameter("mobile");
            String code = (String)request.getParameter("code");;

            TAppCheckCode tAppCheckCode = new TAppCheckCode();
            tAppCheckCode.setPhone(mobile);
            tAppCheckCode.setCode(code);
            tAppCheckCode.setExprie(new Date());
            boolean isValidCheckCode = tAppCheckCodeService.verifyCheckCode(tAppCheckCode);

            if(isValidCheckCode){

                //generate user token
                AppToken appToken = new AppToken();
                appToken.setPhone(mobile);
                appToken.setToken(RandomStrUtil.generateCode(false, 32));
                appToken.setExprie(caculateExpireTime(2592000));
                appTokenService.merge(appToken);

                data.setCode("200");
                data.setMsg("登陆成功");
                Map object = new HashMap();
                object.put("user_id", mobile);
                object.put("token", appToken.getToken());
                object.put("expire", appToken.getExprie().getTime());
                data.setData(object);
            }else{
                data.setCode("402");
                data.setMsg("无效验证码");
                data.setData("");
            }
        }catch (Exception e){
            data.setCode("500");
            log.error("loginWithCode error:", e);
        }
        return data;
    }

    @RequestMapping(value="self/pwd")
    @ResponseBody
    public ResponseData changePwd(HttpServletRequest request, HttpServletResponse response, Model model) {
        ResponseData data = new ResponseData();
        try {
            log.debug(request.getParameterMap().toString());
            String mobile = (String) request.getParameter("mobile");
            String oldPassword = (String) request.getParameter("old_password");
            String newPassword = (String) request.getParameter("new_password");
            if (oldPassword == null || newPassword == null) {
                data.setCode("101");
                data.setMsg("必填参数不能为空 ");
                return data;
            }

            AppUser appUser = new AppUser();
            appUser.setPhone(mobile);
            appUser.setPassword(oldPassword);
            appUser = appUserService.getByPhone(appUser);
            if (!oldPassword.equals(appUser.getPassword())) {
                data.setCode("102");
                data.setMsg("用户原密码有误");
            } else {
                appUser.setPassword(newPassword);
                appUserService.save(appUser);
                data.setCode("100");
                data.setMsg("修改密码成功");
            }
        }catch (Exception e){
            data.setCode("500");
            log.error("changePwd error:", e);
        }
        return data;
    }

    @RequestMapping(value="pwd/reset")
    @ResponseBody
    public ResponseData resetPwd(HttpServletRequest request, HttpServletResponse response, Model model) {
        ResponseData data = new ResponseData();
        try {
            log.debug(request.getParameterMap().toString());
            String mobile = (String) request.getParameter("mobile");
            String code = (String) request.getParameter("code");
            String password = (String) request.getParameter("password");
            if (mobile == null || code == null || password == null) {
                data.setCode("101");
                data.setMsg("必填参数不能为空 ");
                return data;
            }

            TAppCheckCode tAppCheckCode = new TAppCheckCode();
            tAppCheckCode.setPhone(mobile);
            tAppCheckCode.setCode(code);
            tAppCheckCode.setExprie(new Date());
            boolean isValidCheckCode = tAppCheckCodeService.verifyCheckCode(tAppCheckCode);

            if (isValidCheckCode) {
                //cheange pwd
                AppUser appUser = new AppUser();
                appUser.setPhone(mobile);
                appUser = appUserService.getByPhone(appUser);
                appUser.setPassword(password);
                appUser.setUpdateDate(new Date());
                appUserService.save(appUser);
                //generate user token
                AppToken appToken = new AppToken();
                appToken.setPhone(mobile);
                appToken.setToken(RandomStrUtil.generateCode(false, 32));
                appToken.setExprie(caculateExpireTime(2592000));
                appTokenService.merge(appToken);

                data.setCode("200");
                data.setMsg("重置密码成功");
                Map object = new HashMap();
                object.put("user_id", appUser.getPhone());
                object.put("token", appToken.getToken());
                object.put("expire", appToken.getExprie().getTime());
                data.setData(object);
            } else {
                data.setCode("402");
                data.setMsg("无效验证码");
                data.setData("");
            }
        }catch (Exception e){
            data.setCode("500");
            log.error("resetPwd error:", e);
        }
        return data;
    }

    // 常见问题
    @RequestMapping(value = "question")
    @ResponseBody
    public ResponseData question(HttpServletRequest request, HttpServletResponse response, Model model) {
        ResponseData data = new ResponseData();
        log.debug(request.getParameterMap().toString());

        try {


            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            Map<String, Object> mp = new HashMap<String, Object>();
            mp.put("question", "如何注册");
            mp.put("answer", "唐巢账户以手机号作为账号，目前仅支持国内手机号");
            list.add(mp);
            Map<String, Object> mp2 = new HashMap<String, Object>();
            mp2.put("question", "如何使用临时开门");
            mp2.put("answer", "请入住前联系客服，将账号与门锁绑定，并下发蓝牙钥匙；登陆后下载钥匙，即可使用蓝牙开门功能");
            list.add(mp2);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("questions", list);

            data.setData(map);
            data.setCode("200");
        } catch (Exception e) {
            data.setCode("500");
            log.error("get messages error:", e);
        }
        return data;
    }

    @RequestMapping(value="scienerToken")
    @ResponseBody
    public ResponseData scienerToken(HttpServletRequest request, HttpServletResponse response, Model model) {
        ResponseData data = new ResponseData();
        try {
            log.debug(request.getParameterMap().toString());
            String mobile = (String) request.getParameter("mobile");

            AppUser appUser = new AppUser();
            appUser.setPhone(mobile);
            appUser = appUserService.getByPhone(appUser);
            if (appUser.getScienerUserName() != null && appUser.getScienerPassword()!=null) {
                Map scienerRes = scienerLockService.authorize(appUser.getScienerUserName(),appUser.getScienerPassword() );
                data.setCode("200");
                data.setMsg("用户在锁平台授权成功");
                data.setData(scienerRes.get("access_token"));
                return data;
            } else {
                data.setCode("400");
                data.setMsg("缺少锁平台账号，请联系客服");
            }
        }catch (Exception e){
            data.setCode("500");
            log.error("scienerToken error:", e);
        }
        return data;
    }
    /**
     * 计算过期时间，单位秒
     * @param duration
     * @return
     */
    private Date caculateExpireTime(int duration){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(13, duration);
        return cal.getTime();
    }
    private String md5(String password) throws Exception {
        String result = "";
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte bytes[] = md.digest(password.getBytes());
        for (int i = 0; i < bytes.length; i++) {
            String str = Integer.toHexString(bytes[i] & 0xFF);
            if (str.length() == 1) {
                str += "F";
            }
            result += str;
        }
        return result;
    }
}
