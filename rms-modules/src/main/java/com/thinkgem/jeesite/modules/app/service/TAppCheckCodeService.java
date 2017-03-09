/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.dao.TAppCheckCodeDao;
import com.thinkgem.jeesite.modules.app.entity.TAppCheckCode;

/**
 * 验证码Service
 * @author mabindong
 * @version 2015-11-07
 */
@Service
@Transactional(readOnly = true)
public class TAppCheckCodeService extends CrudService<TAppCheckCodeDao, TAppCheckCode> {

	@Autowired
	private TAppCheckCodeDao tAppCheckCodeDao;
	
	public TAppCheckCode get(String id) {
		return super.get(id);
	}
	
	public List<TAppCheckCode> findList(TAppCheckCode tAppCheckCode) {
		return super.findList(tAppCheckCode);
	}
	
	public Page<TAppCheckCode> findPage(Page<TAppCheckCode> page, TAppCheckCode tAppCheckCode) {
		return super.findPage(page, tAppCheckCode);
	}
	
	@Transactional(readOnly = false)
	public void save(TAppCheckCode tAppCheckCode) {
		super.save(tAppCheckCode);
	}
	
	@Transactional(readOnly = false)
	public void delete(TAppCheckCode tAppCheckCode) {
		super.delete(tAppCheckCode);
	}
	
	/**
	 * 根据电话号码，更新验证码
	 * @param tAppCheckCode
	 */
	@Transactional(readOnly = false)
	public void merge(TAppCheckCode tAppCheckCode) {
		tAppCheckCodeDao.delByPhone(tAppCheckCode);
		super.save(tAppCheckCode);
	}
	
	
	/**
	 * 校验验证码
	 * 即查询是否存在不过期的验证码
	 * @param tAppCheckCode
	 */
	@Transactional(readOnly = false)
	public boolean verifyCheckCode(TAppCheckCode tAppCheckCode) {
		tAppCheckCode = tAppCheckCodeDao.getValidCheckCode(tAppCheckCode);
		if(tAppCheckCode == null){
			return false;
		}else{
			return true;
		}
	}
	
	
}