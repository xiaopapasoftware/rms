/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.app.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.dao.CustBindInfoDao;
import com.thinkgem.jeesite.modules.app.entity.CustBindInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * APP用户Service
 * 
 * @author mabindong
 * @version 2015-11-24
 */
@Service
@Transactional(readOnly = true)
public class CustBindInfoService extends CrudService<CustBindInfoDao, CustBindInfo> {

    public CustBindInfo getByCustomerIdAndType(String customerId, String accountType){
        return dao.getByCustomerIdAndType(customerId, accountType);
    }

}
