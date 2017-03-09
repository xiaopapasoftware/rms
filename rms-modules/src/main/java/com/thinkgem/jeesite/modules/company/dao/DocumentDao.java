/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.company.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.company.entity.Document;

/**
 * 办公文件管理DAO接口
 * @author huangsc
 * @version 2015-09-12
 */
@MyBatisDao
public interface DocumentDao extends CrudDao<Document> {
	
}