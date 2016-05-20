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
import com.thinkgem.jeesite.modules.app.entity.Questions;
import com.thinkgem.jeesite.modules.app.service.QuestionsService;

/**
 * 常见问题Controller
 * @author daniel
 * @version 2016-05-10
 */
@Controller
@RequestMapping(value = "${adminPath}/app/questions")
public class QuestionsController extends BaseController {

	@Autowired
	private QuestionsService questionsService;
	
	@ModelAttribute
	public Questions get(@RequestParam(required=false) String id) {
		Questions entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = questionsService.get(id);
		}
		if (entity == null){
			entity = new Questions();
		}
		return entity;
	}
	
	//@RequiresPermissions("app:questions:view")
	@RequestMapping(value = {"list", ""})
	public String list(Questions questions, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Questions> page = questionsService.findPage(new Page<Questions>(request, response), questions); 
		model.addAttribute("page", page);
		return "modules/app/questionsList";
	}

	//@RequiresPermissions("app:questions:view")
	@RequestMapping(value = "form")
	public String form(Questions questions, Model model) {
		model.addAttribute("questions", questions);
		return "modules/app/questionsForm";
	}

	//@RequiresPermissions("app:questions:edit")
	@RequestMapping(value = "save")
	public String save(Questions questions, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, questions)){
			return form(questions, model);
		}
		questionsService.save(questions);
		addMessage(redirectAttributes, "保存常见问题成功");
		return "redirect:"+Global.getAdminPath()+"/app/questions/?repage";
	}
	
	//@RequiresPermissions("app:questions:edit")
	@RequestMapping(value = "delete")
	public String delete(Questions questions, RedirectAttributes redirectAttributes) {
		questionsService.delete(questions);
		addMessage(redirectAttributes, "删除常见问题成功");
		return "redirect:"+Global.getAdminPath()+"/app/questions/?repage";
	}

}