package com.thinkgem.jeesite.modules.contract.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class AuditHisService extends CrudService<AuditHisDao, AuditHis> {

    public Page<AuditHis> findPage(Page<AuditHis> page, AuditHis auditHis) {
	return super.findPage(page, auditHis);
    }

    // 保存审核历史记录
    @Transactional(readOnly = false)
    public void saveAuditHis(AuditHis auditHis, String auditType) {
	AuditHis saveAuditHis = new AuditHis();
	saveAuditHis.preInsert();
	saveAuditHis.setObjectType(auditType);// 审核类型
	saveAuditHis.setObjectId(auditHis.getObjectId());// 审核对象ID
	saveAuditHis.setAuditMsg(auditHis.getAuditMsg());
	saveAuditHis.setAuditStatus(auditHis.getAuditStatus());// 1:通过 2:拒绝
	saveAuditHis.setAuditTime(new Date());
	saveAuditHis.setAuditUser(UserUtils.getUser().getId());
	dao.insert(saveAuditHis);
    }
}
