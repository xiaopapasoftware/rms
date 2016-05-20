/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.app.entity.Questions;

/**
 * 常见问题DAO接口
 * @author daniel
 * @version 2016-05-10
 */
@MyBatisDao
public interface QuestionsDao extends CrudDao<Questions> {
	
}