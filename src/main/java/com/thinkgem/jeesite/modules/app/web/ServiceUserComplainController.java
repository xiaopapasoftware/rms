/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.web;

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
import com.thinkgem.jeesite.modules.app.entity.ServiceUserComplain;
import com.thinkgem.jeesite.modules.app.service.ServiceUserComplainService;

/**
 * 管家投拆Controller
 * @author daniel
 * @version 2016-05-30
 */
@Controller
@RequestMapping(value = "${adminPath}/app/serviceUserComplain")
public class ServiceUserComplainController extends BaseController {

	@Autowired
	private ServiceUserComplainService serviceUserComplainService;
	
	@ModelAttribute
	public ServiceUserComplain get(@RequestParam(required=false) String id) {
		ServiceUserComplain entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = serviceUserComplainService.get(id);
		}
		if (entity == null){
			entity = new ServiceUserComplain();
		}
		return entity;
	}
    //@RequiresPermissions("app:serviceUserComplain:view")
	@RequestMapping(value = {"list", ""})
	public String list(ServiceUserComplain serviceUserComplain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ServiceUserComplain> page = serviceUserComplainService.findPage(new Page<ServiceUserComplain>(request, response), serviceUserComplain); 
		model.addAttribute("page", page);
		return "modules/app/serviceUserComplainList";
	}

	//@RequiresPermissions("app:serviceUserComplain:view")
	@RequestMapping(value = "form")
	public String form(ServiceUserComplain serviceUserComplain, Model model) {
		model.addAttribute("serviceUserComplain", serviceUserComplain);
		return "modules/app/serviceUserComplainForm";
	}

	//@RequiresPermissions("app:serviceUserComplain:edit")
	@RequestMapping(value = "save")
	public String save(ServiceUserComplain serviceUserComplain, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, serviceUserComplain)){
			return form(serviceUserComplain, model);
		}
		serviceUserComplainService.save(serviceUserComplain);
		addMessage(redirectAttributes, "保存管家投拆成功");
		return "redirect:"+Global.getAdminPath()+"/app/serviceUserComplain/?repage";
	}
	
	//@RequiresPermissions("app:serviceUserComplain:edit")
	@RequestMapping(value = "delete")
	public String delete(ServiceUserComplain serviceUserComplain, RedirectAttributes redirectAttributes) {
		serviceUserComplainService.delete(serviceUserComplain);
		addMessage(redirectAttributes, "删除管家投拆成功");
		return "redirect:"+Global.getAdminPath()+"/app/serviceUserComplain/?repage";
	}

}