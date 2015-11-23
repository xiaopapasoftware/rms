/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

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
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.service.ContractBookService;

/**
 * 预约看房信息Controller
 * @author huangsc
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/book")
public class ContractBookController extends BaseController {

	@Autowired
	private ContractBookService contractBookService;
	
	@ModelAttribute
	public ContractBook get(@RequestParam(required=false) String id) {
		ContractBook entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractBookService.get(id);
		}
		if (entity == null){
			entity = new ContractBook();
		}
		return entity;
	}
	
	@RequiresPermissions("contract:contractBook:view")
	@RequestMapping(value = {"list", ""})
	public String list(ContractBook contractBook, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractBook> page = contractBookService.findPage(new Page<ContractBook>(request, response), contractBook); 
		model.addAttribute("page", page);
		return "modules/contract/contractBookList";
	}

	@RequiresPermissions("contract:contractBook:view")
	@RequestMapping(value = "form")
	public String form(ContractBook contractBook, Model model) {
		model.addAttribute("contractBook", contractBook);
		return "modules/contract/contractBookForm";
	}

	@RequiresPermissions("contract:contractBook:edit")
	@RequestMapping(value = "save")
	public String save(ContractBook contractBook, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, contractBook)){
			return form(contractBook, model);
		}
		contractBookService.save(contractBook);
		addMessage(redirectAttributes, "保存预约看房信息成功");
		return "redirect:"+Global.getAdminPath()+"/contract/contractBook/?repage";
	}
	
	@RequiresPermissions("contract:contractBook:edit")
	@RequestMapping(value = "delete")
	public String delete(ContractBook contractBook, RedirectAttributes redirectAttributes) {
		contractBookService.delete(contractBook);
		addMessage(redirectAttributes, "删除预约看房信息成功");
		return "redirect:"+Global.getAdminPath()+"/contract/contractBook/?repage";
	}

}