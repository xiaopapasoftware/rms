/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;

/**
 * 房屋信息DAO接口
 * @author huangsc
 * @version 2015-06-06
 */
@MyBatisDao
public interface HouseDao extends CrudDao<House> {
	
}