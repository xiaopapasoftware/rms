package com.thinkgem.jeesite.modules.contract.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.common.enums.ActFlagEnum;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.sys.entity.User;
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
	User currentUser = UserUtils.getUser();
	Date currentDate = new Date();
	saveAuditHis.setId(IdGen.uuid());
	saveAuditHis.setObjectType(auditType);// 审核类型
	saveAuditHis.setObjectId(auditHis.getObjectId());// 审核对象ID
	saveAuditHis.setAuditMsg(auditHis.getAuditMsg());
	saveAuditHis.setAuditStatus(auditHis.getAuditStatus());// 1:通过 2:拒绝
	saveAuditHis.setCreateDate(currentDate);
	saveAuditHis.setCreateBy(currentUser);
	saveAuditHis.setUpdateDate(currentDate);
	saveAuditHis.setUpdateBy(currentUser);
	saveAuditHis.setAuditTime(currentDate);
	saveAuditHis.setAuditUser(currentUser.getId());
	saveAuditHis.setDelFlag(ActFlagEnum.NORMAL.getValue());
	dao.insert(saveAuditHis);
    }
}
