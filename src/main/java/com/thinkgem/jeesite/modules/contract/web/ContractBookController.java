/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

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
import com.thinkgem.jeesite.modules.app.entity.Message;
import com.thinkgem.jeesite.modules.app.service.MessageService;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.service.ContractBookService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 预约看房信息Controller
 * @author huangsc
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/book")
public class ContractBookController extends BaseController {

	@Autowired
	private ContractBookService contractBookService;
	
	@Autowired
	private MessageService messageService;//APP消息推送
	
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
	
	@RequestMapping(value = {"list", ""})
	public String list(ContractBook contractBook, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractBook> page = contractBookService.findPage(new Page<ContractBook>(request, response), contractBook); 
		model.addAttribute("page", page);
		return "modules/contract/contractBookList";
	}

	@RequestMapping(value = "form")
	public String form(ContractBook contractBook, Model model) {
		model.addAttribute("contractBook", contractBook);
		return "modules/contract/contractBookForm";
	}

	@RequestMapping(value = "save")
	public String save(ContractBook contractBook, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, contractBook)){
			return form(contractBook, model);
		}
		contractBook.setSalesId(UserUtils.getUser().getId());
		contractBookService.save(contractBook);
		addMessage(redirectAttributes, "保存预约看房信息成功");
		return "redirect:"+Global.getAdminPath()+"/contract/book/?repage";
	}
	
	@RequestMapping(value = "confirm")
	public String confirm(ContractBook contractBook, RedirectAttributes redirectAttributes) {
		contractBook.setDelFlag("0");
		contractBook = contractBookService.get(contractBook);
		contractBook.setBookStatus("1");//预约成功
		contractBook.setSalesId(UserUtils.getUser().getId());
		contractBookService.save(contractBook);
		addMessage(redirectAttributes, "确认预约信息成功");
		Message message = new Message();
		message.setContent("您的预约申请已被确认,请按约定日期联系管家看房!");
		message.setTitle("预约提醒");
		message.setType("预约提醒");
		message.setReceiver(contractBook.getUserPhone());
		messageService.addMessage(message, true);
		return "redirect:"+Global.getAdminPath()+"/contract/book/?repage";
	}

	@RequestMapping(value = "cancel")
	public String cancel(ContractBook contractBook, RedirectAttributes redirectAttributes) {
		contractBook.setDelFlag("0");
		contractBook = contractBookService.get(contractBook);
		contractBook.setBookStatus("3");//管家取消预约
		contractBook.setSalesId(UserUtils.getUser().getId());
		contractBookService.save(contractBook);
		addMessage(redirectAttributes, "取消预约信息成功");
		Message message = new Message();
		message.setContent("您的预约申请已被管家取消,请联系管家!");
		message.setTitle("预约提醒");
		message.setType("预约提醒");
		message.setReceiver(contractBook.getUserPhone());
		messageService.addMessage(message, true);
		return "redirect:"+Global.getAdminPath()+"/contract/book/?repage";
	}
}