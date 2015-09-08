/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.common.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;

/**
 * 附件DAO接口
 * 
 * @author huangsc
 * @version 2015-06-14
 */
@MyBatisDao
public interface AttachmentDao extends CrudDao<Attachment> {

	/**
	 * 根据承租合同、出租合同、物业项目、楼宇、房屋、房间的ID来更新对应的文件路径
	 * */
	int updateAttachmentPathByType(Attachment attachment);
}