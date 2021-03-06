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
import com.thinkgem.jeesite.modules.person.entity.Partner;
import com.thinkgem.jeesite.modules.person.service.PartnerService;

/**
 * 合作人信息Controller
 *
 * @author huangsc
 * @version 2015-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/person/partner")
public class PartnerController extends BaseController {

    @Autowired
    private PartnerService partnerService;

    @ModelAttribute
    public Partner get(@RequestParam(required = false) String id) {
        Partner entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = partnerService.get(id);
        }
        if (entity == null) {
            entity = new Partner();
        }
        return entity;
    }

    @RequiresPermissions("person:partner:view")
    @RequestMapping(value = {"list"})
    public String listQuery(Partner partner, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Partner> page = partnerService.findPage(new Page<Partner>(request, response), partner);
        model.addAttribute("page", page);
        return "modules/person/partnerList";
    }

    @RequiresPermissions("person:partner:view")
    @RequestMapping(value = {""})
    public String listNoQuery(Partner partner, HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/person/partnerList";
    }

    @RequiresPermissions("person:partner:view")
    @RequestMapping(value = "form")
    public String form(Partner partner, Model model) {
        model.addAttribute("partner", partner);
        return "modules/person/partnerForm";
    }

    @RequiresPermissions("person:partner:edit")
    @RequestMapping(value = "add")
    public String add(Partner partner, Model model) {
        model.addAttribute("partner", partner);
        return "modules/person/partnerAdd";
    }

    @RequiresPermissions("person:partner:edit")
    @RequestMapping(value = "save")
    public String save(Partner partner, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, partner)) {
            return form(partner, model);
        }
        List<Partner> partners = partnerService.findPartnersByCellNoAndType(partner);
        if (!partner.getIsNewRecord()) {// 是更新
            if (CollectionUtils.isNotEmpty(partners)) {
                partner.setId(partners.get(0).getId());
            }
            partnerService.save(partner);
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "修改合作人信息成功");
            return "redirect:" + Global.getAdminPath() + "/person/partner/?repage";
        } else {// 是新增
            if (CollectionUtils.isNotEmpty(partners)) {
                addMessage(model, ViewMessageTypeEnum.WARNING, "该类型合作人的手机号已被占用，不能重复添加");
                return "modules/person/partnerForm";
            } else {
                partnerService.save(partner);
                addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存合作人信息成功");
                return "redirect:" + Global.getAdminPath() + "/person/partner/?repage";
            }
        }
    }

    @RequiresPermissions("person:partner:edit")
    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public String ajaxSave(Partner partner, Model model, RedirectAttributes redirectAttributes) {
        JSONObject jsonObject = new JSONObject();
        List<Partner> partners = partnerService.findPartnersByCellNoAndType(partner);
        if (CollectionUtils.isNotEmpty(partners)) {
            addMessage(jsonObject, ViewMessageTypeEnum.WARNING, "该类型合作人的手机号已被占用，不能重复添加");
        } else {
            partnerService.save(partner);
            jsonObject.put("id", partner.getId());
            jsonObject.put("name", partner.getPartnerName());
        }
        return jsonObject.toString();
    }

    @RequiresPermissions("person:partner:edit")
    @RequestMapping(value = "delete")
    public String delete(Partner partner, RedirectAttributes redirectAttributes) {
        partnerService.delete(partner);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除合作人信息成功");
        return "redirect:" + Global.getAdminPath() + "/person/partner/?repage";
    }

}
