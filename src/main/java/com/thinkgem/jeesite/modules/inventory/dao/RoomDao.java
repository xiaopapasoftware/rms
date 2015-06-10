/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inventory.entity.Room;

/**
 * 房间信息DAO接口
 * @author huangsc
 * @version 2015-06-09
 */
@MyBatisDao
public interface RoomDao extends CrudDao<Room> {
	
}