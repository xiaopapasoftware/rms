package com.thinkgem.jeesite.modules.person.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.entity.User;
import com.thinkgem.jeesite.modules.person.entity.Company;
import com.thinkgem.jeesite.modules.person.entity.Customer;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.CompanyService;
import com.thinkgem.jeesite.modules.person.service.CustomerService;
import com.thinkgem.jeesite.modules.person.service.TenantService;
import com.thinkgem.jeesite.modules.service.SystemService;
import com.thinkgem.jeesite.modules.utils.DictUtils;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.List;

/**
 * 客户
 */
@Controller
@RequestMapping(value = "${adminPath}/person/customer")
public class CustomerController extends BaseController {

  @Autowired
  private SystemService systemService;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private TenantService tenantService;

  @ModelAttribute
  public Customer get(@RequestParam(required = false) String id) {
    Customer entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = customerService.get(id);
    }
    if (entity == null) {
      entity = new Customer();
    }
    return entity;
  }

  @RequiresPermissions("person:customer:view")
  @RequestMapping(value = {"list", ""})
  public String list(Customer customer, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<Customer> page = customerService.findPage(new Page<Customer>(request, response), customer);
    model.addAttribute("page", page);
    model.addAttribute("listUser", systemService.findUser(new User()));
    return "modules/person/customerList";
  }

  @RequiresPermissions("person:customer:view")
  @RequestMapping(value = "form")
  public String form(Customer customer, Model model) {
    model.addAttribute("customer", customer);
    model.addAttribute("listUser", systemService.findUser(new User()));
    return "modules/person/customerForm";
  }

  @RequiresPermissions("person:customer:edit")
  @RequestMapping(value = "save")
  public String save(Customer customer, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, customer)) {
      return form(customer, model);
    }
    List<Customer> customers = customerService.findCustomerByTelNo(customer);
    if (!customer.getIsNewRecord()) {// 是更新
      if (CollectionUtils.isNotEmpty(customers)) {
        customer.setId(customers.get(0).getId());
      }
      customerService.save(customer);
      addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "修改客户信息成功");
      return "redirect:" + Global.getAdminPath() + "/person/customer/?repage";
    } else {// 新增
      if (CollectionUtils.isNotEmpty(customers)) {
        addMessage(model, ViewMessageTypeEnum.WARNING, "客户手机号已被占用，不能重复添加");
        model.addAttribute("listUser", systemService.findUser(new User()));
        return "modules/person/customerForm";
      } else {
        customer.setIsTenant(DictUtils.getDictValue("否", "yes_no", ""));
        customerService.save(customer);
        addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存客户信息成功");
        return "redirect:" + Global.getAdminPath() + "/person/customer/?repage";
      }
    }

  }

  @RequiresPermissions("person:customer:edit")
  @RequestMapping(value = "delete")
  public String delete(Customer customer, RedirectAttributes redirectAttributes) {
    customerService.delete(customer);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除客户信息成功");
    return "redirect:" + Global.getAdminPath() + "/person/customer/?repage";
  }

  /**
   * 由用户转为租客
   */
  @RequiresPermissions("person:customer:edit")
  @RequestMapping(value = "convertToTenant")
  public String convertToTenant(Customer customer, HttpServletRequest request, HttpServletResponse response, Model model) {
    Tenant tenant = new Tenant();
    tenant.setCellPhone(customer.getCellPhone());
    tenant.setTenantName(customer.getTrueName());
    tenant.setGender(customer.getGender());
    tenant.setRemarks(customer.getRemarks());
    tenant.setUser(customer.getUser());
    tenant.setCustomerId(customer.getId());
    model.addAttribute("tenant", tenant);
    model.addAttribute("listUser", systemService.findUser(new User()));
    model.addAttribute("listCompany", companyService.findList(new Company()));
    return "modules/person/tenantAdd";
  }

  /**
   * 客户转租客，保存租客信息。
   */
  @RequiresPermissions("person:customer:edit")
  @RequestMapping(value = "saveTenant")
  public String saveTenant(Tenant tenant, Model model, RedirectAttributes redirectAttributes) {
    List<Tenant> tenants = tenantService.findTenantByIdTypeAndNo(tenant);
    if (CollectionUtils.isNotEmpty(tenants)) {
      addMessage(model, ViewMessageTypeEnum.WARNING, "该证件类型租客的证件号码或手机号已被占用，不能重复添加");
      model.addAttribute("listCompany", companyService.findList(new Company()));
      model.addAttribute("listUser", systemService.findUser(new User()));
      return "modules/person/tenantAdd";
    } else {
      tenantService.saveAndUpdateCusStat(tenant);
      addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存租客信息成功");
      return "redirect:" + Global.getAdminPath() + "/person/customer/?repage";
    }
  }

}
