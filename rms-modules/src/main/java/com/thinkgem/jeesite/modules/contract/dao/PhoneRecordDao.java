/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.PhoneRecord;

/**
 * 预约记录AO接口
 * 
 * @author xiao
 */
@MyBatisDao
public interface PhoneRecordDao extends CrudDao<PhoneRecord> {

}
