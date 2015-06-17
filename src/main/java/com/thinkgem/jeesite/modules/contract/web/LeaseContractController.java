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
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.service.AuditHisService;
import com.thinkgem.jeesite.modules.contract.service.LeaseContractService;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.person.entity.Remittancer;
import com.thinkgem.jeesite.modules.person.service.RemittancerService;

/**
 * 承租合同Controller
 * @author huangsc
 * @version 2015-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/leaseContract")
public class LeaseContractController extends BaseController {

	@Autowired
	private LeaseContractService leaseContractService;
	@Autowired
	private PropertyProjectService propertyProjectService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private RemittancerService remittancerService;
	@Autowired
	private AuditHisService auditHisService;
	
	@ModelAttribute
	public LeaseContract get(@RequestParam(required=false) String id) {
		LeaseContract entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = leaseContractService.get(id);
		}
		if (entity == null){
			entity = new LeaseContract();
		}
		return entity;
	}
	
	@RequiresPermissions("contract:leaseContract:view")
	@RequestMapping(value = {"list", ""})
	public String list(LeaseContract leaseContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<LeaseContract> page = leaseContractService.findPage(new Page<LeaseContract>(request, response), leaseContract); 
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);
		
		return "modules/contract/leaseContractList";
	}
	
	@RequestMapping(value = "audit")
	public String audit(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
		leaseContractService.audit(auditHis);
		
		return list(new LeaseContract(),request,response,model);
	}
	
	@RequestMapping(value = "auditHis")
	public String auditHis(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AuditHis> page = auditHisService.findPage(new Page<AuditHis>(request, response), auditHis);
		model.addAttribute("page", page);
		return "modules/contract/auditHis";
	}

	@RequiresPermissions("contract:leaseContract:view")
	@RequestMapping(value = "form")
	public String form(LeaseContract leaseContract, Model model) {
		model.addAttribute("leaseContract", leaseContract);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);
		
		List<Remittancer> remittancerList = remittancerService.findList(new Remittancer());
		model.addAttribute("remittancerList", remittancerList);
		
		return "modules/contract/leaseContractForm";
	}

	@RequiresPermissions("contract:leaseContract:edit")
	@RequestMapping(value = "save")
	public String save(LeaseContract leaseContract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, leaseContract)){
			return form(leaseContract, model);
		}
		leaseContractService.save(leaseContract);
		addMessage(redirectAttributes, "保存承租合同成功");
		return "redirect:"+Global.getAdminPath()+"/contract/leaseContract/?repage";
	}
	
	@RequiresPermissions("contract:leaseContract:edit")
	@RequestMapping(value = "delete")
	public String delete(LeaseContract leaseContract, RedirectAttributes redirectAttributes) {
		leaseContractService.delete(leaseContract);
		addMessage(redirectAttributes, "删除承租合同成功");
		return "redirect:"+Global.getAdminPath()+"/contract/leaseContract/?repage";
	}

}