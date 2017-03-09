/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.app.entity.Repair;

/**
 * 报修DAO接口
 * @author daniel
 * @version 2016-07-04
 */
@MyBatisDao
public interface RepairDao extends CrudDao<Repair> {
	
}