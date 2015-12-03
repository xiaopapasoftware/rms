/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.message.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.message.entity.Message;

/**
 * 消息DAO接口
 * @author mabindong
 * @version 2015-12-01
 */
@MyBatisDao
public interface MessageDao extends CrudDao<Message> {
	
}