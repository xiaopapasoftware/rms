package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

/**
 * 合租出租率统计
 * 
 * @author wangshujin
 */
public class JointRentRateReport extends DataEntity<JointRentRateReport> implements Comparable<JointRentRateReport> {

  private static final long serialVersionUID = 1592319323465365879L;

  private PropertyProject propertyProject; // 物业项目

  private Date startDate;// 开始日期

  private Date endDate;// 结束日期

  private String projectName; // 物业项目名称

  private String totalNum;// 总数数量

  private String rentedNum;// 已出租房间数

  private String rentRate;// 出租率

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

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getTotalNum() {
    return totalNum;
  }

  public void setTotalNum(String totalNum) {
    this.totalNum = totalNum;
  }

  public String getRentedNum() {
    return rentedNum;
  }

  public void setRentedNum(String rentedNum) {
    this.rentedNum = rentedNum;
  }

  public String getRentRate() {
    return rentRate;
  }

  public void setRentRate(String rentRate) {
    this.rentRate = rentRate;
  }

  @Override
  public int compareTo(JointRentRateReport o) {
    return Integer.valueOf(this.getTotalNum()) - Integer.valueOf(o.getTotalNum());
  }
}
