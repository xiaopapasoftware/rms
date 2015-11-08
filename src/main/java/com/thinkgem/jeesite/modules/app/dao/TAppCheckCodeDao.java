/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.app.entity.TAppCheckCode;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;

/**
 * 验证码DAO接口
 * @author mabindong
 * @version 2015-11-07
 */
@MyBatisDao
public interface TAppCheckCodeDao extends CrudDao<TAppCheckCode> {
	public int delByPhone(TAppCheckCode tAppCheckCode);

	public TAppCheckCode getValidCheckCode(TAppCheckCode tAppCheckCode);
}