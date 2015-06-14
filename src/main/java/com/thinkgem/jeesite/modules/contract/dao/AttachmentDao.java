/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.Attachment;

/**
 * 附件DAO接口
 * @author huangsc
 * @version 2015-06-14
 */
@MyBatisDao
public interface AttachmentDao extends CrudDao<Attachment> {
	
}