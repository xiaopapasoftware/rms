/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.funds.dao.PaymenttransDtlDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymenttransDtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * fundsService
 * 
 * @author funds
 * @version 2017-10-21
 */
@Service
@Transactional(readOnly = true)
public class PaymenttransDtlService extends CrudService<PaymenttransDtlDao, PaymenttransDtl> {

  @Autowired
  private PaymenttransDtlDao paymenttransDtlDao;

  public PaymenttransDtl get(String id) {
    return super.get(id);
  }

  public List<PaymenttransDtl> findList(PaymenttransDtl paymenttransDtl) {
    return super.findList(paymenttransDtl);
  }

  public Page<PaymenttransDtl> findPage(Page<PaymenttransDtl> page, PaymenttransDtl paymenttransDtl) {
    return super.findPage(page, paymenttransDtl);
  }

  @Transactional(readOnly = false)
  public void save(PaymenttransDtl paymenttransDtl) {
    super.save(paymenttransDtl);
  }

  @Transactional(readOnly = false)
  public void delete(PaymenttransDtl paymenttransDtl) {
    super.delete(paymenttransDtl);
  }

  public List<PaymenttransDtl> queryPaymenttransDtlListByContractIdList(Date startDate, Date endDate, List<String> contractIdList) {
    return paymenttransDtlDao.queryPaymenttransDtlListByContractIdList(startDate, endDate, contractIdList);
  }

}
