/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.person.entity.Lessor;

/**
 * 出租人管理DAO接口
 * @author wage
 * @version 2015-09-18
 */
@MyBatisDao
public interface LessorDao extends CrudDao<Lessor> {
	
}