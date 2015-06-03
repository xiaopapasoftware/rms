/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.person.entity.NeighborhoodContact;

/**
 * 居委会联系人DAO接口
 * @author huangsc
 * @version 2015-06-02
 */
@MyBatisDao
public interface NeighborhoodContactDao extends CrudDao<NeighborhoodContact> {
	
}