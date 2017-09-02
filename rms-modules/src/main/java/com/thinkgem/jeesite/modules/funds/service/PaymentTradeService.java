package com.thinkgem.jeesite.modules.funds.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;

/**
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class PaymentTradeService extends CrudService<PaymentTradeDao, PaymentTrade> {

  /**
   * 删除合同下指定交易类型的账务交易记录
   */
  @Transactional(readOnly = false)
  public void deleteByTradeIds(List<String> tradingAccountsIds) {
    PaymentTrade ptr = new PaymentTrade();
    ptr.preUpdate();
    ptr.setTradeIdList(tradingAccountsIds);
    super.delete(ptr);
  }

  /**
   * 获取合同下指定交易类型的账务交易记录
   */
  @Transactional(readOnly = true)
  public List<PaymentTrade> getTradeListByTradeIds(List<String> tradingAccountsIds) {
    PaymentTrade ptr = new PaymentTrade();
    ptr.setDelFlag("0");
    ptr.setTradeIdList(tradingAccountsIds);
    return super.findList(ptr);
  }
}
