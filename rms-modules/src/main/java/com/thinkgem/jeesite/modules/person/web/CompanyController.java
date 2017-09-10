package com.thinkgem.jeesite.modules.person.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.person.entity.Company;
import com.thinkgem.jeesite.modules.person.service.CompanyService;

/**
 * 企业信息
 */
@Controller
@RequestMapping(value = "${adminPath}/person/company")
public class CompanyController extends BaseController {

  @Autowired
  private CompanyService companyService;

  @ModelAttribute
  public Company get(@RequestParam(required = false) String id) {
    Company entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = companyService.get(id);
    }
    if (entity == null) {
      entity = new Company();
    }
    return entity;
  }

  @RequiresPermissions("person:company:view")
  @RequestMapping(value = {"list", ""})
  public String list(Company company, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<Company> page = companyService.findPage(new Page<Company>(request, response), company);
    model.addAttribute("page", page);
    return "modules/person/companyList";
  }

  @RequiresPermissions("person:company:view")
  @RequestMapping(value = "form")
  public String form(Company company, Model model) {
    model.addAttribute("company", company);
    return "modules/person/companyForm";
  }

  @RequiresPermissions("person:company:edit")
  @RequestMapping(value = "save")
  public String save(Company company, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, company)) {
      return form(company, model);
    }
    List<Company> companys = Lists.newArrayList();
    if (StringUtils.isNotEmpty(company.getIdType()) && StringUtils.isNotEmpty(company.getIdNo())) {
      companys = companyService.findCompanyByIdTypeAndVal(company);
    }
    if (!company.getIsNewRecord()) {// 是更新
      if (CollectionUtils.isNotEmpty(companys)) {
        company.setId(companys.get(0).getId());
      }
      companyService.save(company);
      addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "修改企业信息成功");
      return "redirect:" + Global.getAdminPath() + "/person/company/?repage";
    } else {// 新增
      if (CollectionUtils.isNotEmpty(companys)) {
        addMessage(model, ViewMessageTypeEnum.WARNING, "企业证件类型及证件号码已被占用，不能重复添加!");
        return "modules/person/companyForm";
      } else {
        companyService.save(company);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存企业信息成功");
        return "redirect:" + Global.getAdminPath() + "/person/company/?repage";
      }
    }
  }

  @RequiresPermissions("person:company:edit")
  @RequestMapping(value = "delete")
  public String delete(Company company, RedirectAttributes redirectAttributes) {
    companyService.delete(company);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除企业信息及其企业联系人信息成功");
    return "redirect:" + Global.getAdminPath() + "/person/company/?repage";
  }

}
