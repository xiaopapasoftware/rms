package com.thinkgem.jeesite.modules.app.web;

import com.alibaba.druid.util.StringUtils;
import com.thinkgem.jeesite.common.RespConstants;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ParamsException;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.app.annotation.CurrentUser;
import com.thinkgem.jeesite.modules.app.annotation.CurrentUserPhone;
import com.thinkgem.jeesite.modules.app.entity.AppUser;
import com.thinkgem.jeesite.modules.app.entity.Message;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.app.service.AppUserService;
import com.thinkgem.jeesite.modules.app.service.MessageService;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.enums.FileType;
import com.thinkgem.jeesite.modules.utils.UserUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "${apiPath}/self")
public class AppSelfController extends AppBaseController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AttachmentDao attachmentDao;

    /**
     * 查询个人消息
     *
     * @param telPhone 当前登录人的电话号码
     * @param pageNo   当前页数
     * @param pageSize 每页展示的条数
     * @return
     */
    @RequestMapping(value = "message")
    @ResponseBody
    public ResponseData messageList(@CurrentUserPhone String telPhone, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        Page<Message> page = new Page<Message>();
        page.setPageSize(pageSize);
        page.setPageNo(pageNo);
        Message message = new Message();
        message.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
        message.setReceiver(telPhone);
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
        map.put("list", list);
        map.put("totalCount", page.getCount());

        return ResponseData.success().data(map);
    }

    /**
     * 查询个人信息
     *
     * @return
     */
    @RequestMapping(value = "info/")
    @ResponseBody
    public ResponseData getInfo(@CurrentUser AppUser appUser) {
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
        return ResponseData.success().data(infoMap);
    }

    @RequestMapping(value = "avatar")
    @ResponseBody
    public ResponseData avatar(@CurrentUser AppUser appUser, String attachPath) {
        Attachment attachment = new Attachment();
        attachment.setAttachmentType(FileType.APP_USER_AVATAR.getValue());
        attachment.setBizId(appUser.getId());
        attachment.preUpdate();
        attachmentDao.delete(attachment);
        attachment.setId(IdGen.uuid());
        attachment.setAttachmentType(FileType.APP_USER_AVATAR.getValue());
        attachment.setAttachmentPath(attachPath);
        attachment.setCreateDate(new Date());
        attachment.setCreateBy(UserUtils.getUser());
        attachment.setUpdateDate(new Date());
        attachment.setUpdateBy(UserUtils.getUser());
        attachment.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
        attachment.setBizId(appUser.getId());
        attachmentDao.insert(attachment);
        String img_url = Global.getConfig("img.url");
        return ResponseData.success().data(img_url + attachPath);
    }

    @RequestMapping(value = "ic")
    @ResponseBody
    public ResponseData uploadIc(@CurrentUser AppUser appUser, String frontPath, String backPath) {
        if (frontPath == null || backPath == null) {
            throw new ParamsException(RespConstants.ERROR_CODE_101, "身份证前后照片不能为空");
        }

        Attachment attachment = new Attachment();
        attachment.setAttachmentType(FileType.APP_USER_ID_FRONT.getValue());
        attachment.setBizId(appUser.getId());
        attachment.preUpdate();
        attachmentDao.delete(attachment);
        attachment.setId(IdGen.uuid());
        attachment.setAttachmentType(FileType.APP_USER_ID_FRONT.getValue());
        attachment.setAttachmentPath(frontPath);
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
        attachmentback.setAttachmentPath(backPath);
        attachmentback.setCreateDate(new Date());
        attachmentback.setCreateBy(UserUtils.getUser());
        attachmentback.setUpdateDate(new Date());
        attachmentback.setUpdateBy(UserUtils.getUser());
        attachmentback.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
        attachmentback.setBizId(appUser.getId());
        attachmentDao.insert(attachmentback);
        String img_url = Global.getConfig("img.url");
        Map<String, String> icMap = new HashMap<String, String>();
        icMap.put("front", img_url + frontPath);
        icMap.put("back", img_url + backPath);
        return ResponseData.success().data(icMap);
    }

    @RequestMapping(value = "info/change")
    @ResponseBody
    public ResponseData changeInfo(@CurrentUser AppUser appUser, HttpServletRequest request) {
        appUser.setName(request.getParameter("name"));
        appUser.setIdCardNo(request.getParameter("id"));
        appUser.setSex(request.getParameter("sex"));
        appUser.setBirth(request.getParameter("birth"));
        appUser.setAge(request.getParameter("age"));
        appUser.setProfession(request.getParameter("profession"));
        appUser.setCorp(request.getParameter("corp"));
        appUserService.save(appUser);
        return ResponseData.success();
    }

}
