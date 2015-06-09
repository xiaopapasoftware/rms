/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

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
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.service.LeaseContractService;

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
		return "modules/contract/leaseContractList";
	}

	@RequiresPermissions("contract:leaseContract:view")
	@RequestMapping(value = "form")
	public String form(LeaseContract leaseContract, Model model) {
		model.addAttribute("leaseContract", leaseContract);
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