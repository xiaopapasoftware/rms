package com.thinkgem.jeesite.modules.report.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 单间的房租平均价格统计报表
 * 
 * @author wangshujin
 */
public class RentAveragePriceReport extends DataEntity<RentAveragePriceReport> implements Comparable<RentAveragePriceReport> {

  private static final long serialVersionUID = -757744148201792424L;

  private Date startDate;// 开始日期

  private Date endDate;// 结束日期

  private String projectName; // 物业项目名称

  private String jointRentAvgPrice;// 单间平均租赁价格

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  @ExcelField(title = "物业项目名称", align = 2, sort = 1)
  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  @ExcelField(title = "单间租赁平均价格", align = 2, sort = 2)
  public String getJointRentAvgPrice() {
    return jointRentAvgPrice;
  }

  public void setJointRentAvgPrice(String jointRentAvgPrice) {
    this.jointRentAvgPrice = jointRentAvgPrice;
  }

  @Override
  public int compareTo(RentAveragePriceReport o) {
    return new BigDecimal(this.jointRentAvgPrice).subtract(new BigDecimal(o.jointRentAvgPrice)).intValue();
  }
}
