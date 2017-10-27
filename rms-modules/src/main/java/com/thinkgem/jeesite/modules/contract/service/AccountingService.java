/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.dao.AccountingDao;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;


/**
 * 退租核算Service
 * 
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class AccountingService extends CrudService<AccountingDao, Accounting> {

  @Override
  public List<Accounting> findList(Accounting entity) {
    areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
    return super.findList(entity);
  }

  @Override
  public Page<Accounting> findPage(Page<Accounting> page, Accounting entity) {
    areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
    return super.findPage(page, entity);
  }

  /**
   * 删除合同下的退租核算记录
   */
  @Transactional(readOnly = false)
  public void delRentContractAccountings(RentContract rentContract) {
    Accounting accounting = new Accounting();
    accounting.preUpdate();
    accounting.setRentContract(rentContract);
    super.delete(accounting);
  }

  @Transactional(readOnly = false)
  public void updatePaymentTransId(Accounting accounting) {
    dao.updatePaymentTransId(accounting);
  }
}
