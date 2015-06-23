/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.web;

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
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.person.entity.Customer;
import com.thinkgem.jeesite.modules.person.service.CustomerService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

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
		customerService.save(customer);
		addMessage(redirectAttributes, "保存客户信息成功");
		return "redirect:" + Global.getAdminPath() + "/person/customer/?repage";
	}

	@RequiresPermissions("person:customer:edit")
	@RequestMapping(value = "delete")
	public String delete(Customer customer, RedirectAttributes redirectAttributes) {
		customerService.delete(customer);
		addMessage(redirectAttributes, "删除客户信息成功");
		return "redirect:" + Global.getAdminPath() + "/person/customer/?repage";
	}

}