/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

package com.thinkgem.jeesite.modules.funds.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.funds.entity.PaymenttransDtl;
import com.thinkgem.jeesite.modules.funds.service.PaymenttransDtlService;

/**
 * fundsController
 * 
 * @author funds
 * @version 2017-10-21
 */
@Controller
@RequestMapping(value = "${adminPath}/funds/funds/paymenttransDtl")
public class PaymenttransDtlController extends BaseController {

  @Autowired
  private PaymenttransDtlService paymenttransDtlService;

  @ModelAttribute
  public PaymenttransDtl get(@RequestParam(required = false) String id) {
    PaymenttransDtl entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = paymenttransDtlService.get(id);
    }
    if (entity == null) {
      entity = new PaymenttransDtl();
    }
    return entity;
  }

  @RequiresPermissions("funds:funds:paymenttransDtl:view")
  @RequestMapping(value = {"list", ""})
  public String list(PaymenttransDtl paymenttransDtl, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<PaymenttransDtl> page = paymenttransDtlService.findPage(new Page<PaymenttransDtl>(request, response), paymenttransDtl);
    model.addAttribute("page", page);
    return "funds/funds/funds/paymenttransDtlList";
  }

  @RequiresPermissions("funds:funds:paymenttransDtl:view")
  @RequestMapping(value = "form")
  public String form(PaymenttransDtl paymenttransDtl, Model model) {
    model.addAttribute("paymenttransDtl", paymenttransDtl);
    return "funds/funds/funds/paymenttransDtlForm";
  }

  @RequiresPermissions("funds:funds:paymenttransDtl:edit")
  @RequestMapping(value = "save")
  public String save(PaymenttransDtl paymenttransDtl, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, paymenttransDtl)) {
      return form(paymenttransDtl, model);
    }
    paymenttransDtlService.save(paymenttransDtl);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存funds成功");
    return "redirect:" + Global.getAdminPath() + "/funds/funds/paymenttransDtl/?repage";
  }

  @RequiresPermissions("funds:funds:paymenttransDtl:edit")
  @RequestMapping(value = "delete")
  public String delete(PaymenttransDtl paymenttransDtl, RedirectAttributes redirectAttributes) {
    paymenttransDtlService.delete(paymenttransDtl);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除funds成功");
    return "redirect:" + Global.getAdminPath() + "/funds/funds/paymenttransDtl/?repage";
  }

}
