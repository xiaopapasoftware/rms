/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.app.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.app.util.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.dao.AppTokenDao;
import com.thinkgem.jeesite.modules.app.entity.AppToken;

/**
 * TOKENService
 * 
 * @author mabindong
 * @version 2015-11-08
 */
@Service
@Transactional(readOnly = true)
public class AppTokenService extends CrudService<AppTokenDao, AppToken> {
  @Autowired
  private AppTokenDao appTokenDao;

  public AppToken get(String id) {
    return super.get(id);
  }

  public List<AppToken> findList(AppToken appToken) {
    return super.findList(appToken);
  }

  public Page<AppToken> findPage(Page<AppToken> page, AppToken appToken) {
    return super.findPage(page, appToken);
  }

  @Transactional(readOnly = false)
  public void save(AppToken appToken) {
    super.save(appToken);
  }

  @Transactional(readOnly = false)
  public void delete(AppToken appToken) {
    super.delete(appToken);
  }

  /**
   * 根据电话号码，更新TOKEN
   * 
   */
  @Transactional(readOnly = false)
  public void merge(AppToken appToken) {
    appTokenDao.delByPhone(appToken);
    super.save(appToken);
  }

  public AppToken findByTokenAndExpire(AppToken appToken) {
    return appTokenDao.findByTokenAndExpire(appToken);
  }

  public AppToken findByToken(AppToken appToken) {
    return appTokenDao.findByToken(appToken);
  }

  public ResponseData tokenMerge(String telPhone) {
    AppToken appToken = new AppToken();
    appToken.setPhone(telPhone);
    appToken.setToken(TokenGenerator.generateValue());
        /*token 有效期7天*/
    LocalDate localDate = LocalDate.now().plusDays(Constants.TOKEN_EXPIRE_DAY);
    Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.of("GMT")));
    appToken.setExprie(Date.from(instant));
    merge(appToken);
    return ResponseData.success().message("登陆成功").data(appToken.getToken());
  }
}
