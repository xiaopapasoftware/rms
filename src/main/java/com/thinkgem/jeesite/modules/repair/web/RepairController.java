/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.repair.web;

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
import com.thinkgem.jeesite.modules.repair.entity.Repair;
import com.thinkgem.jeesite.modules.repair.service.RepairService;

/**
 * 报修Controller
 * @author daniel
 * @version 2015-12-06
 */
@Controller
@RequestMapping(value = "${adminPath}/repair/repair")
public class RepairController extends BaseController {

	@Autowired
	private RepairService repairService;
	
	@ModelAttribute
	public Repair get(@RequestParam(required=false) String id) {
		Repair entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = repairService.get(id);
		}
		if (entity == null){
			entity = new Repair();
		}
		return entity;
	}
	
	//@RequiresPermissions("repair:repair:view")
	@RequestMapping(value = {"list", ""})
	public String list(Repair repair, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Repair> page = repairService.findPage(new Page<Repair>(request, response), repair); 
		model.addAttribute("page", page);
		return "modules/repair/repairList";
	}

	//@RequiresPermissions("repair:repair:view")
	@RequestMapping(value = "form")
	public String form(Repair repair, Model model) {
		model.addAttribute("repair", repair);
		return "modules/repair/repairForm";
	}

	//@RequiresPermissions("repair:repair:edit")
	@RequestMapping(value = "save")
	public String save(Repair repair, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, repair)){
			return form(repair, model);
		}
		repairService.save(repair);
		addMessage(redirectAttributes, "保存报修成功");
		return "redirect:"+Global.getAdminPath()+"/repair/repair/?repage";
	}
	
	//@RequiresPermissions("repair:repair:edit")
	@RequestMapping(value = "delete")
	public String delete(Repair repair, RedirectAttributes redirectAttributes) {
		repairService.delete(repair);
		addMessage(redirectAttributes, "删除报修成功");
		return "redirect:"+Global.getAdminPath()+"/repair/repair/?repage";
	}

}