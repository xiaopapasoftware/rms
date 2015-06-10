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
import com.thinkgem.jeesite.modules.person.entity.Partner;
import com.thinkgem.jeesite.modules.person.service.PartnerService;

/**
 * 合作人信息Controller
 * @author huangsc
 * @version 2015-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/person/partner")
public class PartnerController extends BaseController {

	@Autowired
	private PartnerService partnerService;
	
	@ModelAttribute
	public Partner get(@RequestParam(required=false) String id) {
		Partner entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = partnerService.get(id);
		}
		if (entity == null){
			entity = new Partner();
		}
		return entity;
	}
	
	@RequiresPermissions("person:partner:view")
	@RequestMapping(value = {"list", ""})
	public String list(Partner partner, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Partner> page = partnerService.findPage(new Page<Partner>(request, response), partner); 
		model.addAttribute("page", page);
		return "modules/person/partnerList";
	}

	@RequiresPermissions("person:partner:view")
	@RequestMapping(value = "form")
	public String form(Partner partner, Model model) {
		model.addAttribute("partner", partner);
		return "modules/person/partnerForm";
	}

	@RequiresPermissions("person:partner:edit")
	@RequestMapping(value = "save")
	public String save(Partner partner, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, partner)){
			return form(partner, model);
		}
		partnerService.save(partner);
		addMessage(redirectAttributes, "保存合作人信息成功");
		return "redirect:"+Global.getAdminPath()+"/person/partner/?repage";
	}
	
	@RequiresPermissions("person:partner:edit")
	@RequestMapping(value = "delete")
	public String delete(Partner partner, RedirectAttributes redirectAttributes) {
		partnerService.delete(partner);
		addMessage(redirectAttributes, "删除合作人信息成功");
		return "redirect:"+Global.getAdminPath()+"/person/partner/?repage";
	}

}