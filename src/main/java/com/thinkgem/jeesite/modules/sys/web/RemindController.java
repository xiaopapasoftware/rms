package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;

@Controller
@RequestMapping(value = "${adminPath}/sys/remind")
public class RemindController extends BaseController {
	@Autowired
	private PaymentTransService paymentTransService;
	
	@RequiresPermissions("sys:remind:rental")
	@RequestMapping(value = {"rentalList", ""})
	public String list(PaymentTrans paymentTrans, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PaymentTrans> page = paymentTransService.findRemind(new Page<PaymentTrans>(request, response), paymentTrans); 
		model.addAttribute("page", page);
		return "modules/sys/rentalRemind";
	}
}
