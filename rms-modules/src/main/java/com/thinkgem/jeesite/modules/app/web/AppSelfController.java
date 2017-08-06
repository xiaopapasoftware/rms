package com.thinkgem.jeesite.modules.app.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.modules.utils.UserUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.app.entity.AppUser;
import com.thinkgem.jeesite.modules.app.entity.Message;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.app.service.AppUserService;
import com.thinkgem.jeesite.modules.app.service.MessageService;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.enums.FileType;

@Controller
@RequestMapping(value = "${apiPath}/self")
public class AppSelfController extends AppBaseController{
  Logger log = LoggerFactory.getLogger(AppSelfController.class);

  @Autowired
  private MessageService messageService;

  @Autowired
  private AppUserService appUserService;

  @Autowired
  private AttachmentDao attachmentDao;

  // 消息查询
  @RequestMapping(value = "message")
  @ResponseBody
  public ResponseData messageList(HttpServletRequest request, HttpServletResponse response, Model model) {
    ResponseData data = new ResponseData();
    log.debug(request.getParameterMap().toString());
    String mobile = (String) request.getParameter("mobile");
    if (mobile == null) {
      data.setCode("101");
      data.setMsg("用户获取失败");
      return data;
    }
    if (null == request.getParameter("p_n") || null == request.getParameter("p_s")) {
      data.setCode("101");
      return data;
    }
    try {
      Integer p_n = Integer.valueOf(request.getParameter("p_n"));
      Integer p_s = Integer.valueOf(request.getParameter("p_s"));
      Page<Message> page = new Page<Message>();
      page.setPageSize(p_s);
      page.setPageNo(p_n);
      Message message = new Message();
      message.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
      message.setReceiver(mobile);
      page = messageService.findPage(page, message);
      List<Message> msgs = page.getList();
      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
      for (Message msg : msgs) {
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("id", msg.getId());
        mp.put("time", DateFormatUtils.format(msg.getCreateDate(), "yyyy-MM-dd HH:mm"));
        mp.put("msg", msg.getContent());
        list.add(mp);
      }

      Map<String, Object> map = new HashMap<String, Object>();
      map.put("msgs", list);
      map.put("p_t", page.getCount());

      data.setData(map);
      data.setCode("200");
    } catch (Exception e) {
      data.setCode("500");
      log.error("get messages error:", e);
    }
    return data;
  }

  @RequestMapping(value = "info/{telPhone}")
  @ResponseBody
  public ResponseData getInfo(@PathVariable String telPhone) {
    ResponseData data = new ResponseData();
    try {
      AppUser appUser = new AppUser();
      appUser.setPhone(telPhone);
      appUser = appUserService.getByPhone(appUser);
      Map<String, String> infoMap = new HashMap<String, String>();
      infoMap.put("name", appUser.getName());
      infoMap.put("id", appUser.getIdCardNo());
      infoMap.put("sex", appUser.getSex());
      infoMap.put("birth", appUser.getBirth());
      infoMap.put("age", appUser.getAge());
      infoMap.put("profession", appUser.getProfession());
      infoMap.put("corp", appUser.getCorp());
      String img_url = Global.getConfig("img.url");
      String avatar = "";
      if (!StringUtils.isEmpty(appUser.getAvatar())) {
        avatar = img_url + appUser.getAvatar();
      } else {// default avatar
        avatar = img_url + "/rms-api/pic/avatar.jpg";
      }
      infoMap.put("avatar", avatar);
      String idCardPhotoFront = "";
      if (!StringUtils.isEmpty(appUser.getIdCardPhoto＿front())) {
        idCardPhotoFront = img_url + appUser.getIdCardPhoto＿front();
      }
      infoMap.put("id_photo_front", idCardPhotoFront);
      String idCardPhotoBack = "";
      if (!StringUtils.isEmpty(appUser.getIdCardPhoto＿back())) {
        idCardPhotoBack = img_url + appUser.getIdCardPhoto＿back();
      }
      infoMap.put("id_photo_back", idCardPhotoBack);
      data.setData(infoMap);
      data.setCode("200");
    } catch (Exception e) {
      data.setCode("500");
      log.error("getinfo error:", e);
    }
    return data;
  }

