/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.app.entity.AppUser;
import com.thinkgem.jeesite.modules.app.entity.TAppCheckCode;

/**
 * APP用户DAO接口
 * @author mabindong
 * @version 2015-11-08
 */
@MyBatisDao
public interface AppUserDao extends CrudDao<AppUser> {
	public AppUser getByPhone(AppUser appUser);
}