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
import com.thinkgem.jeesite.modules.app.entity.Message;
import com.thinkgem.jeesite.modules.app.service.MessageService;

/**
 * 消息Controller
 * @author huangsc
 * @version 2015-12-14
 */
@Controller
@RequestMapping(value = "${adminPath}/app/message")
public class MessageController extends BaseController {

	@Autowired
	private MessageService messageService;
	
	@ModelAttribute
	public Message get(@RequestParam(required=false) String id) {
		Message entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = messageService.get(id);
		}
		if (entity == null){
			entity = new Message();
		}
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String list(Message message, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Message> page = messageService.findPage(new Page<Message>(request, response), message); 
		model.addAttribute("page", page);
		return "modules/app/messageList";
	}

	@RequestMapping(value = "form")
	public String form(Message message, Model model) {
		model.addAttribute("message", message);
		return "modules/app/messageForm";
	}

	@RequestMapping(value = "save")
	public String save(Message message, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, message)){
			return form(message, model);
		}
		messageService.save(message);
		addMessage(redirectAttributes, "保存消息成功");
		return "redirect:"+Global.getAdminPath()+"/app/message/?repage";
	}
	
	@RequestMapping(value = "delete")
	public String delete(Message message, RedirectAttributes redirectAttributes) {
		messageService.delete(message);
		addMessage(redirectAttributes, "删除消息成功");
		return "redirect:"+Global.getAdminPath()+"/app/message/?repage";
	}

}