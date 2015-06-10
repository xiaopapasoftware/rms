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
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;

/**
 * 出租合同Controller
 * @author huangsc
 * @version 2015-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/rentContract")
public class RentContractController extends BaseController {

	@Autowired
	private RentContractService rentContractService;
	
	@ModelAttribute
	public RentContract get(@RequestParam(required=false) String id) {
		RentContract entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = rentContractService.get(id);
		}
		if (entity == null){
			entity = new RentContract();
		}
		return entity;
	}
	
	@RequiresPermissions("contract:rentContract:view")
	@RequestMapping(value = {"list", ""})
	public String list(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RentContract> page = rentContractService.findPage(new Page<RentContract>(request, response), rentContract); 
		model.addAttribute("page", page);
		return "modules/contract/rentContractList";
	}

	@RequiresPermissions("contract:rentContract:view")
	@RequestMapping(value = "form")
	public String form(RentContract rentContract, Model model) {
		model.addAttribute("rentContract", rentContract);
		return "modules/contract/rentContractForm";
	}

	@RequiresPermissions("contract:rentContract:edit")
	@RequestMapping(value = "save")
	public String save(RentContract rentContract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rentContract)){
			return form(rentContract, model);
		}
		rentContractService.save(rentContract);
		addMessage(redirectAttributes, "保存出租合同成功");
		return "redirect:"+Global.getAdminPath()+"/contract/rentContract/?repage";
	}
	
	@RequiresPermissions("contract:rentContract:edit")
	@RequestMapping(value = "delete")
	public String delete(RentContract rentContract, RedirectAttributes redirectAttributes) {
		rentContractService.delete(rentContract);
		addMessage(redirectAttributes, "删除出租合同成功");
		return "redirect:"+Global.getAdminPath()+"/contract/rentContract/?repage";
	}

}