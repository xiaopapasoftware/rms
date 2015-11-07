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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.fee.service.ElectricFeeService;

/**
 * 电费结算Controller
 * @author huangsc
 * @version 2015-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/fee/electricFee")
public class ElectricFeeController extends BaseController {

	@Autowired
	private ElectricFeeService electricFeeService;
	
	@ModelAttribute
	public ElectricFee get(@RequestParam(required=false) String id) {
		ElectricFee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = electricFeeService.get(id);
		}
		if (entity == null){
			entity = new ElectricFee();
		}
		return entity;
	}
	
	//@RequiresPermissions("fee:electricFee:view")
	@RequestMapping(value = {"list", ""})
	public String list(ElectricFee electricFee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ElectricFee> page = electricFeeService.findPage(new Page<ElectricFee>(request, response), electricFee); 
		model.addAttribute("page", page);
		return "modules/fee/electricFeeList";
	}

	//@RequiresPermissions("fee:electricFee:view")
	@RequestMapping(value = "form")
	public String form(ElectricFee electricFee, Model model) {
		model.addAttribute("electricFee", electricFee);
		return "modules/fee/electricFeeForm";
	}

	//@RequiresPermissions("fee:electricFee:edit")
	@RequestMapping(value = "save")
	public String save(ElectricFee electricFee, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, electricFee)){
			return form(electricFee, model);
		}
		electricFeeService.save(electricFee);
		addMessage(redirectAttributes, "电费充值成功");
		return "redirect:"+Global.getAdminPath()+"/fee/electricFee/?repage";
	}
	
	//@RequiresPermissions("fee:electricFee:edit")
	@RequestMapping(value = "delete")
	public String delete(ElectricFee electricFee, RedirectAttributes redirectAttributes) {
		electricFeeService.delete(electricFee);
		addMessage(redirectAttributes, "删除电费结算成功");
		return "redirect:"+Global.getAdminPath()+"/fee/electricFee/?repage";
	}

	@RequestMapping(value = {"getMeterValue"})
	@ResponseBody
	public String getMeterValue(String rentContractId,String type) {
		return electricFeeService.getMeterValue(rentContractId,type);
	}
	
	@RequestMapping(value = {"getFee"})
	@ResponseBody
	public String getFee(String rentContractId,String type) {
		return electricFeeService.getMeterFee(rentContractId,type);
	}
}