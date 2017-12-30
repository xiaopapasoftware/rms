package com.thinkgem.jeesite.modules.contract.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangshujin
 *
 */
@Service
@Transactional(readOnly = true)
public class AuditService extends CrudService<AuditDao, Audit> {

  @Transactional(readOnly = false)
  public void update(Audit audit) {
    audit.preUpdate();
    dao.update(audit);
  }

  @Transactional(readOnly = false)
  public void insert(String objectType, String nextRole, String objectId, String remarks) {
    Audit audit = new Audit();
    audit.preInsert();
    audit.setObjectId(objectId);
    audit.setObjectType(objectType);
    audit.setNextRole(nextRole);
    audit.setRemarks(remarks);
    dao.insert(audit);
  }

  @Transactional(readOnly = false)
  public void delete(String objectId) {
    Audit audit = new Audit();
    audit.preUpdate();
    audit.setObjectId(objectId);
    dao.delete(audit);
  }

  @Transactional(readOnly = false)
  public void deleteRentContract(String objectId) {
    Audit audit = new Audit();
    audit.preUpdate();
    audit.setObjectId(objectId);
    dao.deleteRentContract(audit);
  }
}
