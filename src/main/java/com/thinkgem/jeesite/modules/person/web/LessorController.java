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
import com.thinkgem.jeesite.modules.person.entity.Lessor;
import com.thinkgem.jeesite.modules.person.service.LessorService;

/**
 * 出租人管理Controller
 * @author wage
 * @version 2015-09-18
 */
@Controller
@RequestMapping(value = "${adminPath}/person/lessor")
public class LessorController extends BaseController {

	@Autowired
	private LessorService lessorService;
	
	@ModelAttribute
	public Lessor get(@RequestParam(required=false) String id) {
		Lessor entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = lessorService.get(id);
		}
		if (entity == null){
			entity = new Lessor();
		}
		return entity;
	}
	
	@RequiresPermissions("person:lessor:view")
	@RequestMapping(value = {"list", ""})
	public String list(Lessor lessor, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Lessor> page = lessorService.findPage(new Page<Lessor>(request, response), lessor); 
		model.addAttribute("page", page);
		return "modules/person/lessorList";
	}

	@RequiresPermissions("person:lessor:view")
	@RequestMapping(value = "form")
	public String form(Lessor lessor, Model model) {
		model.addAttribute("lessor", lessor);
		return "modules/person/lessorForm";
	}

	@RequiresPermissions("person:lessor:edit")
	@RequestMapping(value = "save")
	public String save(Lessor lessor, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, lessor)){
			return form(lessor, model);
		}
		lessorService.save(lessor);
		addMessage(redirectAttributes, "保存出租人管理成功");
		return "redirect:"+Global.getAdminPath()+"/person/lessor/?repage";
	}
	
	@RequiresPermissions("person:lessor:edit")
	@RequestMapping(value = "delete")
	public String delete(Lessor lessor, RedirectAttributes redirectAttributes) {
		lessorService.delete(lessor);
		addMessage(redirectAttributes, "删除出租人管理成功");
		return "redirect:"+Global.getAdminPath()+"/person/lessor/?repage";
	}

}