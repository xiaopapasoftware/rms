package com.thinkgem.jeesite.modules.inventory.web;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inventory.entity.ManagementCompany;
import com.thinkgem.jeesite.modules.inventory.service.ManagementCompanyService;
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
import java.util.List;

/**
 * 物业公司Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/managementCompany")
public class ManagementCompanyController extends BaseController {

    @Autowired
    private ManagementCompanyService managementCompanyService;

    @ModelAttribute
    public ManagementCompany get(@RequestParam(required = false) String id) {
        ManagementCompany entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = managementCompanyService.get(id);
        }
        if (entity == null) {
            entity = new ManagementCompany();
        }
        return entity;
    }

    @RequiresPermissions("inventory:managementCompany:view")
    @RequestMapping(value = {"list", ""})
    public String list(ManagementCompany managementCompany, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ManagementCompany> page = managementCompanyService.findPage(new Page<ManagementCompany>(request, response), managementCompany);
        model.addAttribute("page", page);
        return "modules/inventory/managementCompanyList";
    }

    @RequiresPermissions("inventory:managementCompany:view")
    @RequestMapping(value = "form")
    public String form(ManagementCompany managementCompany, Model model) {
        model.addAttribute("managementCompany", managementCompany);
        return "modules/inventory/managementCompanyForm";
    }

    @RequestMapping(value = "add")
    public String add(ManagementCompany managementCompany, Model model) {
        model.addAttribute("managementCompany", managementCompany);
        return "modules/inventory/managementCompanyAdd";
    }

    @RequiresPermissions("inventory:managementCompany:edit")
    @RequestMapping(value = "save")
    public String save(ManagementCompany managementCompany, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, managementCompany)) {
            return form(managementCompany, model);
        }
        List<ManagementCompany> managementCompanys = managementCompanyService.findCompanyByNameAndAddress(managementCompany);
        if (!managementCompany.getIsNewRecord()) {// 是更新
            // 是更新
            if (CollectionUtils.isNotEmpty(managementCompanys)) {// 只更新备注
                managementCompany.setId(managementCompanys.get(0).getId());
            }
            managementCompanyService.save(managementCompany);
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "修改物业公司成功");
            return "redirect:" + Global.getAdminPath() + "/inventory/managementCompany/?repage";

        } else {// 是新增
            if (CollectionUtils.isNotEmpty(managementCompanys)) {
                addMessage(model, ViewMessageTypeEnum.WARNING, "物业公司名称及地址已被使用，不能重复添加");
                return "modules/inventory/managementCompanyForm";
            } else {
                managementCompanyService.save(managementCompany);
                addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存物业公司成功");
                return "redirect:" + Global.getAdminPath() + "/inventory/managementCompany/?repage";
            }
        }

    }

    @RequiresPermissions("inventory:managementCompany:edit")
    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public String ajaxSave(ManagementCompany managementCompany, Model model, RedirectAttributes redirectAttributes) {
        JSONObject jsonObject = new JSONObject();
        List<ManagementCompany> managementCompanys = managementCompanyService.findCompanyByNameAndAddress(managementCompany);
        if (CollectionUtils.isNotEmpty(managementCompanys)) {
            addMessage(jsonObject, ViewMessageTypeEnum.ERROR, "物业公司名称及地址已被使用，不能重复添加");
        } else {
            managementCompanyService.save(managementCompany);
            jsonObject.put("id", managementCompany.getId());
            jsonObject.put("name", managementCompany.getCompanyName());
        }
        return jsonObject.toString();
    }

    @RequiresPermissions("inventory:managementCompany:edit")
    @RequestMapping(value = "delete")
    public String delete(ManagementCompany managementCompany, RedirectAttributes redirectAttributes) {
        managementCompanyService.delete(managementCompany);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除物业公司及其所有联系人成功");
        return "redirect:" + Global.getAdminPath() + "/inventory/managementCompany/?repage";
    }

}
