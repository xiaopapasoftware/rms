/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import java.util.List;

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
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.service.AgreementChangeService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.TenantService;

/**
 * 协议变更Controller
 * 
 * @author huangsc
 * @version 2015-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/agreementChange")
public class AgreementChangeController extends BaseController {

	@Autowired
	private AgreementChangeService agreementChangeService;
	@Autowired
	private RentContractService rentContractService;
	@Autowired
	private TenantService tenantService;

	@ModelAttribute
	public AgreementChange get(@RequestParam(required = false) String id) {
		AgreementChange entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = agreementChangeService.get(id);
		}
		if (entity == null) {
			entity = new AgreementChange();
		}
		return entity;
	}

	@RequiresPermissions("contract:agreementChange:view")
	@RequestMapping(value = {"list", ""})
	public String list(AgreementChange agreementChange, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<AgreementChange> page = agreementChangeService.findPage(new Page<AgreementChange>(request, response),
				agreementChange);
		model.addAttribute("page", page);
		return "modules/contract/agreementChangeList";
	}

	@RequestMapping(value = "audit")
	public String audit(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
		agreementChangeService.audit(auditHis);

		return list(new AgreementChange(), request, response, model);
	}

	@RequiresPermissions("contract:agreementChange:view")
	@RequestMapping(value = "form")
	public String form(AgreementChange agreementChange, Model model) {
		model.addAttribute("agreementChange", agreementChange);

		if (null != agreementChange && !StringUtils.isBlank(agreementChange.getId())) {
			agreementChange.setLiveList(agreementChangeService.findLiveTenant(agreementChange));
			agreementChange.setTenantList(agreementChangeService.findTenant(agreementChange));
		}

		List<Tenant> tenantList = tenantService.findList(new Tenant());
		model.addAttribute("tenantList", tenantList);

		return "modules/contract/agreementChangeForm";
	}

	@RequiresPermissions("contract:agreementChange:edit")
	@RequestMapping(value = "save")
	public String save(AgreementChange agreementChange, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, agreementChange)) {
			return form(agreementChange, model);
		}
		agreementChangeService.save(agreementChange);
		addMessage(redirectAttributes, "保存协议变更成功");
		return "redirect:" + Global.getAdminPath() + "/contract/agreementChange/?repage";
	}

	@RequiresPermissions("contract:agreementChange:edit")
	@RequestMapping(value = "delete")
	public String delete(AgreementChange agreementChange, RedirectAttributes redirectAttributes) {
		agreementChangeService.delete(agreementChange);
		addMessage(redirectAttributes, "删除协议变更成功");
		return "redirect:" + Global.getAdminPath() + "/contract/agreementChange/?repage";
	}

}