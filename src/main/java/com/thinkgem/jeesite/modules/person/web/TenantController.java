/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
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
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.person.entity.Company;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.CompanyService;
import com.thinkgem.jeesite.modules.person.service.TenantService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;

/**
 * 租客信息Controller
 * 
 * @author huangsc
 * @version 2015-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/person/tenant")
public class TenantController extends BaseController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private TenantService tenantService;

	@Autowired
	private ContractTenantDao contractTenantDao;

	@ModelAttribute
	public Tenant get(@RequestParam(required = false) String id) {
		Tenant entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = tenantService.get(id);
		}
		if (entity == null) {
			entity = new Tenant();
		}
		return entity;
	}

	@RequiresPermissions("person:tenant:view")
	@RequestMapping(value = {"list", ""})
	public String list(Tenant tenant, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tenant> page = tenantService.findPage(new Page<Tenant>(request, response), tenant);
		model.addAttribute("page", page);
		model.addAttribute("listUser", systemService.findUser(new User()));
		model.addAttribute("listCompany", companyService.findList(new Company()));
		return "modules/person/tenantList";
	}

	@RequiresPermissions("person:tenant:view")
	@RequestMapping(value = "form")
	public String form(Tenant tenant, Model model) {
		model.addAttribute("tenant", tenant);
		model.addAttribute("listUser", systemService.findUser(new User()));
		model.addAttribute("listCompany", companyService.findList(new Company()));
		return "modules/person/tenantForm";
	}
	
	@RequestMapping(value = "add")
	public String add(Tenant tenant, Model model) {
		model.addAttribute("tenant", tenant);
		model.addAttribute("listUser", systemService.findUser(new User()));
		model.addAttribute("listCompany", companyService.findList(new Company()));
		model.addAttribute("type", tenant.getType());
		return "modules/person/tenantAddDialog";
	}

	@RequiresPermissions("person:tenant:edit")
	@RequestMapping(value = "save")
	public String save(Tenant tenant, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tenant)) {
			return form(tenant, model);
		}
		List<Tenant> tenants = tenantService.findTenantByIdTypeAndNo(tenant);
		if (!tenant.getIsNewRecord()) {// 是更新
			if (CollectionUtils.isNotEmpty(tenants)) {
				tenant.setId(tenants.get(0).getId());
			}
			tenantService.save(tenant);
			addMessage(redirectAttributes, "修改租客信息成功");
			return "redirect:" + Global.getAdminPath() + "/person/tenant/?repage";

		} else {// 是新增
			if (CollectionUtils.isNotEmpty(tenants)) {
				model.addAttribute("message", "该证件类型租客的证件号码已被占用，不能重复添加");
				model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());
				model.addAttribute("listCompany", companyService.findList(new Company()));
				model.addAttribute("listUser", systemService.findUser(new User()));
				return "modules/person/tenantForm";
			} else {
				tenantService.save(tenant);
				addMessage(redirectAttributes, "保存租客信息成功");
				return "redirect:" + Global.getAdminPath() + "/person/tenant/?repage";
			}
		}
	}
	
	@RequestMapping(value = "ajaxSave")
	@ResponseBody
	public String ajaxSave(Tenant tenant, Model model, RedirectAttributes redirectAttributes) {
		JSONObject jsonObject = new JSONObject();
		List<Tenant> tenants = tenantService.findTenantByIdTypeAndNo(tenant);
		if (CollectionUtils.isNotEmpty(tenants)) {
			model.addAttribute("listCompany", companyService.findList(new Company()));
			model.addAttribute("listUser", systemService.findUser(new User()));
			jsonObject.put("message",  "该证件类型租客的证件号码已被占用，不能重复添加");
		} else {
			String id = tenantService.saveAndReturnId(tenant);
			jsonObject.put("id", id);
			jsonObject.put("name", tenant.getLabel());
		}
		
		return jsonObject.toString();
	}

	@RequiresPermissions("person:tenant:edit")
	@RequestMapping(value = "delete")
	public String delete(Tenant tenant, RedirectAttributes redirectAttributes) {
		ContractTenant ct = new ContractTenant();
		ct.setTenantId(tenant.getId());
		List<ContractTenant> cts = contractTenantDao.findList(ct);
		if (CollectionUtils.isNotEmpty(cts)) {
			addMessage(redirectAttributes, "租客已预付定金或生成合同，不能删除");
		} else {
			tenantService.delete(tenant);
			addMessage(redirectAttributes, "删除租客信息成功");
		}
		return "redirect:" + Global.getAdminPath() + "/person/tenant/?repage";
	}

}