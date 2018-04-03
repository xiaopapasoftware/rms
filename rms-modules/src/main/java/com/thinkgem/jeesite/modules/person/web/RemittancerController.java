package com.thinkgem.jeesite.modules.person.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
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

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.person.entity.Remittancer;
import com.thinkgem.jeesite.modules.person.service.RemittancerService;

/**
 * 汇款人信息
 */
@Controller
@RequestMapping(value = "${adminPath}/person/remittancer")
public class RemittancerController extends BaseController {

    @Autowired
    private RemittancerService remittancerService;

    @ModelAttribute
    public Remittancer get(@RequestParam(required = false) String id) {
        Remittancer entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = remittancerService.get(id);
        }
        if (entity == null) {
            entity = new Remittancer();
        }
        return entity;
    }

    @RequiresPermissions("person:remittancer:view")
    @RequestMapping(value = {""})
    public String listNoQuery(Remittancer remittancer, HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/person/remittancerList";
    }

    @RequiresPermissions("person:remittancer:view")
    @RequestMapping(value = {"list"})
    public String listQuery(Remittancer remittancer, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Remittancer> page = remittancerService.findPage(new Page<Remittancer>(request, response), remittancer);
        model.addAttribute("page", page);
        return "modules/person/remittancerList";
    }

    @RequiresPermissions("person:remittancer:view")
    @RequestMapping(value = "form")
    public String form(Remittancer remittancer, Model model) {
        model.addAttribute("remittancer", remittancer);
        return "modules/person/remittancerForm";
    }

    @RequiresPermissions("person:remittancer:edit")
    @RequestMapping(value = "add")
    public String add(Remittancer remittancer, Model model) {
        model.addAttribute("remittancer", remittancer);
        return "modules/person/remittancerAdd";
    }

    @RequiresPermissions("person:remittancer:edit")
    @RequestMapping(value = "save")
    public String save(Remittancer remittancer, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, remittancer)) {
            return form(remittancer, model);
        }
        List<Remittancer> remittancers = remittancerService.findRemittancersByBankNameAndNo(remittancer);
        if (!remittancer.getIsNewRecord()) {// 是更新
            if (CollectionUtils.isNotEmpty(remittancers)) {
                remittancer.setId(remittancers.get(0).getId());
            }
            remittancerService.save(remittancer);
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "修改汇款人信息成功");
            return "redirect:" + Global.getAdminPath() + "/person/remittancer/?repage";

        } else {// 是新增
            if (CollectionUtils.isNotEmpty(remittancers)) {
                addMessage(model, ViewMessageTypeEnum.WARNING, "汇款人的开户行名称及开户行账号已被占用，不能重复添加");
                return "modules/person/remittancerForm";
            } else {
                remittancerService.save(remittancer);
                addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存汇款人信息成功");
                return "redirect:" + Global.getAdminPath() + "/person/remittancer/?repage";
            }
        }
    }

    @RequiresPermissions("person:remittancer:edit")
    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public String ajaxSave(Remittancer remittancer, Model model, RedirectAttributes redirectAttributes) {
        JSONObject jsonObject = new JSONObject();
        List<Remittancer> remittancers = remittancerService.findRemittancersByBankNameAndNo(remittancer);
        if (CollectionUtils.isNotEmpty(remittancers)) {
            addMessage(jsonObject, ViewMessageTypeEnum.WARNING, "汇款人的开户行名称及开户行账号已被占用，不能重复添加");
        } else {
            remittancerService.save(remittancer);
            jsonObject.put("id", remittancer.getId());
            jsonObject.put("name", remittancer.getUserName());
            jsonObject.put("accountNo", remittancer.getBankAccount());
        }
        return jsonObject.toString();
    }

    @RequiresPermissions("person:remittancer:edit")
    @RequestMapping(value = "delete")
    public String delete(Remittancer remittancer, RedirectAttributes redirectAttributes) {
        remittancerService.delete(remittancer);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除汇款人信息成功");
        return "redirect:" + Global.getAdminPath() + "/person/remittancer/?repage";
    }

}
