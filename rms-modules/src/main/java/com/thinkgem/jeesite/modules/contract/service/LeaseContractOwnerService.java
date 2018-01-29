package com.thinkgem.jeesite.modules.contract.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContractOwner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xiao
 */
@Service
@Transactional(readOnly = true)
public class LeaseContractOwnerService extends CrudService<LeaseContractOwnerDao, LeaseContractOwner> {

    public List<LeaseContractOwner> getListByContractId (String leaseContractId) {
        return dao.getListByContractId(leaseContractId);
    }

    @Transactional(readOnly = false)
    public void deleteListByContractId(String leaseContractId) {
        dao.deleteListByContractId(leaseContractId);
    }

    public List<String> getContractIdListByOwnerIdList (List<String> ownerIdList) {
        return dao.getContractIdListByOwnerIdList(ownerIdList);
    }

}
