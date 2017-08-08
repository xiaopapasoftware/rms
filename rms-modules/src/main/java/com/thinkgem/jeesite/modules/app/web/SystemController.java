package com.thinkgem.jeesite.modules.app.web;

import com.thinkgem.jeesite.common.RespConstants;
import com.thinkgem.jeesite.common.exception.AuthcException;
import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.PasswordHelper;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.annotation.AuthIgnore;
import com.thinkgem.jeesite.modules.app.annotation.CurrentUser;
import com.thinkgem.jeesite.modules.app.entity.*;
import com.thinkgem.jeesite.modules.app.service.*;
import com.thinkgem.jeesite.modules.app.util.TokenGenerator;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import com.thinkgem.jeesite.modules.lock.service.ScienerLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping(value = "${apiPath}/system")
public class SystemController extends AppBaseController {


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
            return ResponseData.failure(RespConstants.ERROR_CODE_405).message(RespConstants.ERROR_MSG_405);
        } else {
            //create app user
            appUser = new AppUser();
            appUser.setPhone(telPhone);
            appUser.setPassword(PasswordHelper.encryptPassword(password));
            appUserService.save(appUser);

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
            return appTokenService.tokenMerge(telPhone);
        }
    }

    @AuthIgnore
    @RequestMapping(value = "send_code")
    @ResponseBody
    public ResponseData check_code(String telPhone) {
        if (StringUtils.isBlank(telPhone)) {
            new ResponseData(RespConstants.ERROR_CODE_103, RespConstants.ERROR_MSG_103);
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
            throw new AuthcException(RespConstants.ERROR_CODE_403, RespConstants.ERROR_MSG_403);
        } else if (PasswordHelper.checkPassword(appUser.getPassword(), password)) {
            return new ResponseData(RespConstants.ERROR_CODE_406, RespConstants.ERROR_MSG_406);
        }
        return appTokenService.tokenMerge(telPhone);
    }

    @AuthIgnore
    @RequestMapping(value = "login/code")
    @ResponseBody
    public ResponseData loginWithCode(String telPhone, String code) {
        AppUser appUser = new AppUser();
        appUser.setPhone(telPhone);
        appUser = appUserService.getByPhone(appUser);
        if (appUser == null) {
            throw new AuthcException(RespConstants.ERROR_CODE_403, RespConstants.ERROR_MSG_403);
        }
        appSmsMessageService.verifyCode(telPhone, code);
        //generate user token
        return appTokenService.tokenMerge(telPhone);
    }


    @RequestMapping(value = "self/pwd")
    @ResponseBody
    public ResponseData changePwd(@CurrentUser AppUser appUser, String telPhone, String newPassword, String oldPassword) {
        if (telPhone == null || newPassword == null || oldPassword == null) {
            return ResponseData.failure(RespConstants.ERROR_CODE_101).message("必填参数不能为空");
        }

        if (PasswordHelper.checkPassword(appUser.getPassword(), oldPassword)) {
            appUser.setPassword(PasswordHelper.encryptPassword(newPassword));
            appUserService.save(appUser);
            return appTokenService.tokenMerge(telPhone);
        } else {
            return ResponseData.failure(RespConstants.ERROR_CODE_406).message(RespConstants.ERROR_MSG_406);
        }
    }

    @RequestMapping(value = "pwd/reset")
    @ResponseBody
    public ResponseData resetPwd(@CurrentUser AppUser appUser, String telPhone, String code, String password) {
        if (telPhone == null || code == null || password == null) {
            return ResponseData.failure(RespConstants.ERROR_CODE_101).message("必填参数不能为空 ");
        }

        appSmsMessageService.verifyCode(telPhone, code);
        appUser.setPhone(telPhone);
        appUser.setPassword(PasswordHelper.encryptPassword(password));
        appUserService.save(appUser);
        return appTokenService.tokenMerge(telPhone);
    }

    // 常见问题
    @AuthIgnore
    @RequestMapping(value = "question")
    @ResponseBody
    public ResponseData question() {
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

        return ResponseData.success().data(map);
    }

    @RequestMapping(value = "scienerToken")
    @ResponseBody
    public ResponseData scienerToken(@CurrentUser AppUser appUser) {
        if (appUser.getScienerUserName() != null && appUser.getScienerPassword() != null) {
            Map scienerRes = scienerLockService.authorize(appUser.getScienerUserName(), appUser.getScienerPassword());
            return ResponseData.success().message("用户在锁平台授权成功").data(scienerRes.get("access_token"));
        } else {
            return ResponseData.failure(RespConstants.ERROR_CODE_104).message(RespConstants.ERROR_MSG_104);
        }
    }

}
