/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.entity.Area;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 物业项目Entity
 * 
 * @author huangsc
 * @version 2015-06-03
 */
public class PropertyProject extends DataEntity<PropertyProject> {

  private static final long serialVersionUID = 1L;
  private Neighborhood neighborhood; // 居委会
  private ManagementCompany managementCompany; // 物业公司
  private String projectName; // 物业项目名称
  private String projectSimpleName;// 物业项目拼音首字母
  private String projectAddr; // 物业项目地址
  private String attachmentPath;// 物业项目图片

  private Area area; //区域

  private String cityCode;
  private String cityName;
  private String districtCode;
  private String districtName;
  private Integer coordsys;
  private String commReqId;
  private Integer alipayStatus;

  public PropertyProject() {
    super();
  }

  public PropertyProject(String id) {
    super(id);
  }

  @NotNull(message = "居委会不能为空")
  public Neighborhood getNeighborhood() {
    return neighborhood;
  }

  public void setNeighborhood(Neighborhood neighborhood) {
    this.neighborhood = neighborhood;
  }

  @NotNull(message = "物业公司不能为空")
  public ManagementCompany getManagementCompany() {
    return managementCompany;
  }

  public void setManagementCompany(ManagementCompany managementCompany) {
    this.managementCompany = managementCompany;
  }

  public String getProjectSimpleName() {
    return projectSimpleName;
  }

  public void setProjectSimpleName(String projectSimpleName) {
    this.projectSimpleName = projectSimpleName;
  }

  @Length(min = 1, max = 100, message = "物业项目名称长度必须介于 1 和 100 之间")
  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  @Length(min = 1, max = 300, message = "物业项目地址长度必须介于 1 和 300 之间")
  public String getProjectAddr() {
    return projectAddr;
  }

  public Area getArea() {
    return area;
  }

  public void setArea(Area area) {
    this.area = area;
  }

  public void setProjectAddr(String projectAddr) {
    this.projectAddr = projectAddr;
  }

  public String getAttachmentPath() {
    return attachmentPath;
  }

  public void setAttachmentPath(String attachmentPath) {
    this.attachmentPath = attachmentPath;
  }

  public String getCityCode() {
    return cityCode;
  }

  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getDistrictCode() {
    return districtCode;
  }

  public void setDistrictCode(String districtCode) {
    this.districtCode = districtCode;
  }

  public String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(String districtName) {
    this.districtName = districtName;
  }

  public Integer getCoordsys() {
    return coordsys;
  }

  public void setCoordsys(Integer coordsys) {
    this.coordsys = coordsys;
  }

  public String getCommReqId() {
    return commReqId;
  }

  public void setCommReqId(String commReqId) {
    this.commReqId = commReqId;
  }

  public Integer getAlipayStatus() {
    return alipayStatus;
  }

  public void setAlipayStatus(Integer alipayStatus) {
    this.alipayStatus = alipayStatus;
  }
}
