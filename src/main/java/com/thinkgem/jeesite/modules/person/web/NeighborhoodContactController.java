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
import com.thinkgem.jeesite.modules.person.entity.NeighborhoodContact;
import com.thinkgem.jeesite.modules.person.service.NeighborhoodContactService;

/**
 * 居委会联系人Controller
 * @author huangsc
 * @version 2015-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/person/neighborhoodContact")
public class NeighborhoodContactController extends BaseController {

	@Autowired
	private NeighborhoodContactService neighborhoodContactService;
	
	@ModelAttribute
	public NeighborhoodContact get(@RequestParam(required=false) String id) {
		NeighborhoodContact entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = neighborhoodContactService.get(id);
		}
		if (entity == null){
			entity = new NeighborhoodContact();
		}
		return entity;
	}
	
	@RequiresPermissions("person:neighborhoodContact:view")
	@RequestMapping(value = {"list", ""})
	public String list(NeighborhoodContact neighborhoodContact, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NeighborhoodContact> page = neighborhoodContactService.findPage(new Page<NeighborhoodContact>(request, response), neighborhoodContact); 
		model.addAttribute("page", page);
		return "modules/person/neighborhoodContactList";
	}

	@RequiresPermissions("person:neighborhoodContact:view")
	@RequestMapping(value = "form")
	public String form(NeighborhoodContact neighborhoodContact, Model model) {
		model.addAttribute("neighborhoodContact", neighborhoodContact);
		return "modules/person/neighborhoodContactForm";
	}

	@RequiresPermissions("person:neighborhoodContact:edit")
	@RequestMapping(value = "save")
	public String save(NeighborhoodContact neighborhoodContact, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, neighborhoodContact)){
			return form(neighborhoodContact, model);
		}
		neighborhoodContactService.save(neighborhoodContact);
		addMessage(redirectAttributes, "保存居委会联系人成功");
		return "redirect:"+Global.getAdminPath()+"/person/neighborhoodContact/?repage";
	}
	
	@RequiresPermissions("person:neighborhoodContact:edit")
	@RequestMapping(value = "delete")
	public String delete(NeighborhoodContact neighborhoodContact, RedirectAttributes redirectAttributes) {
		neighborhoodContactService.delete(neighborhoodContact);
		addMessage(redirectAttributes, "删除居委会联系人成功");
		return "redirect:"+Global.getAdminPath()+"/person/neighborhoodContact/?repage";
	}

}