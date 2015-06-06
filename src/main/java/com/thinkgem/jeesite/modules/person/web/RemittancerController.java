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
import com.thinkgem.jeesite.modules.person.entity.Remittancer;
import com.thinkgem.jeesite.modules.person.service.RemittancerService;

/**
 * 汇款人信息Controller
 * @author huangsc
 * @version 2015-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/person/remittancer")
public class RemittancerController extends BaseController {

	@Autowired
	private RemittancerService remittancerService;
	
	@ModelAttribute
	public Remittancer get(@RequestParam(required=false) String id) {
		Remittancer entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = remittancerService.get(id);
		}
		if (entity == null){
			entity = new Remittancer();
		}
		return entity;
	}
	
	@RequiresPermissions("person:remittancer:view")
	@RequestMapping(value = {"list", ""})
	public String list(Remittancer remittancer, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Remittancer> page = remittancerService.findPage(new Page<Remittancer>(request, response), remittancer); 
		model.addAttribute("page", page);
		return "modules/person/remittancerList";
	}

	@RequiresPermissions("person:remittancer:view")
	@RequestMapping(value = "form")
	public String form(Remittancer remittancer, Model model) {
		model.addAttribute("remittancer", remittancer);
		return "modules/person/remittancerForm";
	}

	@RequiresPermissions("person:remittancer:edit")
	@RequestMapping(value = "save")
	public String save(Remittancer remittancer, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, remittancer)){
			return form(remittancer, model);
		}
		remittancerService.save(remittancer);
		addMessage(redirectAttributes, "保存汇款人信息成功");
		return "redirect:"+Global.getAdminPath()+"/person/remittancer/?repage";
	}
	
	@RequiresPermissions("person:remittancer:edit")
	@RequestMapping(value = "delete")
	public String delete(Remittancer remittancer, RedirectAttributes redirectAttributes) {
		remittancerService.delete(remittancer);
		addMessage(redirectAttributes, "删除汇款人信息成功");
		return "redirect:"+Global.getAdminPath()+"/person/remittancer/?repage";
	}

}