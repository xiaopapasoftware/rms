package com.thinkgem.jeesite.modules.app.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.activiti.engine.impl.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.modules.app.entity.AppToken;
import com.thinkgem.jeesite.modules.app.entity.AppUser;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.app.entity.TAppCheckCode;
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
	
	@RequestMapping(value="register")
	@ResponseBody
	public String register(@RequestBody HashMap paramsMap) {
		
		ResponseData data = new ResponseData(); 
		String mobile = (String) paramsMap.get("mobile");
		String code = (String) paramsMap.get("code");
		String password =  (String) paramsMap.get("password");
		
		TAppCheckCode tAppCheckCode = new TAppCheckCode();
		tAppCheckCode.setPhone(mobile);
		tAppCheckCode.setCode(code);
		tAppCheckCode.setExprie(new Date());
		boolean isValidCheckCode = tAppCheckCodeService.verifyCheckCode(tAppCheckCode);
		if(isValidCheckCode){
			//exist?
			AppUser appUser = new AppUser();
			appUser.setPhone(mobile);
			appUser = appUserService.getByPhone(appUser);
			if(appUser!=null){
				
				data.setCode("406");
				data.setMsg("user exists!");
				data.setData("");
				
			}else{			
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
				
				
				data.setCode("200");
				data.setMsg("register success");
				JSONObject object = new JSONObject();
				object.put("user_id", appUser.getPhone());
				object.put("token", appToken.getToken());
				object.put("expire", formatDate(appToken.getExprie()));
				data.setData(object.toString());
			}
		}else{
			data.setCode("402");
			data.setMsg("invalid check code");
			data.setData("");
		}
		return JsonUtil.object2Json(data);
	}
	
	@RequestMapping(value="check_code")
	@ResponseBody
	public String check_code(@RequestBody HashMap paramsMap) {
		try{
			String mobile = (String) paramsMap.get("mobile");
		
			TAppCheckCode tAppCheckCode = new TAppCheckCode();
			tAppCheckCode.setPhone(mobile);
			tAppCheckCode.setCode(RandomStrUtil.generateCode(true, 6));
			Date expiredate = caculateExpireTime(60);
			tAppCheckCode.setExprie(expiredate);
			tAppCheckCodeService.merge(tAppCheckCode);
			smsService.sendSms(mobile, "校验码"+mobile+",您正在使用唐巢APP,校验码很重要，请勿谢告诉任何人.");
			ResponseData data = new ResponseData(); 
			data.setCode("200");
			data.setMsg("fetch check code success");
			data.setData(tAppCheckCode.getCode());
			return JsonUtil.object2Json(data);
		}catch(Exception e){
			ResponseData data = new ResponseData(); 
			data.setCode("411");
			data.setMsg("fetch check code error");
			return JsonUtil.object2Json(data);
		}
	}
	
	@RequestMapping(value="login/pwd")
	@ResponseBody
	public String loginWithPwd(@RequestBody HashMap paramsMap) {
		
		ResponseData data = new ResponseData(); 
		String phone = (String) paramsMap.get("mobile");
		String password = (String) paramsMap.get("password");
		AppUser appUser = new AppUser();
		appUser.setPhone(phone);
		appUser = appUserService.getByPhone(appUser);
		if(appUser==null){
			data.setCode("403");
			data.setMsg("user doesn't exist");
		}else if(!password.equals(appUser.getPassword())){
			data.setCode("404");
			data.setMsg("wrong user/password");			
		}else{
			//generate new user token
			AppToken appToken = new AppToken();
			appToken.setPhone(appUser.getPhone());
			appToken.setToken(RandomStrUtil.generateCode(false, 32));
			appToken.setExprie(caculateExpireTime(2592000));
			appTokenService.merge(appToken);
			JSONObject object = new JSONObject();
			object.put("user_id", appUser.getPhone());
			object.put("token", appToken.getToken());
			object.put("expire", formatDate(appToken.getExprie()));
			data.setData(object.toString());
			data.setCode("200");
			data.setMsg("login success");
		}
		return JsonUtil.object2Json(data);
	}
	
	@RequestMapping(value="login/code")
	@ResponseBody
	public String loginWithCode(@RequestBody HashMap paramsMap) {
		
		String res = "";
		String mobile = (String) paramsMap.get("mobile");
		String code = (String) paramsMap.get("code");;
		
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
			
			ResponseData data = new ResponseData(); 
			data.setCode("200");
			data.setMsg("login success");
			JSONObject object = new JSONObject();
			object.put("user_id", mobile);
			object.put("token", appToken.getToken());
			object.put("expire", formatDate(appToken.getExprie()));
			data.setData(object.toString());
			res = JsonUtil.object2Json(data);
		}else{
			ResponseData data = new ResponseData(); 
			data.setCode("402");
			data.setMsg("invalid check code");
			data.setData("");
			res = JsonUtil.object2Json(data);
		}
		return res;
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
	
	private String formatDate(Date date){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    return sdf.format(date); 
	}
	

}
