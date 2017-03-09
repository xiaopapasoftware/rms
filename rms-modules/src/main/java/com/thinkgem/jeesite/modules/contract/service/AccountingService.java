/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.dao.AccountingDao;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.enums.AccountingTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;

/**
 * 退租核算Service
 */
@Service
@Transactional(readOnly = true)
public class AccountingService extends CrudService<AccountingDao, Accounting> {

  @Autowired
  private PaymentTransService paymentTransService;

  @Transactional(readOnly = false)
  public int delByRent(Accounting accounting) {
    return dao.delByRent(accounting);
  }

  /**
   * 后台直接修改退租核算记录ACCOUNTING,同时对应的修改款项.只允许修改核算记录的金额，核算类型及方向都不可以修改
   */
  @Transactional(readOnly = false)
  public void updateAccountingAndPaymentTrans(Accounting accounting) {
    super.save(accounting);
    String accountingType = accounting.getAccountingType();
    String tradeType = "";
    if (AccountingTypeEnum.ADVANCE_RETURN_ACCOUNT.getValue().equals(accountingType)) {
      tradeType = TradeTypeEnum.ADVANCE_RETURN_RENT.getValue();
    } else if (AccountingTypeEnum.NORMAL_RETURN_ACCOUNT.getValue().equals(accountingType)) {
      tradeType = TradeTypeEnum.NORMAL_RETURN_RENT.getValue();
    } else if (AccountingTypeEnum.LATE_RETURN_ACCOUNT.getValue().equals(accountingType)) {
      tradeType = TradeTypeEnum.OVERDUE_RETURN_RENT.getValue();
    } else {
      tradeType = TradeTypeEnum.SPECIAL_RETURN_RENT.getValue();
    }
    PaymentTrans paymentTrans = new PaymentTrans();
    paymentTrans.setTransId(accounting.getRentContract().getId());
    paymentTrans.setTradeType(tradeType);
    paymentTrans.setPaymentType(accounting.getFeeType());
    paymentTrans.setTradeDirection(accounting.getFeeDirection());
    List<PaymentTrans> trans = paymentTransService.findList(paymentTrans);
    if (CollectionUtils.isNotEmpty(trans)) {
      PaymentTrans pt = trans.get(0);
      if (PaymentTransStatusEnum.NO_SIGN.getValue().equals(pt.getTransStatus())) {
        if (accounting.getFeeAmount() != null && accounting.getFeeAmount() > 0) {
          pt.setTradeAmount(accounting.getFeeAmount());
          pt.setLastAmount(accounting.getFeeAmount());
          paymentTransService.save(pt);
        }
      }
    }
  }

}
