package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.modules.report.dao.ContractReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangshujin
 */
@Service
public class ContractReportService {

    @Autowired
    private ContractReportDao contractReportDao;


    public List queryContractReport() {
        return contractReportDao.queryContractReport();
    }
}
