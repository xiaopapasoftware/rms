/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.entity.Questions;
import com.thinkgem.jeesite.modules.app.dao.QuestionsDao;

/**
 * 常见问题Service
 * @author daniel
 * @version 2016-05-10
 */
@Service
@Transactional(readOnly = true)
public class QuestionsService extends CrudService<QuestionsDao, Questions> {

	public Questions get(String id) {
		return super.get(id);
	}
	
	public List<Questions> findList(Questions questions) {
		return super.findList(questions);
	}
	
	public Page<Questions> findPage(Page<Questions> page, Questions questions) {
		return super.findPage(page, questions);
	}
	
	@Transactional(readOnly = false)
	public void save(Questions questions) {
		super.save(questions);
	}
	
	@Transactional(readOnly = false)
	public void delete(Questions questions) {
		super.delete(questions);
	}
	
}