/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;

/**
 * 协议变更DAO接口
 * @author huangsc
 * @version 2015-06-11
 */
@MyBatisDao
public interface AgreementChangeDao extends CrudDao<AgreementChange> {
	
}