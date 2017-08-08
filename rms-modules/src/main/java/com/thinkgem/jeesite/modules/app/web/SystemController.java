package com.thinkgem.jeesite.modules.app.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.GenerateCode;
import com.thinkgem.jeesite.common.utils.PasswordHelper;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.annotation.AuthIgnore;
import com.thinkgem.jeesite.modules.app.entity.*;
import com.thinkgem.jeesite.modules.app.service.*;
import com.thinkgem.jeesite.modules.app.util.TokenGenerator;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import com.thinkgem.jeesite.modules.lock.service.ScienerLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping(value = "${apiPath}/system")
public class SystemController extends AppBaseController {

    private static final int TOKEN_EXPIRE_DAY = 7;

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

    @Autowired
    private QuestionsService questionsService;

    @Autowired
    private AppSmsMessageService appSmsMessageService;

    @RequestMapping(value = "checkToken")
    @ResponseBody
    public String checkToken(HttpServletRequest request, HttpServletResponse response, Model model) {
        String res;
        try {
            String token = (String) request.getHeader("token");
            logger.debug("in checkToken. token:" + token);
            //验证token是否存在
            AppToken appToken = new AppToken();
            appToken.setToken(token);
            appToken.setExprie(new Date());
            appToken = appTokenService.findByTokenAndExpire(appToken);
            if (appToken != null)
                res = appToken.getPhone();
            else
                res = null;
            logger.debug("in chekcToken. return phone:" + res);
        } catch (Exception e) {
            res = null;
            logger.error("chekcToken error:", e);
        }
        return res;
    }

    @AuthIgnore
    @RequestMapping(value = "register")
    @ResponseBody
    public ResponseData register(String telPhone, String code, String password) {
        appSmsMessageService.verifyCode(telPhone, code);

        //exist?
        AppUser appUser = new AppUser();
        appUser.setPhone(telPhone);
        appUser = appUserService.getByPhone(appUser);
        if (appUser != null) {
            return ResponseData.failure(200).message("用户已存在");
        } else {
            //create app user
            appUser = new AppUser();
            appUser.setPhone(telPhone);
            appUser.setPassword(PasswordHelper.encryptPassword(password));
            appUserService.save(appUser);
            //generate user token
            AppToken appToken = new AppToken();
            appToken.setPhone(appUser.getPhone());
            appToken.setToken(GenerateCode.generateCode());
                /*token 有效期7天*/
            LocalDate localDate = LocalDate.now().plusDays(TOKEN_EXPIRE_DAY);
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.of("GMT")));
            appToken.setExprie(Date.from(instant));
            appTokenService.save(appToken);

