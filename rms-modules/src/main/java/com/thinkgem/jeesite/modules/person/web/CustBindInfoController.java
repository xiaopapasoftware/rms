package com.thinkgem.jeesite.modules.person.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.CustBindInfo;
import com.thinkgem.jeesite.modules.app.service.CustBindInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * APP用户Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/person/custBindInfo")
public class CustBindInfoController extends BaseController {

  @Autowired
  private CustBindInfoService custBindInfoService;

  @ModelAttribute
  public CustBindInfo get(@RequestParam(required = false) String id) {
    CustBindInfo entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = custBindInfoService.get(id);
    }
    if (entity == null) {
      entity = new CustBindInfo();
    }
    return entity;
  }

  @RequiresPermissions("person:custBindInfo:view")
  @RequestMapping(value = {"list", ""})
  public String list(CustBindInfo custBindInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<CustBindInfo> page = custBindInfoService.findPage(new Page<>(request, response), custBindInfo);
    model.addAttribute("page", page);
    return "modules/person/custBindInfoList";
  }

  @RequiresPermissions("person:custBindInfo:view")
  @RequestMapping(value = "form")
  public String form(CustBindInfo custBindInfo, Model model) {
    model.addAttribute("custBindInfo", custBindInfo);
    return "modules/person/custBindInfoForm";
  }

  @RequiresPermissions("person:custBindInfo:edit")
  @RequestMapping(value = "save")
  public String save(CustBindInfo custBindInfo, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, custBindInfo)) {
      return form(custBindInfo, model);
    }
    custBindInfoService.save(custBindInfo);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存用户成功");
    return "redirect:" + Global.getAdminPath() + "/person/custBindInfo/?repage";
  }

  @RequiresPermissions("person:custBindInfo:edit")
  @RequestMapping(value = "delete")
  public String delete(CustBindInfo custBindInfo, RedirectAttributes redirectAttributes) {
    custBindInfoService.delete(custBindInfo);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除用户成功");
    return "redirect:" + Global.getAdminPath() + "/person/custBindInfo/?repage";
  }

}
