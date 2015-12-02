package com.thinkgem.jeesite.modules.app.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.app.entity.AppUser;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.app.service.AppUserService;
import com.thinkgem.jeesite.modules.message.entity.Message;
import com.thinkgem.jeesite.modules.message.service.MessageService;



@Controller
@RequestMapping("self")
public class AppSelfController {
	Logger log = LoggerFactory.getLogger(AppSelfController.class);
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private AppUserService appUserService;
	
	//消息查询
	@RequestMapping(value="message")
	@ResponseBody
	public ResponseData messageList(HttpServletRequest request, HttpServletResponse response, Model model) {
		ResponseData data = new ResponseData();
		log.debug(request.getParameterMap().toString());
		String mobile = (String) request.getParameter("mobile");
		if(mobile == null ){
			data.setCode("101");
			data.setMsg("用户获取失败");	
			return data;
		}
		try {
			
			Message message = new Message();
			message.setDelFlag("0");
			message.setReceiver(mobile);
			List<Message> msgs = messageService.findList(message);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(Message msg : msgs) {
				Map<String,Object> mp = new HashMap<String,Object>();
				mp.put("id", msg.getId());
				mp.put("time", DateFormatUtils.format(msg.getCreateDate(), "yyyy-MM-dd HH:mm"));
				mp.put("msg", msg.getContent());
				list.add(mp);
			}
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("msgs", list);
			
			data.setData(map);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("get messages error:",e);
		}
		return data;
	}

	@RequestMapping(value = "info")
    @ResponseBody
    public ResponseData getInfo(HttpServletRequest request, HttpServletResponse response) {
    	ResponseData data = new ResponseData();
    	if(null == request.getParameter("mobile")) {
    		data.setCode("101");
    		return data;
    	}
    	
    	try {
			String mobile = request.getParameter("mobile");
			AppUser appUser = new AppUser();
			appUser.setPhone(mobile);
			appUser = appUserService.getByPhone(appUser);
			
			Map<String, String> infoMap = new HashMap<String, String>();
			infoMap.put("name", appUser.getName());
			infoMap.put("id", appUser.getIdCardNo());
			infoMap.put("sex", appUser.getSex());
			infoMap.put("birth", appUser.getBirth());
			infoMap.put("age", appUser.getAge());
			infoMap.put("profession", appUser.getProfession());
			infoMap.put("corp", appUser.getCorp());
	
			
			PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
			
//			String avatar = "";
//			if(!StringUtils.isEmpty(house.getAvatarPath())) {
//				
//			    String img_url = appUser.getProperty("img.url");
//				avatar = img_url+appUser.getAvatarPath;
//			}
//			map.put("avatar", cover);
//			String id_photo_front = "";
//			if(!StringUtils.isEmpty(house.getAvatarPath())) {
//				
//			    String img_url = appUser.getProperty("img.url");
//				avatar = img_url+appUser.getAvatarPath;
//			}
//			map.put("id_photo_front", id_photo_front);
//			String id_photo_back = "";
//			if(!StringUtils.isEmpty(house.getAvatarPath())) {
//				
//			    String img_url = appUser.getProperty("img.url");
//				avatar = img_url+appUser.getAvatarPath;
//			}
//			map.put("id_photo_back", id_photo_back);
			
			
			data.setData(infoMap);
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("getinfo error:",e);
		}
    	return data;
	}
	
	
	@RequestMapping(value = "info/change")
    @ResponseBody
    public ResponseData changeInfo(HttpServletRequest request, HttpServletResponse response) {
    	ResponseData data = new ResponseData();
    	if(null == request.getParameter("mobile")) {
    		data.setCode("101");
    		return data;
    	}
    	
    	try {
			String mobile = request.getParameter("mobile");
			AppUser appUser = new AppUser();
			appUser.setPhone(mobile);
			appUser = appUserService.getByPhone(appUser);
			
			appUser.setName(request.getParameter("name"));
			appUser.setIdCardNo(request.getParameter("id"));
			appUser.setSex(request.getParameter("sex"));
			appUser.setBirth(request.getParameter("birth"));
			appUser.setAge(request.getParameter("age"));
			appUser.setProfession(request.getParameter("profession"));
			appUser.setCorp(request.getParameter("corp"));
	
			appUserService.save(appUser);
			
			data.setData("");
			data.setCode("200");
		} catch (Exception e) {
			data.setCode("500");
			log.error("change info error:",e);
		}
    	return data;
	}
	
}