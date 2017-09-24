/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.web;

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
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.service.ReceiptService;
import com.thinkgem.jeesite.modules.utils.DictUtils;

/**
 * 账务收据
 */
@Controller
@RequestMapping(value = "${adminPath}/funds/receipt")
public class ReceiptController extends BaseController {

  @Autowired
  private ReceiptService receiptService;

  @ModelAttribute
  public Receipt get(@RequestParam(required = false) String id) {
    Receipt entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = receiptService.get(id);
    }
    if (entity == null) {
      entity = new Receipt();
    }
    return entity;
  }

  @RequiresPermissions("funds:receipt:view")
  @RequestMapping(value = {""})
  public String listNoQuery(Receipt receipt, HttpServletRequest request, HttpServletResponse response, Model model) {
    return "modules/funds/receiptList";
  }

  @RequiresPermissions("funds:receipt:view")
  @RequestMapping(value = {"list"})
  public String listQuery(Receipt receipt, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<Receipt> page = receiptService.findPage(new Page<Receipt>(request, response), receipt);
    model.addAttribute("page", page);
    return "modules/funds/receiptList";
  }

  @RequiresPermissions("funds:tradingAccounts:edit")
  @RequestMapping(value = {"viewReceipt"})
  public String viewReceipt(Receipt receipt, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<Receipt> page = receiptService.findPage(new Page<Receipt>(request, response), receipt);
    model.addAttribute("page", page);
    return "modules/funds/viewReceipt";
  }

  @RequiresPermissions("funds:receipt:view")
  @RequestMapping(value = "form")
  public String form(Receipt receipt, Model model) {
    receipt.setTradeTypeDesc(DictUtils.getDictLabel(receipt.getTradeType(), "trans_type", ""));
    receipt.setPaymentTypeDesc(DictUtils.getDictLabel(receipt.getPaymentType(), "payment_type", ""));
    model.addAttribute("receipt", receipt);
    return "modules/funds/receiptForm";
  }

  @RequiresPermissions("funds:receipt:edit")
  @RequestMapping(value = "save")
  public String save(Receipt receipt, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, receipt)) {
      return form(receipt, model);
    }
    String oriReceiptNo = receiptService.get(receipt.getId()).getReceiptNo();
    String newReceiptNo = receipt.getReceiptNo();
    if (!oriReceiptNo.equals(newReceiptNo) && receiptService.checkReceiptNoIsRepeat(newReceiptNo)) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "收据号码已经存在，修改失败!");
    } else {
      receiptService.save(receipt);
      addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "操作成功!");
    }
    return "redirect:" + Global.getAdminPath() + "/funds/receipt/?repage";
  }

  @RequiresPermissions("funds:receipt:edit")
  @RequestMapping(value = "delete")
  public String delete(Receipt receipt, RedirectAttributes redirectAttributes) {
    receiptService.delete(receipt);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除账务收据成功");
    return "redirect:" + Global.getAdminPath() + "/funds/receipt/?repage";
  }

}
