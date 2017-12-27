package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.FeeReportDao;
import com.thinkgem.jeesite.modules.report.entity.FeeReport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FeeReportService extends CrudService<FeeReportDao, FeeReport> {

  public List<FeeReport> getFeeReportByRoomIdList(List<String> roomIdList, Double minValue, Double maxValue) {
    return dao.getFeeReportByRoomIdList(roomIdList, minValue, maxValue);
  }

  public List<FeeReport> getFeeReportList(int size) {
    return dao.getFeeReportList(size);
  }

  @Transactional(readOnly = false)
  public void deleteFeeReportByRentContractId(String rentContractId) {
    dao.deleteFeeReportByRentContractId(rentContractId);
  }

}
