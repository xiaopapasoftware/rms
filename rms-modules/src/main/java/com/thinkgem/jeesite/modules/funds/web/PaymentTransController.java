/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.funds.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.utils.DictUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans4Export;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;

/**
 * 款项交易Controller
 * 
 * @author huangsc
 * @version 2015-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/funds/paymentTrans")
public class PaymentTransController extends BaseController {

  @Autowired
  private PaymentTransService paymentTransService;

  @ModelAttribute
  public PaymentTrans get(@RequestParam(required = false) String id) {
    PaymentTrans entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = paymentTransService.get(id);
    }
    if (entity == null) {
      entity = new PaymentTrans();
    }
    return entity;
  }

  // @RequiresPermissions("funds:paymentTrans:view")
  @SuppressWarnings({"rawtypes", "unchecked"})
  @RequestMapping(value = {"list", ""})
  public String list(PaymentTrans paymentTrans, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page pageParam = new Page<PaymentTrans>(request, response);
    pageParam.setPageSize(250);
    Page<PaymentTrans> page = paymentTransService.findPage(pageParam, paymentTrans);
    model.addAttribute("page", page);
    return "modules/funds/paymentTransList";
  }

  // @RequiresPermissions("funds:paymentTrans:view")
  @RequestMapping(value = "form")
  public String form(PaymentTrans paymentTrans, Model model) {
    model.addAttribute("paymentTrans", paymentTrans);
    return "modules/funds/paymentTransForm";
  }

  // @RequiresPermissions("funds:paymentTrans:edit")
  @RequestMapping(value = "save")
  public String save(PaymentTrans paymentTrans, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, paymentTrans)) {
      return form(paymentTrans, model);
    }
    paymentTransService.save(paymentTrans);
    addMessage(redirectAttributes, "保存款项交易成功");
    return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
  }

  // @RequiresPermissions("funds:paymentTrans:edit")
  @RequestMapping(value = "delete")
  public String delete(PaymentTrans paymentTrans, RedirectAttributes redirectAttributes) {
    paymentTransService.delete(paymentTrans);
    addMessage(redirectAttributes, "删除款项交易成功");
    return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
  }

  /**
   * 导出款项数据
   */
  @RequestMapping(value = "exportPaymentTrans", method = RequestMethod.POST)
  public String exportPaymentTrans(PaymentTrans paymentTrans, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
    try {
      String fileName = "款项交易数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<PaymentTrans> page = paymentTransService.findPage(new Page<PaymentTrans>(request, response, -1), paymentTrans);
      if (CollectionUtils.isNotEmpty(page.getList())) {
        for (PaymentTrans pt : page.getList()) {
          pt.setPaymentType(DictUtils.getDictLabel(pt.getPaymentType(), "payment_type", pt.getPaymentType()));
          pt.setTradeType(DictUtils.getDictLabel(pt.getTradeType(), "trans_type", pt.getTradeType()));
          pt.setTransStatus(DictUtils.getDictLabel(pt.getTransStatus(), "trade_status", pt.getTransStatus()));
          pt.setTradeDirection(DictUtils.getDictLabel(pt.getTradeDirection(), "trans_dirction", pt.getTradeDirection()));
          pt.setTradeAmount4Export(pt.getTradeAmount() == null ? "0" : pt.getTradeAmount().toString());
          pt.setTransAmount4Export(pt.getTransAmount() == null ? "0" : pt.getTransAmount().toString());
          pt.setLastAmount4Export(pt.getLastAmount() == null ? "0" : pt.getLastAmount().toString());
        }
      }
      new ExportExcel("款项交易数据", PaymentTrans4Export.class).setDataList(page.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }
  }

}
