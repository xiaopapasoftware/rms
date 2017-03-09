package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 整租平均房租价格
 * 
 * @author wangshujin
 */
public class WholeAvgPriceReport extends DataEntity<WholeAvgPriceReport> {

  private static final long serialVersionUID = 527341013657196456L;

  private Date startDate;// 开始日期

  private Date endDate;// 结束日期

  private String projectName; // 物业项目名称

  // 分为：1室1厅、1室2厅、2室1厅、2室2厅、3室1厅、3室2厅、4室1厅、4室2厅
  private String wholeAvgHouseType;// 房型

  // 平均房租价格
  private String wholeAvgPrice;

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

  @ExcelField(title = "物业项目名称", align = 2, sort = 2)
  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  @ExcelField(title = "整租房型", align = 2, sort = 1)
  public String getWholeAvgHouseType() {
    return wholeAvgHouseType;
  }

  public void setWholeAvgHouseType(String wholeAvgHouseType) {
    this.wholeAvgHouseType = wholeAvgHouseType;
  }

  @ExcelField(title = "整租租赁平均价格", align = 2, sort =3)
  public String getWholeAvgPrice() {
    return wholeAvgPrice;
  }

  public void setWholeAvgPrice(String wholeAvgPrice) {
    this.wholeAvgPrice = wholeAvgPrice;
  }



}
