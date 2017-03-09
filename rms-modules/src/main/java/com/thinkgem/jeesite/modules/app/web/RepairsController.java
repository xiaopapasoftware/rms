/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.app.entity.Repairs;
import com.thinkgem.jeesite.modules.app.service.RepairsService;

/**
 * 报修Controller
 * @author huangsc
 * @version 2015-12-14
 */
@Controller
@RequestMapping(value = "${adminPath}/app/repairs")
public class RepairsController extends BaseController {

	@Autowired
	private RepairsService repairsService;
	
	@ModelAttribute
	public Repairs get(@RequestParam(required=false) String id) {
		Repairs entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = repairsService.get(id);
		}
		if (entity == null){
			entity = new Repairs();
		}
		return entity;
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(Repairs repairs, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Repairs> page = repairsService.findPage(new Page<Repairs>(request, response), repairs); 
		model.addAttribute("page", page);
		return "modules/app/repairsList";
	}


	@RequestMapping(value = "form")
	public String form(Repairs repairs, Model model) {
		model.addAttribute("repairs", repairs);
		return "modules/app/repairsForm";
	}

	@RequestMapping(value = "save")
	public String save(Repairs repairs, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, repairs)){
			return form(repairs, model);
		}
		repairsService.save(repairs);
		addMessage(redirectAttributes, "保存报修成功");
		return "redirect:"+Global.getAdminPath()+"/app/repairs/?repage";
	}
	

	@RequestMapping(value = "delete")
	public String delete(Repairs repairs, RedirectAttributes redirectAttributes) {
		repairsService.delete(repairs);
		addMessage(redirectAttributes, "删除报修成功");
		return "redirect:"+Global.getAdminPath()+"/app/repairs/?repage";
	}

}