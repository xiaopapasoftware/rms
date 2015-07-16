/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
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

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.person.entity.Company;
import com.thinkgem.jeesite.modules.person.entity.Customer;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.CompanyService;
import com.thinkgem.jeesite.modules.person.service.CustomerService;
import com.thinkgem.jeesite.modules.person.service.TenantService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 客户信息Controller
 * 
 * @author huangsc
 * @version 2015-06-06
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
				Customer upCustomer = new Customer();
				upCustomer.setId(customers.get(0).getId());
				upCustomer.setCellPhone(customer.getCellPhone());
				upCustomer.setContactName(customer.getContactName());
				upCustomer.setGender(customer.getGender());
				upCustomer.setIsTenant(customers.get(0).getIsTenant());
				upCustomer.setRemarks(customer.getRemarks());
				upCustomer.setUser(customer.getUser());
				customerService.save(customer);
			} else {
				customerService.save(customer);
			}
			addMessage(redirectAttributes, "修改客户信息成功");
			return "redirect:" + Global.getAdminPath() + "/person/customer/?repage";
		} else {// 新增
			if (CollectionUtils.isNotEmpty(customers)) {
				model.addAttribute("message", "客户手机号已被占用，不能重复添加");
				model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());
				model.addAttribute("listUser", systemService.findUser(new User()));
				return "modules/person/customerForm";
			} else {
				customer.setIsTenant(DictUtils.getDictValue("否", "yes_no", ""));
				customerService.save(customer);
				addMessage(redirectAttributes, "保存客户信息成功");
				return "redirect:" + Global.getAdminPath() + "/person/customer/?repage";
			}
		}

	}

	@RequiresPermissions("person:customer:edit")
	@RequestMapping(value = "delete")
	public String delete(Customer customer, RedirectAttributes redirectAttributes) {
		customerService.delete(customer);
		addMessage(redirectAttributes, "删除客户信息成功");
		return "redirect:" + Global.getAdminPath() + "/person/customer/?repage";
	}

	/**
	 * 由用户转为租客
	 */
	@RequiresPermissions("person:customer:edit")
	@RequestMapping(value = "convertToTenant")
	public String convertToTenant(Customer customer, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Tenant tenant = new Tenant();
		tenant.setCellPhone(customer.getCellPhone());
		tenant.setTenantName(customer.getContactName());
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
	 * */
	@RequiresPermissions("person:customer:edit")
	@RequestMapping(value = "saveTenant")
	public String saveTenant(Tenant tenant, Model model, RedirectAttributes redirectAttributes) {
		List<Tenant> tenants = tenantService.findTenantByIdTypeAndNo(tenant);
		if (CollectionUtils.isNotEmpty(tenants)) {
			model.addAttribute("message", "该证件类型租客的证件号码已被占用，不能重复添加");
			model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());
			model.addAttribute("listCompany", companyService.findList(new Company()));
			model.addAttribute("listUser", systemService.findUser(new User()));
			return "modules/person/tenantAdd";
		} else {
			tenantService.saveAndUpdateCusStat(tenant);
			addMessage(redirectAttributes, "保存租客信息成功");
			return "redirect:" + Global.getAdminPath() + "/person/customer/?repage";
		}
	}

}