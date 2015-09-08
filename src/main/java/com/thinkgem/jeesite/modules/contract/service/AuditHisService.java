package com.thinkgem.jeesite.modules.contract.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;

@Service
@Transactional(readOnly = true)
public class AuditHisService extends CrudService<AuditHisDao, AuditHis> {
	public Page<AuditHis> findPage(Page<AuditHis> page, AuditHis auditHis) {
		return super.findPage(page, auditHis);
	}
}
