/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
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
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.DepositAgreementService;
import com.thinkgem.jeesite.modules.contract.service.LeaseContractService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.funds.service.ReceiptService;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 账务交易Controller
 * 
 * @author huangsc
 * @version 2015-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/funds/tradingAccounts")
public class TradingAccountsController extends BaseController {

    @Autowired
    private TradingAccountsService tradingAccountsService;
    @Autowired
    private PaymentTransService paymentTransService;
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private DepositAgreementService depositAgreementService;
    @Autowired
    private RentContractService rentContractService;
    @Autowired
    private LeaseContractService leaseContractService;

    @ModelAttribute
    public TradingAccounts get(@RequestParam(required = false) String id) {
	TradingAccounts entity = null;
	if (StringUtils.isNotBlank(id)) {
	    entity = tradingAccountsService.get(id);
	}
	if (entity == null) {
	    entity = new TradingAccounts();
	}
	return entity;
    }

    @RequestMapping(value = { "viewReceiptAttachmentFiles" })
    public String viewReceiptAttachmentFiles(String id, Model model) {
	TradingAccounts entity = tradingAccountsService.get(id);
	model.addAttribute("tradingAccounts", entity);
	if ("1".equals(entity.getTradeType())) {// 预约定金
	    return "modules/funds/viewReceiptAttachment";
	} else {
	    return "modules/funds/viewRentAttachment";
	}
    }

    // @RequiresPermissions("funds:tradingAccounts:view")
    @RequestMapping(value = { "list", "" })
    public String list(TradingAccounts tradingAccounts, HttpServletRequest request, HttpServletResponse response, Model model) {
	Page<TradingAccounts> page = tradingAccountsService.findPage(new Page<TradingAccounts>(request, response), tradingAccounts);
	model.addAttribute("page", page);
	return "modules/funds/tradingAccountsList";
    }

