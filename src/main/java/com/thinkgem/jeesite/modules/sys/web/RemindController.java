package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.LeaseContractService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;

@Controller
@RequestMapping(value = "${adminPath}/sys/remind")
public class RemindController extends BaseController {
	@Autowired
	private PaymentTransService paymentTransService;
	@Autowired
	private LeaseContractService leaseContractService;
	@Autowired
	private RentContractService rentContractService;
	
	//@RequiresPermissions("sys:remind:rental")
	@RequestMapping(value = {"rentalList", ""})
	public String rentalList(PaymentTrans paymentTrans, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PaymentTrans> page = paymentTransService.findRemind(new Page<PaymentTrans>(request, response), paymentTrans); 
		model.addAttribute("page", page);
		return "modules/sys/rentalRemind";
	}
	
	//@RequiresPermissions("sys:remind:leaseContract")
	@RequestMapping(value = {"leaseContractList", ""})
	public String leaseContractList(LeaseContract leaseContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<LeaseContract> page = leaseContractService.findLeaseContractList(new Page<LeaseContract>(request, response), leaseContract); 
		model.addAttribute("page", page);
		return "modules/sys/leaseContract";
	}
	
	//@RequiresPermissions("sys:remind:renewContract")
	@RequestMapping(value = {"renewContract", ""})
	public String renewContract(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RentContract> page = rentContractService.findContractList(new Page<RentContract>(request, response), rentContract); 
		model.addAttribute("page", page);
		return "modules/sys/rentContractList";
	}
	
	@RequestMapping(value = {"remind"})
    @ResponseBody
    public String remind(HttpServletRequest request, HttpServletResponse response) {
		String json = "{";
		Page<PaymentTrans> page = paymentTransService.findRemind(new Page<PaymentTrans>(request, response,-1), new PaymentTrans()); 
		json += "\"paymentTrans\":\""+page.getList().size()+"\",";
		Page<LeaseContract> pageLeaseContract = leaseContractService.findLeaseContractList(new Page<LeaseContract>(request, response,-1), new LeaseContract()); 
		json += "\"leaseContract\":\""+pageLeaseContract.getList().size()+"\",";
		Page<RentContract> pageRentContract = rentContractService.findContractList(new Page<RentContract>(request, response,-1), new RentContract()); 
		json += "\"rentContract\":\""+pageRentContract.getList().size()+"\"";
		json += "}";
		return json;
    }
}
