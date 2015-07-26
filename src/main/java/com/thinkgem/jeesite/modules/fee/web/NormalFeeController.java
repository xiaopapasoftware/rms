/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.fee.web;

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
import com.thinkgem.jeesite.modules.fee.entity.NormalFee;
import com.thinkgem.jeesite.modules.fee.service.NormalFeeService;

/**
 * 一般费用结算Controller
 * @author huangsc
 * @version 2015-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/fee/normalFee")
public class NormalFeeController extends BaseController {

	@Autowired
	private NormalFeeService normalFeeService;
	
	@ModelAttribute
	public NormalFee get(@RequestParam(required=false) String id) {
		NormalFee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = normalFeeService.get(id);
		}
		if (entity == null){
			entity = new NormalFee();
		}
		return entity;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(NormalFee normalFee, HttpServletRequest request, HttpServletResponse response, Model model) {
		String feeType="",type = "";
		if("water".equals(normalFee.getType())||"0".equals(normalFee.getType())) {
			feeType = "0";
			type = "水";
		} else if("gas".equals(normalFee.getType())||"1".equals(normalFee.getType())) {
			feeType = "1";
			type = "燃气";
		} else if("net".equals(normalFee.getType())||"2".equals(normalFee.getType())) {
			feeType = "2";
			type = "宽带";
		} else if("tv".equals(normalFee.getType())||"3".equals(normalFee.getType())) {
			feeType = "3";
			type = "有线电视";
		}
		normalFee.setFeeType(feeType);
		normalFee.setType(type);
		Page<NormalFee> page = normalFeeService.findPage(new Page<NormalFee>(request, response), normalFee); 
		model.addAttribute("page", page);
		model.addAttribute("normalFee", normalFee);
		return "modules/fee/normalFeeList";
	}

	@RequestMapping(value = "form")
	public String form(NormalFee normalFee, Model model) {
		model.addAttribute("normalFee", normalFee);
		return "modules/fee/normalFeeForm";
	}

	@RequestMapping(value = "save")
	public String save(NormalFee normalFee, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		if (!beanValidator(model, normalFee)){
			return form(normalFee, model);
		}
		normalFeeService.save(normalFee);
		addMessage(redirectAttributes, "缴纳成功");
		return this.list(normalFee, request, response, model);
	}
	
	@RequestMapping(value = "delete")
	public String delete(NormalFee normalFee, RedirectAttributes redirectAttributes) {
		normalFeeService.delete(normalFee);
		addMessage(redirectAttributes, "删除一般费用结算成功");
		return "redirect:"+Global.getAdminPath()+"/fee/normalFee/?repage";
	}

}