  @RequestMapping(value = "avatar")
  @ResponseBody
  public ResponseData avatar(HttpServletRequest request, HttpServletResponse response) {
    ResponseData data = new ResponseData();
    if (null == request.getParameter("mobile")) {
      data.setCode("101");
      return data;
    }
    try {
      String mobile = request.getParameter("mobile");
      AppUser appUser = new AppUser();
      appUser.setPhone(mobile);
      appUser = appUserService.getByPhone(appUser);
      if (appUser == null) {
        data.setCode("200");
        data.setMsg("用户不存在");
        return data;
      }
      String attach_path = request.getParameter("attach_path");
      if (attach_path == null) {
        data.setCode("500");
        data.setMsg("上传有误");
        return data;
      }
      Attachment attachment = new Attachment();
      attachment.setAttachmentType(FileType.APP_USER_AVATAR.getValue());
      attachment.setBizId(appUser.getId());
      attachment.preUpdate();
      attachmentDao.delete(attachment);
      attachment.setId(IdGen.uuid());
      attachment.setAttachmentType(FileType.APP_USER_AVATAR.getValue());
      attachment.setAttachmentPath(attach_path);
      attachment.setCreateDate(new Date());
      attachment.setCreateBy(UserUtils.getUser());
      attachment.setUpdateDate(new Date());
      attachment.setUpdateBy(UserUtils.getUser());
      attachment.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
      attachment.setBizId(appUser.getId());
      attachmentDao.insert(attachment);
      PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
      String img_url = proper.getProperty("img.url");
      data.setData(img_url + attach_path);
      data.setCode("200");
    } catch (Exception e) {
      data.setCode("500");
      log.error("getinfo error:", e);
    }
    return data;
  }

  @RequestMapping(value = "ic")
  @ResponseBody
  public ResponseData uploadIc(HttpServletRequest request, HttpServletResponse response) {
    ResponseData data = new ResponseData();
    if (null == request.getParameter("mobile")) {
      data.setCode("101");
      return data;
    }
    try {
      String mobile = request.getParameter("mobile");
      AppUser appUser = new AppUser();
      appUser.setPhone(mobile);
      appUser = appUserService.getByPhone(appUser);
      if (appUser == null) {
        data.setCode("200");
        data.setMsg("用户不存在");
        return data;
      }
      String front = request.getParameter("front");
      String back = request.getParameter("back");
      if (front == null || back == null) {
        data.setCode("500");
        data.setMsg("上传有误");
        return data;
      }
      Attachment attachment = new Attachment();
      attachment.setAttachmentType(FileType.APP_USER_ID_FRONT.getValue());
      attachment.setBizId(appUser.getId());
      attachment.preUpdate();
      attachmentDao.delete(attachment);
      attachment.setId(IdGen.uuid());
      attachment.setAttachmentType(FileType.APP_USER_ID_FRONT.getValue());
      attachment.setAttachmentPath(front);
      attachment.setCreateDate(new Date());
      attachment.setCreateBy(UserUtils.getUser());
      attachment.setUpdateDate(new Date());
      attachment.setUpdateBy(UserUtils.getUser());
      attachment.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
      attachment.setBizId(appUser.getId());
      attachmentDao.insert(attachment);
      Attachment attachmentback = new Attachment();
      attachmentback.setAttachmentType(FileType.APP_USER_ID_BACK.getValue());
      attachmentback.setBizId(appUser.getId());
      attachmentback.preUpdate();
      attachmentDao.delete(attachmentback);
      attachmentback.setId(IdGen.uuid());
      attachmentback.setAttachmentType(FileType.APP_USER_ID_BACK.getValue());
      attachmentback.setAttachmentPath(back);
      attachmentback.setCreateDate(new Date());
      attachmentback.setCreateBy(UserUtils.getUser());
      attachmentback.setUpdateDate(new Date());
      attachmentback.setUpdateBy(UserUtils.getUser());
      attachmentback.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
      attachmentback.setBizId(appUser.getId());
      attachmentDao.insert(attachmentback);
      PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
      String img_url = proper.getProperty("img.url");
      Map<String, String> icMap = new HashMap<String, String>();
      icMap.put("front", img_url + front);
      icMap.put("back", img_url + back);
      data.setData(icMap);
      data.setCode("200");
    } catch (Exception e) {
      data.setCode("500");
      log.error("getinfo error:", e);
    }
    return data;
  }

  @RequestMapping(value = "info/change")
  @ResponseBody
  public ResponseData changeInfo(HttpServletRequest request, HttpServletResponse response) {
    ResponseData data = new ResponseData();
    if (null == request.getParameter("mobile")) {
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
      log.error("change info error:", e);
    }
    return data;
  }

}
