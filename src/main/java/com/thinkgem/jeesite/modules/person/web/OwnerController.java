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
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import com.thinkgem.jeesite.modules.person.service.OwnerService;

/**
 * 业主信息Controller
 * @author huangsc
 * @version 2015-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/person/owner")
public class OwnerController extends BaseController {

	@Autowired
	private OwnerService ownerService;
	
	@ModelAttribute
	public Owner get(@RequestParam(required=false) String id) {
		Owner entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ownerService.get(id);
		}
		if (entity == null){
			entity = new Owner();
		}
		return entity;
	}
	
	@RequiresPermissions("person:owner:view")
	@RequestMapping(value = {"list", ""})
	public String list(Owner owner, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Owner> page = ownerService.findPage(new Page<Owner>(request, response), owner); 
		model.addAttribute("page", page);
		return "modules/person/ownerList";
	}

	@RequiresPermissions("person:owner:view")
	@RequestMapping(value = "form")
	public String form(Owner owner, Model model) {
		model.addAttribute("owner", owner);
		return "modules/person/ownerForm";
	}

	@RequiresPermissions("person:owner:edit")
	@RequestMapping(value = "save")
	public String save(Owner owner, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, owner)){
			return form(owner, model);
		}
		ownerService.save(owner);
		addMessage(redirectAttributes, "保存业主信息成功");
		return "redirect:"+Global.getAdminPath()+"/person/owner/?repage";
	}
	
	@RequiresPermissions("person:owner:edit")
	@RequestMapping(value = "delete")
	public String delete(Owner owner, RedirectAttributes redirectAttributes) {
		ownerService.delete(owner);
		addMessage(redirectAttributes, "删除业主信息成功");
		return "redirect:"+Global.getAdminPath()+"/person/owner/?repage";
	}

}