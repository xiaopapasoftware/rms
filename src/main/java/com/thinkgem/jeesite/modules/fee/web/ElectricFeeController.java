/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.fee.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
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
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFeeUseInfo;
import com.thinkgem.jeesite.modules.fee.service.ElectricFeeService;

/**
 * 电费结算Controller
 * 
 * @author huangsc
 * @version 2015-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/fee/electricFee")
public class ElectricFeeController extends BaseController {

    @Autowired
    private ElectricFeeService electricFeeService;

    @Autowired
    private RentContractService rentContractService;

    @ModelAttribute
    public ElectricFee get(@RequestParam(required = false) String id) {
	ElectricFee entity = null;
	if (StringUtils.isNotBlank(id)) {
	    entity = electricFeeService.get(id);
	}
	if (entity == null) {
	    entity = new ElectricFee();
	}
	return entity;
    }

    // @RequiresPermissions("fee:electricFee:view")
    @RequestMapping(value = { "list", "" })
    public String list(ElectricFee electricFee, HttpServletRequest request, HttpServletResponse response, Model model) {
	Page<ElectricFee> page = electricFeeService.findPage(new Page<ElectricFee>(request, response), electricFee);
	model.addAttribute("page", page);
	return "modules/fee/electricFeeList";
    }

    // @RequiresPermissions("fee:electricFee:view")
    @RequestMapping(value = "form")
    public String form(ElectricFee electricFee, Model model) {
	model.addAttribute("electricFee", electricFee);
	return "modules/fee/electricFeeForm";
    }

    @RequestMapping(value = "viewUseInfo")
    public String viewUseInfo(ElectricFeeUseInfo electricFeeUseInfo, Model model) {
	if (StringUtils.isNotEmpty(electricFeeUseInfo.getContractCode())) {
	    RentContract resultRentContract = rentContractService.findContractByCode(electricFeeUseInfo.getContractCode());
	    if (resultRentContract != null) {
		String startDate = electricFeeUseInfo.getStartDate();
		String endDate = electricFeeUseInfo.getEndDate();
		Map<Integer, String> resultMap = electricFeeService.getMeterFee(resultRentContract.getId(), startDate, endDate);
		if (MapUtils.isNotEmpty(resultMap)) {
		    List<ElectricFeeUseInfo> list = new ArrayList<ElectricFeeUseInfo>();
		    ElectricFeeUseInfo ele = new ElectricFeeUseInfo();
		    ele.setContractCode(resultRentContract.getContractCode());
		    ele.setEndDate(endDate);
		    ele.setPersonalPrice("0".equals(resultMap.get(4)) ? "" : resultMap.get(4));
		    if (StringUtils.isNotEmpty(resultMap.get(4)) && StringUtils.isNotEmpty(resultMap.get(1))) {
			double value = Double.valueOf(resultMap.get(4)) * Double.valueOf(resultMap.get(1));
			ele.setPersonalUseAmount(new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).toString());
		    } else {
			ele.setPersonalUseAmount("");
		    }
		    ele.setPersonalUseEle("0".equals(resultMap.get(1)) ? "" : resultMap.get(1));
		    ele.setPublicPrice("0".equals(resultMap.get(5)) ? "" : resultMap.get(5));
		    if (StringUtils.isNotEmpty(resultMap.get(5)) && StringUtils.isNotEmpty(resultMap.get(2))) {
			double value = Double.valueOf(resultMap.get(5)) * Double.valueOf(resultMap.get(2));
			ele.setPublicUseAmount(new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).toString());
		    } else {
			ele.setPublicUseAmount("");
		    }
		    ele.setPublicUseEle("0".equals(resultMap.get(2)) ? "" : resultMap.get(2));
		    ele.setRemainedEle("0".equals(resultMap.get(3)) ? "" : resultMap.get(3));
		    if (StringUtils.isNotEmpty(resultMap.get(3)) && StringUtils.isNotEmpty(resultMap.get(4))) {
			double value = Double.valueOf(resultMap.get(4)) * Double.valueOf(resultMap.get(3));
			ele.setRemainedEleAmount(new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).toString());
		    } else {
			ele.setRemainedEleAmount("");
		    }
		    ele.setStartDate(startDate);
		    ele.setReturnValue(resultMap.get(0));
		    list.add(ele);
		    model.addAttribute("electricFeeUseInfo", electricFeeUseInfo);
		    model.addAttribute("list", list);
		}
	    }
	}
	return "modules/fee/electricFeeUseInfo";
    }

    // @RequiresPermissions("fee:electricFee:edit")
    @RequestMapping(value = "save")
    public String save(ElectricFee electricFee, Model model, RedirectAttributes redirectAttributes) {
	if (!beanValidator(model, electricFee)) {
	    return form(electricFee, model);
	}
	electricFeeService.save(electricFee);
	addMessage(redirectAttributes, "电费充值申请成功提交！请等待审核结果。");
	return "redirect:" + Global.getAdminPath() + "/fee/electricFee/?repage";
    }

    // @RequiresPermissions("fee:electricFee:edit")
    @RequestMapping(value = "delete")
    public String delete(ElectricFee electricFee, RedirectAttributes redirectAttributes) {
	electricFeeService.delete(electricFee);
	addMessage(redirectAttributes, "删除电费结算成功");
	return "redirect:" + Global.getAdminPath() + "/fee/electricFee/?repage";
    }

}