/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.company.service;

import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.modules.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.company.dao.WorkRecordDao;
import com.thinkgem.jeesite.modules.company.entity.WorkRecord;

/**
 * 工作记录Service
 * @author huangsc
 * @version 2015-09-12
 */
@Service
@Transactional(readOnly = true)
public class WorkRecordService extends CrudService<WorkRecordDao, WorkRecord> {

	public WorkRecord get(String id) {
		return super.get(id);
	}
	
	public List<WorkRecord> findList(WorkRecord workRecord) {
		return super.findList(workRecord);
	}
	
	public Page<WorkRecord> findPage(Page<WorkRecord> page, WorkRecord workRecord) {
		return super.findPage(page, workRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkRecord workRecord) {
		super.save(workRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(WorkRecord workRecord) {
		super.delete(workRecord);
	}
	
	@Transactional(readOnly = false)
	public void review(WorkRecord workRecord) {
		WorkRecord updateWorkRecord = super.get(workRecord.getId());
		updateWorkRecord.setRecordStatus("2");//已审阅
		updateWorkRecord.setReviewDate(new Date());
		updateWorkRecord.setReviewRemarks(workRecord.getReviewRemarks());
		updateWorkRecord.setUser(UserUtils.getUser());
		updateWorkRecord.setUpdateBy(UserUtils.getUser());
		updateWorkRecord.setUpdateDate(new Date());
		save(updateWorkRecord);
	}
}