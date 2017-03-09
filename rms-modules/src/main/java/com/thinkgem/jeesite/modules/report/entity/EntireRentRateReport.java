package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 整租出租率统计报表
 * 
 * @author wangshujin
 */
public class EntireRentRateReport extends DataEntity<EntireRentRateReport> implements Comparable<EntireRentRateReport> {

  private static final long serialVersionUID = -1333669783060223262L;

  private Date startDate;// 指定的查询日期

  private String projectName; // 物业项目名称

  private String totalNum;// 房屋总数

  private String rentedNum;// 部分/完全出租房屋数

  private String rentRate;// 出租率

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }


  @ExcelField(title = "物业项目名称", align = 2, sort = 1)
  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  @ExcelField(title = "房屋总数", align = 2, sort = 2)
  public String getTotalNum() {
    return totalNum;
  }

  public void setTotalNum(String totalNum) {
    this.totalNum = totalNum;
  }

  @ExcelField(title = "部分/完全出租房屋数", align = 2, sort = 3)
  public String getRentedNum() {
    return rentedNum;
  }

  public void setRentedNum(String rentedNum) {
    this.rentedNum = rentedNum;
  }

  @ExcelField(title = "出租率", align = 2, sort = 4)
  public String getRentRate() {
    return rentRate;
  }

  public void setRentRate(String rentRate) {
    this.rentRate = rentRate;
  }

  @Override
  public int compareTo(EntireRentRateReport o) {
    return Integer.valueOf(this.getTotalNum()) - Integer.valueOf(o.getTotalNum());
  }

}
