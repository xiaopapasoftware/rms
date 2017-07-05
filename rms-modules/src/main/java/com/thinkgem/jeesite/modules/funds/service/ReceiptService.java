/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import java.util.List;

import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.funds.dao.ReceiptDao;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;

/**
 * @author wangshujin
 *
 */
@Service
@Transactional(readOnly = true)
public class ReceiptService extends CrudService<ReceiptDao, Receipt> {

  public Receipt get(String id) {
    return super.get(id);
  }

  public List<Receipt> findList(Receipt receipt) {
    areaScopeFilter(receipt, "dsf", "tp.area_id=sua.area_id");
    return super.findList(receipt);
  }

  public Page<Receipt> findPage(Page<Receipt> page, Receipt receipt) {
    areaScopeFilter(receipt, "dsf", "tp.area_id=sua.area_id");
    return super.findPage(page, receipt);
  }

  @Transactional(readOnly = false)
  public void deleteByTradeIds(List<String> tradingAccountsIds) {
    Receipt receipt = new Receipt();
    receipt.setTradingAccountsIdList(tradingAccountsIds);
    receipt.preUpdate();
    super.delete(receipt);
  }

  /**
   * 校验收据编号重复 true为重复，false为不重复
   */
  public boolean checkReceiptNoIsRepeat(String receiptNo) {
    Receipt tmpReceipt = new Receipt();
    tmpReceipt.setReceiptNo(receiptNo);
    List<Receipt> list = super.findList(tmpReceipt);
    if (CollectionUtils.isNotEmpty(list)) {
      return true;
    } else {
      return false;
    }
  }


}
