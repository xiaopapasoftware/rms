package com.thinkgem.jeesite.modules.report.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

/**
 * 房租平均价格统计报表
 * 
 * @author wangshujin
 */
public class RentAveragePriceReport extends DataEntity<RentAveragePriceReport> implements Comparable<RentAveragePriceReport> {

  private static final long serialVersionUID = -757744148201792424L;

  private PropertyProject propertyProject; // 物业项目

  private Date startDate;// 开始日期

  private Date endDate;// 结束日期

  private String projectName; // 物业项目名称

  private String jointRentAvgPrice;// 合租平均价格

  private String entireRentAvgPrice;// 整租平均价格

  public PropertyProject getPropertyProject() {
    return propertyProject;
  }

  public void setPropertyProject(PropertyProject propertyProject) {
    this.propertyProject = propertyProject;
  }

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

  @ExcelField(title = "合租平均价格", align = 2, sort = 2)
  public String getJointRentAvgPrice() {
    return jointRentAvgPrice;
  }

  public void setJointRentAvgPrice(String jointRentAvgPrice) {
    this.jointRentAvgPrice = jointRentAvgPrice;
  }

  @ExcelField(title = "整租平均价格", align = 2, sort = 3)
  public String getEntireRentAvgPrice() {
    return entireRentAvgPrice;
  }

  public void setEntireRentAvgPrice(String entireRentAvgPrice) {
    this.entireRentAvgPrice = entireRentAvgPrice;
  }

  @Override
  public int compareTo(RentAveragePriceReport o) {
    return new BigDecimal(this.jointRentAvgPrice).subtract(new BigDecimal(o.jointRentAvgPrice)).intValue();
  }

}
