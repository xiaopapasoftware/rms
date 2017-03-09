package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

/**
 * 房屋数量统计报表
 * 
 * @author wangshujin
 */
public class HouseReport extends DataEntity<HouseReport> implements Comparable<HouseReport> {

  private static final long serialVersionUID = 8680192864814821364L;

  private PropertyProject propertyProject; // 物业项目

  private String projectName; // 物业项目名称

  private String totalNum;// 总数数量

  private String renovationNum;// 待装修数量

  private String toBeReservedNum;// 待出租可预订数量

  private String reservedNum;// 已预定数量

  private String partRentNum; // 部分出租数

  private String wholeRentNum; // 完全出租数

  public PropertyProject getPropertyProject() {
    return propertyProject;
  }

  public void setPropertyProject(PropertyProject propertyProject) {
    this.propertyProject = propertyProject;
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

  @ExcelField(title = "待装修房屋数", align = 2, sort = 3)
  public String getRenovationNum() {
    return renovationNum;
  }

  public void setRenovationNum(String renovationNum) {
    this.renovationNum = renovationNum;
  }

  @ExcelField(title = "待出租可预订房屋数", align = 2, sort = 4)
  public String getToBeReservedNum() {
    return toBeReservedNum;
  }

  public void setToBeReservedNum(String toBeReservedNum) {
    this.toBeReservedNum = toBeReservedNum;
  }

  @ExcelField(title = "已预定房屋数", align = 2, sort = 5)
  public String getReservedNum() {
    return reservedNum;
  }

  public void setReservedNum(String reservedNum) {
    this.reservedNum = reservedNum;
  }

  @ExcelField(title = "部分出租房屋数", align = 2, sort = 6)
  public String getPartRentNum() {
    return partRentNum;
  }

  public void setPartRentNum(String partRentNum) {
    this.partRentNum = partRentNum;
  }

  @ExcelField(title = "完全出租房屋数", align = 2, sort = 7)
  public String getWholeRentNum() {
    return wholeRentNum;
  }

  public void setWholeRentNum(String wholeRentNum) {
    this.wholeRentNum = wholeRentNum;
  }


  @Override
  public int compareTo(HouseReport o) {
    int leftTotalNum = Integer.valueOf(this.getTotalNum());
    int rightTotalNum = Integer.valueOf(o.getTotalNum());
    return leftTotalNum - rightTotalNum;
  }
}
