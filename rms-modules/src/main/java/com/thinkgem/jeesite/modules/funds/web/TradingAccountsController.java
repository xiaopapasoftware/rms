package com.thinkgem.jeesite.modules.funds.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.MoneyReceivedTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TenantTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeDirectionEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradingAccountsStatusEnum;
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
import com.thinkgem.jeesite.modules.utils.DictUtils;


/**
 * @author wangshujin
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

  @RequestMapping(value = {"viewReceiptAttachmentFiles"})
  public String viewReceiptAttachmentFiles(String id, Model model) {
    TradingAccounts entity = tradingAccountsService.get(id);
    model.addAttribute("tradingAccounts", entity);
    if (TradeTypeEnum.DEPOSIT_AGREEMENT.getValue().equals(entity.getTradeType())) {
      return "modules/funds/viewReceiptAttachment";
    } else {
      return "modules/funds/viewRentAttachment";
    }
  }

  // @RequiresPermissions("funds:tradingAccounts:view")
  @RequestMapping(value = {"list", ""})
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
    String postpaidFeeId = "";// 后付费交易ID

    // 防止同时开多个浏览器，需对传进的款项id做查询判断
    boolean check = true;
    if (ArrayUtils.isNotEmpty(paymentTransIdArray)) {
      for (String transId : paymentTransIdArray) {
        PaymentTrans pt = paymentTransService.get(transId);
        if (pt == null || (pt != null && pt.getDelFlag().equals(BaseEntity.DEL_FLAG_DELETE))) {
          check = false;
          break;
        }
        if (StringUtils.isNotBlank(pt.getPostpaidFeeId())) {
          postpaidFeeId = pt.getPostpaidFeeId();
        }
      }
    } else {
      addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "请选择款项进行到账！");
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }
    if (!check) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "您选择的款项记录已被修改，请刷新页面，重新勾选进行操作！");
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }

    // 校验 如果账务交易类型是 后付费付款，则必须后付费款项一次性全部到账
    int postpaidfeeTransCount = 0;// 后付费交易原生包含的款项数
    if (TradeTypeEnum.PUB_FEE_POSTPAID.getValue().equals(type)) {
      PaymentTrans temppt = new PaymentTrans();
      temppt.setPostpaidFeeId(postpaidFeeId);
      List<PaymentTrans> postpaidFeeTransList = paymentTransService.findList(temppt);
      if (CollectionUtils.isNotEmpty(postpaidFeeTransList)) {
        postpaidfeeTransCount = postpaidFeeTransList.size();
      }
      if (postpaidfeeTransCount != paymentTransIdArray.length) {
        addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "后付费款项需要一次性全部到账登记，您已遗漏，请重新进行到账登记操作！");
        return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
      }
    }

    // 校验指定款项不能跨月到账，涉及到的款项类型有（房租金额6，水费金额14，燃气金额16，电视费18，宽带费20，服务费22）
    if (!checkPaymentTransSignDateValid(paymentTransIdArray, PaymentTransTypeEnum.RENT_AMOUNT.getValue())) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "您选择到账的房租款项之前的月份有仍未到账的，请依次到账！");
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }
    if (!checkPaymentTransSignDateValid(paymentTransIdArray, PaymentTransTypeEnum.WATER_AMOUNT.getValue())) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "您选择到账的水费款项之前的月份有仍未到账的，请依次到账！");
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }
    if (!checkPaymentTransSignDateValid(paymentTransIdArray, PaymentTransTypeEnum.GAS_AMOUNT.getValue())) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "您选择到账的燃气款项之前的月份有仍未到账的，请依次到账！");
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }
    if (!checkPaymentTransSignDateValid(paymentTransIdArray, PaymentTransTypeEnum.TV_AMOUNT.getValue())) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "您选择到账的电视费款项之前的月份有仍未到账的，请依次到账！");
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }
    if (!checkPaymentTransSignDateValid(paymentTransIdArray, PaymentTransTypeEnum.NET_AMOUNT.getValue())) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "您选择到账的宽带费款项之前的月份有仍未到账的，请依次到账！");
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }
    if (!checkPaymentTransSignDateValid(paymentTransIdArray, PaymentTransTypeEnum.SERVICE_AMOUNT.getValue())) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "您选择到账的服务费款项之前的月份有仍未到账的，请依次到账！");
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }

    if (TradeTypeEnum.LEASE_CONTRACT_TRADE.getValue().equals(type)) {
      tradingAccounts.setTradeStatus(TradingAccountsStatusEnum.AUDIT_PASS.getValue());
      tradingAccounts.setTradeDirection(TradeDirectionEnum.OUT.getValue());
      tradingAccounts.setPayeeType(MoneyReceivedTypeEnum.PERSONAL.getValue());
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
      addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存账务交易成功");
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    } else {
      double amount = 0;// 实际交易金额
      String tradeType = "";// 交易类型
      String tradeObjectId = "";// 交易对象ID
      Map<String, Receipt> paymentTypeMap = new HashMap<String, Receipt>();// key为款项类型，value为收据对象
      String targetReceiptType = "";// 如果交易类型为退租类交易，且最终金额为应收款项且>0时，把应收款项金额最大的款项类型作为最终 收据的款项类型
      List<Receipt> receiptList = new ArrayList<Receipt>();// 渲染到页面上的收据集合

      List<String> containedOutTransList = new ArrayList<String>();// 所有可能包含 出款方向的交易类型集合
      containedOutTransList.add(TradeTypeEnum.LEASE_CONTRACT_TRADE.getValue());
      containedOutTransList.add(TradeTypeEnum.DEPOSIT_TO_BREAK.getValue());
      containedOutTransList.add(TradeTypeEnum.ADVANCE_RETURN_RENT.getValue());
      containedOutTransList.add(TradeTypeEnum.NORMAL_RETURN_RENT.getValue());
      containedOutTransList.add(TradeTypeEnum.OVERDUE_RETURN_RENT.getValue());
      containedOutTransList.add(TradeTypeEnum.SPECIAL_RETURN_RENT.getValue());

      List<String> returnTransList = new ArrayList<String>();// 4种退款交易类型
      returnTransList.add(TradeTypeEnum.ADVANCE_RETURN_RENT.getValue());
      returnTransList.add(TradeTypeEnum.NORMAL_RETURN_RENT.getValue());
      returnTransList.add(TradeTypeEnum.OVERDUE_RETURN_RENT.getValue());
      returnTransList.add(TradeTypeEnum.SPECIAL_RETURN_RENT.getValue());

      for (int i = 0; i < paymentTransIdArray.length; i++) {
        PaymentTrans paymentTrans = paymentTransService.get(paymentTransIdArray[i]);
        if (TradeDirectionEnum.OUT.getValue().equals(paymentTrans.getTradeDirection())) {
          amount -= paymentTrans.getLastAmount();
        } else {// 应收
          amount += paymentTrans.getLastAmount();
        }
        tradeType = paymentTrans.getTradeType();// 交易类型
        tradeObjectId = paymentTrans.getTransId();// 交易对象ID
        String paymentType = paymentTrans.getPaymentType();// 款项类型

        if (!containedOutTransList.contains(tradeType)) { // 交易类型里的款项全是收款，不包含出款
          if (TradeDirectionEnum.IN.getValue().equals(paymentTrans.getTradeDirection())) {
            Receipt receipt = null;
            if (paymentTypeMap.containsKey(paymentType)) {
              receipt = paymentTypeMap.get(paymentType);
              receipt.setReceiptAmount(receipt.getReceiptAmount() + paymentTrans.getLastAmount());
            } else {
              receipt = new Receipt();
              receipt.setReceiptAmount(paymentTrans.getLastAmount());
              receipt.setPaymentType(paymentType);
            }
            paymentTypeMap.put(paymentType, receipt);
          }
        } else {// 为了防止交易类型为退租类交易，最终金额为应收款项且>0时，把应收款项金额最大的款项类型作为最终 收据的款项类型
          if (returnTransList.contains(tradeType)) {
            List<PaymentTrans> inDirectPaymentTrans = new ArrayList<PaymentTrans>();
            if (TradeDirectionEnum.IN.getValue().equals(paymentTrans.getTradeDirection())) {
              inDirectPaymentTrans.add(paymentTrans);
            }
            if (CollectionUtils.isNotEmpty(inDirectPaymentTrans)) {
              Collections.sort(inDirectPaymentTrans, new Comparator<PaymentTrans>() {
                @Override
                public int compare(PaymentTrans o1, PaymentTrans o2) {
                  return o1.getLastAmount().compareTo(o2.getLastAmount());
                }
              });
              targetReceiptType = inDirectPaymentTrans.get(inDirectPaymentTrans.size() - 1).getPaymentType();
            }
          }
        }
      }
      if (TradeTypeEnum.LEASE_CONTRACT_TRADE.getValue().equals(tradeType) || TradeTypeEnum.DEPOSIT_TO_BREAK.getValue().equals(tradeType)) {
        // DONOTHING
      } else if (TradeTypeEnum.ADVANCE_RETURN_RENT.getValue().equals(tradeType) || TradeTypeEnum.NORMAL_RETURN_RENT.getValue().equals(tradeType)
          || TradeTypeEnum.OVERDUE_RETURN_RENT.getValue().equals(tradeType) || TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {
        if (amount > 0) {// 退租类交易类型，但是最终款项为应收款
          Receipt receipt = new Receipt();
          receipt.setReceiptAmount(new BigDecimal(amount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
          receipt.setPaymentType(targetReceiptType);
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
            String tenantType = tenants.get(0).getTenantType();
            if (TenantTypeEnum.PERSONAL.getValue().equals(tenantType)) {
              tradingAccounts.setPayeeType(MoneyReceivedTypeEnum.PERSONAL.getValue());
            }
            if (TenantTypeEnum.AGENCY.getValue().equals(tenantType)) {
              tradingAccounts.setPayeeType(MoneyReceivedTypeEnum.AGENCY.getValue());
            }
          }
        } else {
          RentContract rc = rentContractService.get(tradeObjectId);
          if (rc != null) {// 出租合同承租人
            List<Tenant> tenants = rentContractService.findTenant(rc);
            if (CollectionUtils.isNotEmpty(tenants)) {
              tradingAccounts.setPayeeName(tenants.get(0).getTenantName());
              String tenantType = tenants.get(0).getTenantType();
              if (TenantTypeEnum.PERSONAL.getValue().equals(tenantType)) {
                tradingAccounts.setPayeeType(MoneyReceivedTypeEnum.PERSONAL.getValue());
              }
              if (TenantTypeEnum.AGENCY.getValue().equals(tenantType)) {
                tradingAccounts.setPayeeType(MoneyReceivedTypeEnum.AGENCY.getValue());
              }
            }
          }
        }
      }
      tradingAccounts.setTradeDirection(amount > 0 ? TradeDirectionEnum.IN.getValue() : TradeDirectionEnum.OUT.getValue());
      tradingAccounts.setTradeAmount(new BigDecimal(Math.abs(amount)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
      tradingAccounts.setTradeDirectionDesc(DictUtils.getDictLabel(tradingAccounts.getTradeDirection(), "trans_dirction", ""));
      tradingAccounts.setTradeType(tradeType);
      tradingAccounts.setTradeTypeDesc(DictUtils.getDictLabel(tradingAccounts.getTradeType(), "trans_type", ""));
      model.addAttribute("tradingAccounts", tradingAccounts);
      tradingAccounts.setReceiptList(receiptList);
      return "modules/funds/tradingAccountsForm";
    }
  }


  @SuppressWarnings("unchecked")
  private boolean checkPaymentTransSignDateValid(String[] paymentTransIdArray, String paymentTransType) {
    String rentContractId = "";
    List<PaymentTrans> tempTrans = new ArrayList<PaymentTrans>();// 选中的指定款项类型的款项集合
    for (String transId : paymentTransIdArray) {
      PaymentTrans pt = paymentTransService.get(transId);
      String tradeType = pt.getTradeType();
      if (paymentTransType.equals(pt.getPaymentType()) && (TradeTypeEnum.SIGN_NEW_CONTRACT.getValue().equals(tradeType) || TradeTypeEnum.NORMAL_RENEW.getValue().equals(tradeType)
          || TradeTypeEnum.OVERDUE_AUTO_RENEW.getValue().equals(tradeType))) {
        tempTrans.add(pt);
        rentContractId = pt.getTransId();
      }
    }
    List<PaymentTrans> untradedTransList = null;// 合同下所有未到账的指定款项类型的款项集合
    if (CollectionUtils.isNotEmpty(tempTrans)) {
      untradedTransList = paymentTransService.getPaymentTransByTypeAndStatus(paymentTransType, rentContractId, PaymentTransStatusEnum.NO_SIGN.getValue());
    }
    // 先从所有未到账的指定款项类型列表里过滤掉 选择的需到账登记的款项列表，
    List<PaymentTrans> substractedList = null;
    if (CollectionUtils.isNotEmpty(untradedTransList) && CollectionUtils.isNotEmpty(tempTrans)) {
      substractedList = (List<PaymentTrans>) CollectionUtils.subtract(untradedTransList, tempTrans);
      // 再把过滤出来的款项列表中的开始时间最早的一个a取出，再把用户选择的款项列表里开始时间最早的一个b取出来。如果a<b，则报错提醒用户还有更早的款项未到账。
      if (CollectionUtils.isNotEmpty(substractedList)) {
        Collections.sort(substractedList);
        Collections.sort(tempTrans);
        Date substractedPT = substractedList.get(0).getStartDate();
        Date choosedDate = tempTrans.get(0).getStartDate();
        if (substractedPT.before(choosedDate)) {
          return false;
        }
      }
    }
    return true;
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
  public String audit(AuditHis auditHis, RedirectAttributes redirectAttributes) {
    tradingAccountsService.audit(auditHis);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "账务交易审核完成！");
    return "redirect:" + Global.getAdminPath() + "/funds/tradingAccounts/?repage";
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
        tmpReceipt.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
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
        if (!check) break;
        receiptNoList.add(receipt.getReceiptNo());
      }
    }
    if (!check) {
      addMessage(model, ViewMessageTypeEnum.ERROR, "收据编号:" + receiptNo + "重复或已存在.");
      if (StringUtils.isEmpty(id)) {
        return form(tradingAccounts, model, redirectAttributes);
      } else {
        return "modules/funds/tradingAccountsForm";
      }
    }
    // 检查款项的状态是否正常,防止因为别的操作导致款项被删了。。不能继续进行到账登记
    boolean check2 = true;
    if (StringUtils.isNotEmpty(tradingAccounts.getTransIds())) {
      String[] transIds = tradingAccounts.getTransIds().split(",");
      if (ArrayUtils.isNotEmpty(transIds)) {
        for (String transId : transIds) {
          PaymentTrans pt = paymentTransService.get(transId);
          if (pt == null || (pt != null && pt.getDelFlag().equals(BaseEntity.DEL_FLAG_DELETE))) {
            check2 = false;
            break;
          }
        }
        if (!check2) {
          addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "您选择的款项记录已被修改，请重新进行款项到账登记！");
          return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
        }
      } else {
        addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "您还未选择款项记录！");
        return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
      }
    } else {
      addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "您还未选择款项记录！");
      return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
    }
    tradingAccounts.setTradeStatus(TradingAccountsStatusEnum.TO_AUDIT.getValue());
    tradingAccountsService.save(tradingAccounts);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存账务交易成功");
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
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除账务交易成功");
    return "redirect:" + Global.getAdminPath() + "/funds/tradingAccounts/?repage";
  }

}
