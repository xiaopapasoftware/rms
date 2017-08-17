/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.app.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.app.entity.AppToken;

/**
 * TOKENDAO接口
 * 
 * @author mabindong
 * @version 2015-11-08
 */
@MyBatisDao
public interface AppTokenDao extends CrudDao<AppToken> {
  public int delByPhone(AppToken appToken);

  public AppToken findByTokenAndExpire(AppToken appToken);

  AppToken findByToken(AppToken appToken);

  AppToken findByPhone(AppToken appToken);
}
