package com.thinkgem.jeesite.modules.person.web;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import com.thinkgem.jeesite.modules.person.service.OwnerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 业主信息Controller
 *
 * @author huangsc
 * @version 2015-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/person/owner")
public class OwnerController extends BaseController {

    @Autowired
    private OwnerService ownerService;

    @ModelAttribute
    public Owner get(@RequestParam(required = false) String id) {
        Owner entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = ownerService.get(id);
        }
        if (entity == null) {
            entity = new Owner();
        }
        return entity;
    }

    @RequiresPermissions("person:owner:view")
    @RequestMapping(value = {"list"})
    public String listQuery(Owner owner, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Owner> page = ownerService.findPage(new Page<Owner>(request, response), owner);
        model.addAttribute("page", page);
        return "modules/person/ownerList";
    }

    @RequiresPermissions("person:owner:view")
    @RequestMapping(value = {""})
    public String listNoQuery(Owner owner, HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/person/ownerList";
    }

    @RequiresPermissions("person:owner:view")
    @RequestMapping(value = "form")
    public String form(Owner owner, Model model) {
        model.addAttribute("owner", owner);
        return "modules/person/ownerForm";
    }

    @RequiresPermissions("person:owner:edit")
    @RequestMapping(value = "add")
    public String add(Owner owner, Model model) {
        model.addAttribute("owner", owner);
        return "modules/person/ownerAdd";
    }

    @RequiresPermissions("person:owner:edit")
    @RequestMapping(value = "save")
    public String save(Owner owner, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, owner)) {
            return form(owner, model);
        }
        List<Owner> owners = ownerService.findOwnersByCerNoOrMobNoOrTelNo(owner);
        if (owner.getIsNewRecord()) {// 新增
            if (CollectionUtils.isNotEmpty(owners)) {// 已有重复的身份证号或者手机号或者电话号
                addMessage(model, ViewMessageTypeEnum.WARNING, calculateTipMsg(owner, owners));
                return "modules/person/ownerForm";
            } else {// 无重复业主
                ownerService.save(owner);
                addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存业主信息成功");
                return "redirect:" + Global.getAdminPath() + "/person/owner/?repage";
            }
        } else {// 修改
            if (CollectionUtils.isNotEmpty(owners)) {// 已有重复的身份证号或者手机号或者电话号,更新其余字段
                owner.setId(owners.get(0).getId());
            }
            ownerService.save(owner);
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "修改业主信息成功");
            return "redirect:" + Global.getAdminPath() + "/person/owner/?repage";
        }
    }

    @RequiresPermissions("person:owner:edit")
    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public String ajaxSave(Owner owner, Model model, RedirectAttributes redirectAttributes) {
        JSONObject jsonObject = new JSONObject();
        List<Owner> owners = ownerService.findOwnersByCerNoOrMobNoOrTelNo(owner);
        if (CollectionUtils.isNotEmpty(owners)) {// 已有重复的身份证号或者手机号或者电话号
            addMessage(jsonObject, ViewMessageTypeEnum.WARNING, calculateTipMsg(owner, owners));
        } else {// 无重复业主
            ownerService.save(owner);
            jsonObject.put("id", owner.getId());
            jsonObject.put("name", owner.getName());
            jsonObject.put("cellPhone", owner.getCellPhone());
        }
        return jsonObject.toString();
    }

    @RequiresPermissions("person:owner:edit")
    @RequestMapping(value = "delete")
    public String delete(Owner owner, RedirectAttributes redirectAttributes) {
        ownerService.delete(owner);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除业主信息成功");
        return "redirect:" + Global.getAdminPath() + "/person/owner/?repage";
    }

    /**
     * 计算返回的错误提示信息
     */
    private String calculateTipMsg(Owner oriOwner, List<Owner> owners) {
        if (oriOwner.getSocialNumber().equals(owners.get(0).getSocialNumber())) {
            return "业主的身份证号码已经存在，不能重复添加";
        }
        if (oriOwner.getCellPhone().equals(owners.get(0).getCellPhone())) {
            return "业主的手机号码已经存在，不能重复添加";
        }
        if (oriOwner.getDeskPhone().equals(owners.get(0).getDeskPhone())) {
            return "业主的座机号码已经存在，不能重复添加";
        }
        return "业主的身份证号码或者手机号码或座机号码已经存在，不能重复添加";
    }

    @RequiresPermissions("person:owner:view")
    @RequestMapping(value = {"syncAjaxQuery"})
    @ResponseBody
    public String syncAjaxQuery(String q) {
        List<Owner> ownerList = ownerService.findListByWord(q);
        List<HashMap<String, String>> resultMapList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ownerList)) {
            for (Owner o : ownerList) {
                HashMap<String, String> tempMap = new HashMap<String, String>();
                tempMap.put("id", o.getId());
                tempMap.put("text", o.getName() + "-" + o.getSocialNumber() + "-" + o.getCellPhone() + (StringUtils.isNotEmpty(o.getSecondCellPhone()) ? "/" + o.getSecondCellPhone() : ""));
                resultMapList.add(tempMap);
            }
        }
        return JsonMapper.toJsonString(resultMapList);
    }

}