    // @RequiresPermissions("funds:tradingAccounts:view")
    @RequestMapping(value = "form")
    public String form(TradingAccounts tradingAccounts, Model model, RedirectAttributes redirectAttributes) {
	String type = tradingAccounts.getTradeType();
	String[] paymentTransIdArray = tradingAccounts.getTransIds().split(",");

	boolean check = true;
	// 防止同时开多个浏览器，需对传进的款项id做查询判断
	if (ArrayUtils.isNotEmpty(paymentTransIdArray)) {
	    for (String transId : paymentTransIdArray) {
		PaymentTrans pt = paymentTransService.get(transId);
		if (pt == null || (pt != null && pt.getDelFlag().equals("1"))) {
		    check = false;
		    break;
		}
	    }
	}
	if (!check) {
	    addMessage(redirectAttributes, "您选择的款项记录已被修改，请刷新页面，重新勾选进行操作！");
	    return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
	}
	if ("0".equals(type)) {// 承租合同直接处理，不跳转
	    tradingAccounts.setTradeStatus("1");// 审核通过
	    tradingAccounts.setTradeDirection("0");// 出账
	    tradingAccounts.setPayeeType("1");// 交易人类型为“个人”
	    for (int i = 0; i < paymentTransIdArray.length; i++) {
		PaymentTrans paymentTrans = paymentTransService.get(paymentTransIdArray[i]);
		tradingAccounts.setId(null);
		tradingAccounts.setTradeAmount(paymentTrans.getTradeAmount() - paymentTrans.getTransAmount());
		tradingAccounts.setTransIds(paymentTransIdArray[i]);
		tradingAccounts.setPayeeName(leaseContractService.get(paymentTrans.getTransId()).getRemittancerName());
		if (tradingAccounts.getTradeAmount() > 0) {
		    tradingAccountsService.save(tradingAccounts);
		}
	    }
	    addMessage(redirectAttributes, "保存账务交易成功");
	    return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
	} else {
	    double amount = 0;// 实际交易金额
	    String tradeType = "";// 交易类型
	    String tradeObjectId = "";// 交易对象ID
	    Map<String, Receipt> paymentTypeMap = new HashMap<String, Receipt>();
	    for (int i = 0; i < paymentTransIdArray.length; i++) {
		PaymentTrans paymentTrans = paymentTransService.get(paymentTransIdArray[i]);
		if ("0".equals(paymentTrans.getTradeDirection())) {// 应出
		    amount -= paymentTrans.getLastAmount();
		} else {// 应收
		    amount += paymentTrans.getLastAmount();
		}
		tradeType = paymentTrans.getTradeType();// 交易类型
		tradeObjectId = paymentTrans.getTransId();// 交易对象ID
		String paymentType = paymentTrans.getPaymentType();// 款项类型

		// 包含有出款的交易类型:0=承租合同,2=定金转违约,6=提前退租,7=正常退租,8=逾期退租,9=特殊退租
		if ("0".equals(tradeType) || "2".equals(tradeType)) {// 0=承租合同,2=定金转违约不开收据
		} else if ("6".equals(tradeType) || "7".equals(tradeType) || "8".equals(tradeType) || "9".equals(tradeType)) {
		} else {// 交易类型里的款项全是收款，不包含出款
		    if (!"0".equals(paymentTrans.getTradeDirection())) {// 应收款项
			Receipt receipt = new Receipt();
			if (paymentTypeMap.containsKey(paymentType)) {
			    receipt = paymentTypeMap.get(paymentType);
			}
			receipt.setReceiptAmount((null == receipt.getReceiptAmount() ? 0d : receipt.getReceiptAmount()) + paymentTrans.getLastAmount());
			receipt.setPaymentType(paymentType);
			paymentTypeMap.put(paymentType, receipt);
		    }
		}
	    }
	    List<Receipt> receiptList = new ArrayList<Receipt>(); /* 收据 */
	    if ("0".equals(tradeType) || "2".equals(tradeType)) {// 0=承租合同,2=定金转违约不开收据
	    } else if ("6".equals(tradeType) || "7".equals(tradeType) || "8".equals(tradeType) || "9".equals(tradeType)) {// 6=提前退租,7=正常退租,8=逾期退租,9=特殊退租
		if (amount > 0) {// 总到账金额大于0
		    Receipt receipt = new Receipt();
		    receipt.setReceiptAmount(new BigDecimal(amount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
		    receiptList.add(receipt);
		}
	    } else {
		for (String key : paymentTypeMap.keySet()) {
		    Receipt receipt = paymentTypeMap.get(key);
		    receiptList.add(receipt);
		}
	    }
	    // 获取交易对象名称,设置交易对象名称、交易对象类型
	    if (StringUtils.isNotEmpty(tradeObjectId)) {
		DepositAgreement da = depositAgreementService.get(tradeObjectId);
		if (da != null) {// 定金协议承租人
		    List<Tenant> tenants = depositAgreementService.findTenant(da);// 定金协议的承租人列表
		    if (CollectionUtils.isNotEmpty(tenants)) {
			tradingAccounts.setPayeeName(tenants.get(0).getTenantName());
			String tenantType = tenants.get(0).getTenantType();// 租客类型
			if ("0".equals(tenantType)) {// 个人租客
			    tradingAccounts.setPayeeType("1");// 交易人类型为“个人”
			}
			if ("1".equals(tenantType)) {// 企业租客
			    tradingAccounts.setPayeeType("0");// 交易人类型为“单位”
			}
		    }
		} else {
		    RentContract rc = rentContractService.get(tradeObjectId);
		    if (rc != null) {// 出租合同承租人
			List<Tenant> tenants = rentContractService.findTenant(rc);
			if (CollectionUtils.isNotEmpty(tenants)) {
			    tradingAccounts.setPayeeName(tenants.get(0).getTenantName());
			    String tenantType = tenants.get(0).getTenantType();// 租客类型
			    if ("0".equals(tenantType)) {// 个人租客
				tradingAccounts.setPayeeType("1");// 交易人类型为“个人”
			    }
			    if ("1".equals(tenantType)) {// 企业租客
				tradingAccounts.setPayeeType("0");// 交易人类型为“单位”
			    }
			}
		    }
		}
	    }
	    tradingAccounts.setTradeDirection(amount > 0 ? "1" : "0");
	    tradingAccounts.setTradeAmount(new BigDecimal(Math.abs(amount)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
	    tradingAccounts.setTradeDirectionDesc(DictUtils.getDictLabel(tradingAccounts.getTradeDirection(), "trans_dirction", ""));
	    tradingAccounts.setTradeType(tradeType);
	    tradingAccounts.setTradeTypeDesc(DictUtils.getDictLabel(tradingAccounts.getTradeType(), "trans_type", ""));
	    model.addAttribute("tradingAccounts", tradingAccounts);
	    tradingAccounts.setReceiptList(receiptList);
	    return "modules/funds/tradingAccountsForm";
	}
    }

    @RequestMapping(value = "edit")
    public String edit(TradingAccounts tradingAccounts, Model model) {
	tradingAccounts = tradingAccountsService.get(tradingAccounts.getId());
	List<Receipt> receiptList = new ArrayList<Receipt>();
	receiptList = tradingAccountsService.findReceiptList(tradingAccounts);
	tradingAccounts.setReceiptList(receiptList);
	model.addAttribute("tradingAccounts", tradingAccounts);
	return "modules/funds/tradingAccountsEdit";
    }

    @RequestMapping(value = "revoke")
    public String revoke(String id) {
	tradingAccountsService.remoke(id);
	return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }

    @RequestMapping(value = "findOne")
    public String findOne(TradingAccounts tradingAccounts, Model model) {
	tradingAccounts = tradingAccountsService.get(tradingAccounts);
	model.addAttribute("tradingAccounts", tradingAccounts);
	return "modules/funds/tradingAccountsEdit";
    }

    @RequestMapping(value = "audit")
    public String audit(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
	tradingAccountsService.audit(auditHis);
	return list(new TradingAccounts(), request, response, model);
    }

    // @RequiresPermissions("funds:tradingAccounts:edit")
    @RequestMapping(value = "save")
    public String save(TradingAccounts tradingAccounts, Model model, RedirectAttributes redirectAttributes) {
	String id = tradingAccounts.getId();

	if (!beanValidator(model, tradingAccounts)) {
	    return form(tradingAccounts, model, redirectAttributes);
	}

	/* 校验收据编号重复 */
	boolean check = true;
	String receiptNo = "";
	List<String> receiptNoList = new ArrayList<String>();
	if (null != tradingAccounts.getReceiptList()) {
	    for (Receipt receipt : tradingAccounts.getReceiptList()) {
		if (StringUtils.isBlank(receipt.getReceiptNo())) {
		    continue;
		}
		Receipt tmpReceipt = new Receipt();
		tmpReceipt.setReceiptNo(receipt.getReceiptNo());
		tmpReceipt.setDelFlag("0");
		List<Receipt> list = receiptService.findList(tmpReceipt);
		if ((null != list && list.size() > 0)) {
		    for (Receipt tReceipt : list) {
			if (receipt.getReceiptNo().equals(tReceipt.getReceiptNo()) && !tReceipt.getTradingAccounts().getId().equals(tradingAccounts.getId())) {
			    receiptNo = receipt.getReceiptNo();
			    check = false;
			    break;
			}
		    }
		}
		if (receiptNoList.contains(receipt.getReceiptNo())) {
		    receiptNo = receipt.getReceiptNo();
		    check = false;
		}
		if (!check)
		    break;
		receiptNoList.add(receipt.getReceiptNo());
	    }
	}

	if (!check) {
	    model.addAttribute("message", "收据编号:" + receiptNo + "重复或已存在.");
	    model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
	    if (StringUtils.isEmpty(id)) {
		return form(tradingAccounts, model, redirectAttributes);
	    } else {
		return "modules/funds/tradingAccountsForm";
	    }
	}
	tradingAccounts.setTradeStatus("0");// 待审核
	tradingAccountsService.save(tradingAccounts);
	addMessage(redirectAttributes, "保存账务交易成功");
	if (StringUtils.isEmpty(id)) {
	    return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
	} else {
	    return "redirect:" + Global.getAdminPath() + "/funds/tradingAccounts/?repage";
	}
    }

    // @RequiresPermissions("funds:tradingAccounts:edit")
    @RequestMapping(value = "delete")
    public String delete(TradingAccounts tradingAccounts, RedirectAttributes redirectAttributes) {
	tradingAccountsService.delete(tradingAccounts);
	addMessage(redirectAttributes, "删除账务交易成功");
	return "redirect:" + Global.getAdminPath() + "/funds/tradingAccounts/?repage";
    }

}