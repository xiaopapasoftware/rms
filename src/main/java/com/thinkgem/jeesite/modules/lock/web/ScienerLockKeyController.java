/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.lock.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.AppUser;
import com.thinkgem.jeesite.modules.app.service.AppUserService;
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.inventory.entity.Neighborhood;
import com.thinkgem.jeesite.modules.inventory.service.NeighborhoodService;
import com.thinkgem.jeesite.modules.lock.entity.ScienerKey;
import com.thinkgem.jeesite.modules.lock.service.ScienerLockKeyService;
import com.thinkgem.jeesite.modules.lock.service.ScienerLockService;
import com.thinkgem.jeesite.modules.person.entity.NeighborhoodContact;
import com.thinkgem.jeesite.modules.person.service.NeighborhoodContactService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 钥匙管理Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/lock/scienerLockKey")
public class ScienerLockKeyController extends BaseController {

    @Autowired
    private ScienerLockService scienerLockService;

    @Autowired
    private AppUserService appUserService;


    @ModelAttribute
    public ScienerKey get(@RequestParam(required = false) String openid,
                          @RequestParam(required = false) String lockId,
                          @RequestParam(required = false) String keyId) {
        logger.debug("*********************: " + openid + " " + lockId + " " + keyId);
        ScienerKey entity = new ScienerKey();
	    entity.setLockId(lockId);
        entity.setKeyId(keyId);
        entity.setOpenid(openid);
	    return entity;
    }

    // @RequiresPermissions("person:neighborhoodContact:view")
    @RequestMapping(value = { "list", "" })
    public String list(ScienerKey keyparam, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("lockList", scienerLockService.getLockList());
        AppUser appUser = new AppUser();
        appUser.setDelFlag("0");
        List<AppUser> users = appUserService.findList(appUser);
        model.addAttribute("users", users);
        List<Map> keys = scienerLockService.getAllKeys();
        List<Map> resKeys = new ArrayList<Map>();
        //用户科技侠账号与用户名的关系map
        Map scienerAccountMap = new HashMap();
        for(AppUser user: users){
            scienerAccountMap.put(user.getScienerUserName(), user.getName());
        }

        //查询条件
        String lockId = null;
        String username = null;
        String keyStatus = null;
        logger.debug("++++++++++++++keyparam:" + keyparam);
        if(keyparam != null ){
            lockId = keyparam.getLockId();
            username = keyparam.getUsername();
            keyStatus = keyparam.getKeyStatus();
        }
        if(keys!= null){
            for(Map key: keys){
                if(StringUtils.isNotBlank(lockId)){
                    if(!lockId.equals(""+key.get("lockId"))) continue;
                }
                if(StringUtils.isNotBlank(username)){
                    if(!username.equals(""+key.get("username"))) continue;
                }
                if(StringUtils.isNotBlank(keyStatus)){
                    if(!keyStatus.equals(""+key.get("keyStatus"))) continue;
                }

                if(key.get("date")!= null){
                    key.put("date", new Date((Long)key.get("date")));
                }
                if(key.get("startDate")!= null && !"0".equals(""+key.get("startDate"))){
                    key.put("startDate", new Date((Long)key.get("startDate")));
                    key.put("endDate", new Date((Long)key.get("endDate")));
                    key.put("keyType", "1");//限时
                }else{
                    key.put("keyType", "0");//永久
                }

                key.put("appUserName", scienerAccountMap.get(key.get("username")));
                resKeys.add(key);
            }
        }

        Collections.sort(resKeys, new Comparator<Map>() {
            public int compare(Map arg0, Map arg1) {
                return ((Date)arg0.get("date")).compareTo((Date)arg1.get("date"));
            }
        });

        model.addAttribute("keys",resKeys);
        model.addAttribute("key",keyparam);
        return"modules/lock/scienerKeyList";
    }

                // @RequiresPermissions("person:neighborhoodContact:view")
        @RequestMapping(value = "form")
    public String form(ScienerKey key, Model model) {
        model.addAttribute("key", key);
        model.addAttribute("lockList", scienerLockService.getLockList());
        AppUser appUser = new AppUser();
        appUser.setDelFlag("0");
        List<AppUser> users = appUserService.findList(appUser);
        model.addAttribute("users", users);
        return "modules/lock/scienerKeyForm";
    }

    // @RequiresPermissions("person:neighborhoodContact:edit")
    @RequestMapping(value = "save")
    public String save(ScienerKey key, Model model, RedirectAttributes redirectAttributes) {
        logger.debug(key.toString());
        if (!beanValidator(model, key)) {
            return form(key, model);
        }
        //调用科技侠分配钥匙接口
        scienerLockService.sendKey(Integer.parseInt(key.getLockId()), key.getUsername(), key.getStartDate().getTime(), key.getEndDate().getTime(), key.getRemark());
        addMessage(redirectAttributes, "分配钥匙成功");
        return "redirect:" + Global.getAdminPath() + "/lock/scienerLockKey/?repage";
    }

    // @RequiresPermissions("person:neighborhoodContact:edit")
    @RequestMapping(value = "delete")
    public String delete(ScienerKey key, RedirectAttributes redirectAttributes) {
        logger.debug("))))))))))))))))))))))" + key);
	scienerLockService.deleteKey(Integer.parseInt(key.getLockId()), Integer.parseInt(key.getOpenid()), Integer.parseInt(key.getKeyId()));
	addMessage(redirectAttributes, "删除钥匙成功");
	return "redirect:" + Global.getAdminPath() + "/lock/scienerLockKey/?repage";
    }

}