            Message message = new Message();
            message.setContent("欢迎使用唐巢APP");
            message.setReceiver(telPhone);
            message.setSender("system");
            message.setStatus("00");//新增
            message.setTitle("注册成功");
            message.setType("LOGIN");
            messageService.save(message);
            //给用户注册科技侠账号
                /*try {
                    String scienerPwd = RandomStrUtil.generateCode(false, 16);
                    String scienerPwdMd5 = md5(scienerPwd);
                    Map res = scienerLockService.regUser(telPhone, scienerPwdMd5);
                    if (res.get("errcode") == null && res.get("username") != null) {//保存生成的用户记录
                        appUser.setScienerUserName((String) res.get("username"));
                        appUser.setScienerPassword(scienerPwdMd5);
                        appUserService.save(appUser);
                        log.debug("register sciener account success");
                    }
                } catch (Exception e) {
                    log.error("register sciener account error. " + e.getMessage());
                }*/
            return ResponseData.success().message("注册成功").data(appToken.getToken());
        }
    }

    @AuthIgnore
    @RequestMapping(value = "send_code")
    @ResponseBody
    public ResponseData check_code(String telPhone) {
        if (StringUtils.isBlank(telPhone)) {
            new ResponseData(101, "手机号码不能为空");
        }
        appSmsMessageService.sendValidCode(telPhone);
        return ResponseData.success().message("验证码发送成功，请注意查收");
    }

    @AuthIgnore
    @RequestMapping(value = "login/pwd")
    @ResponseBody
    public ResponseData loginWithPwd(String telPhone, String password) {
        AppUser appUser = new AppUser();
        appUser.setPhone(telPhone);
        appUser = appUserService.getByPhone(appUser);
        if (appUser == null) {
            return new ResponseData(403, "当前用户不存在");
        } else if (PasswordHelper.checkPassword(appUser.getPassword(), password)) {
            return new ResponseData(404, "用户名/密码有误");
        } else {
            //generate new user token
            AppToken appToken = new AppToken();
            appToken.setPhone(appUser.getPhone());
            appToken.setToken(GenerateCode.generateCode());
            /*token 有效期7天*/
            LocalDate localDate = LocalDate.now().plusDays(TOKEN_EXPIRE_DAY);
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.of("GMT")));
            appToken.setExprie(Date.from(instant));
            appTokenService.merge(appToken);
            return ResponseData.success().message("登陆成功").data(appToken.getToken());
        }
    }

    @AuthIgnore
    @RequestMapping(value = "login/code")
    @ResponseBody
    public ResponseData loginWithCode(String telPhone, String code) {

        appSmsMessageService.verifyCode(telPhone, code);
        //generate user token
        AppToken appToken = new AppToken();
        appToken.setPhone(telPhone);
        appToken.setToken(TokenGenerator.generateValue());
        /*token 有效期7天*/
        LocalDate localDate = LocalDate.now().plusDays(TOKEN_EXPIRE_DAY);
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.of("GMT")));
        appToken.setExprie(Date.from(instant));
        appTokenService.merge(appToken);
        return ResponseData.success().message("登陆成功").data(appToken.getToken());
    }

    @AuthIgnore
    @RequestMapping(value = "self/pwd")
    @ResponseBody
    public ResponseData changePwd(String telPhone, String newPassword, String oldPassword) {
        if (telPhone == null || newPassword == null || oldPassword == null) {
            return ResponseData.failure(101).message("必填参数不能为空 ");
        }

        AppUser appUser = new AppUser();
        appUser.setPhone(telPhone);
        appUser = appUserService.getByPhone(appUser);
        if (appUser == null) {
            return new ResponseData(403, "当前用户不存在");
        }

        if (PasswordHelper.checkPassword(appUser.getPassword(), oldPassword)) {
            appUser.setPassword(PasswordHelper.encryptPassword(newPassword));
            appUserService.save(appUser);
            return new ResponseData(200, "修改密码成功");
        } else {
            return new ResponseData(404, "用户名/密码有误");
        }
    }

    @AuthIgnore
    @RequestMapping(value = "pwd/reset")
    @ResponseBody
    public ResponseData resetPwd(String telPhone, String code, String password) {
        if (telPhone == null || code == null || password == null) {
            return ResponseData.failure(101).message("必填参数不能为空 ");
        }

        appSmsMessageService.verifyCode(telPhone, code);
        AppToken appToken = new AppToken();
        appToken.setPhone(telPhone);
        appToken.setToken(GenerateCode.generateCode());
        /*token 有效期7天*/
        LocalDate localDate = LocalDate.now().plusDays(TOKEN_EXPIRE_DAY);
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.of("GMT")));
        appToken.setExprie(Date.from(instant));
        appTokenService.merge(appToken);
        return ResponseData.success().message("登陆成功").data(appToken.getToken());
    }

    // 常见问题
    @AuthIgnore
    @RequestMapping(value = "question")
    @ResponseBody
    public ResponseData question() {
        ResponseData data = new ResponseData();
        try {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Questions que = new Questions();
            Page p = new Page<Questions>();
            p.setOrderBy("sort");
            que.setPage(p);
            List<Questions> qeustions = questionsService.findList(que);
            for (Questions q : qeustions) {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("question", q.getQuestion());
                mp.put("answer", q.getAnswer());
                list.add(mp);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", list);

            data.setData(map);
            data.setCode(200);
        } catch (Exception e) {
            data.setCode(500);
            logger.error("get messages error:", e);
        }
        return data;
    }

    @RequestMapping(value = "scienerToken")
    @ResponseBody
    public ResponseData scienerToken(HttpServletRequest request, HttpServletResponse response, Model model) {
        ResponseData data = new ResponseData();
        try {
            logger.debug(request.getParameterMap().toString());
            String mobile = (String) request.getParameter("mobile");

            AppUser appUser = new AppUser();
            appUser.setPhone(mobile);
            appUser = appUserService.getByPhone(appUser);
            if (appUser.getScienerUserName() != null && appUser.getScienerPassword() != null) {
                Map scienerRes = scienerLockService.authorize(appUser.getScienerUserName(), appUser.getScienerPassword());
                data.setCode(200);
                data.setMsg("用户在锁平台授权成功");
                data.setData(scienerRes.get("access_token"));
                return data;
            } else {
                data.setCode(400);
                data.setMsg("缺少锁平台账号，请联系客服");
            }
        } catch (Exception e) {
            data.setCode(500);
            logger.error("scienerToken error:", e);
        }
        return data;
    }

    /**
     * 计算过期时间，单位秒
     *
     * @param duration
     * @return
     */
    private Date caculateExpireTime(int duration) {
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
