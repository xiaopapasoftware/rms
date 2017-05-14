/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.dao.ReceiptDao;

/**
 * 账务收据Service
 *
 * @author huangsc
 * @version 2015-06-11
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
    public void save(Receipt receipt) {
        super.save(receipt);
    }

    @Transactional(readOnly = false)
    public void delete(Receipt receipt) {
        super.delete(receipt);
    }

}