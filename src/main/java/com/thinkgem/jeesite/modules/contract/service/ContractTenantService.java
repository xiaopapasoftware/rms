package com.thinkgem.jeesite.modules.contract.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;

/**
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class ContractTenantService extends CrudService<ContractTenantDao, ContractTenant> {

    @Transactional(readOnly = false)
    public void delete(ContractTenant contractTenant) {
	contractTenant.preUpdate();
	dao.delete(contractTenant);
    }

    @Transactional(readOnly = false)
    public void save(ContractTenant contractTenant) {
	contractTenant.preInsert();
	dao.insert(contractTenant);
    }
}
