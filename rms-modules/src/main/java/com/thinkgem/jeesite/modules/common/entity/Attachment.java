/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.common.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 附件Entity
 * 
 * @author huangsc
 * @version 2015-06-14
 */
public class Attachment extends DataEntity<Attachment> {

  private static final long serialVersionUID = 1L;
  private String leaseContractId; // 承租合同
  private String rentContractId; // 出租合同
  private String depositAgreemId;// 定金协议
  private String tradingAccountsId;// 账务交易
  private String propertyProjectId; // 物业项目
  private String buildingId; // 楼宇
  private String houseId; // 房屋
  private String roomId; // 房间
  private String attachmentName; // 附件名称
  private String attachmentType; // 附件类型
  private String attachmentPath; // 附件地址
  private String bizId;
  private List<String> tradingAccountsIdList;// 用于SQL参数

  public Attachment() {
    super();
  }

  public Attachment(String id) {
    super(id);
  }

  @Length(min = 0, max = 64, message = "承租合同长度必须介于 0 和 64 之间")
  public String getLeaseContractId() {
    return leaseContractId;
  }

  public void setLeaseContractId(String leaseContractId) {
    this.leaseContractId = leaseContractId;
  }

  @Length(min = 0, max = 64, message = "出租合同长度必须介于 0 和 64 之间")
  public String getRentContractId() {
    return rentContractId;
  }

  public void setRentContractId(String rentContractId) {
    this.rentContractId = rentContractId;
  }

  public String getDepositAgreemId() {
    return depositAgreemId;
  }

  public void setDepositAgreemId(String depositAgreemId) {
    this.depositAgreemId = depositAgreemId;
  }

  public String getTradingAccountsId() {
    return tradingAccountsId;
  }

  public void setTradingAccountsId(String tradingAccountsId) {
    this.tradingAccountsId = tradingAccountsId;
  }

  @Length(min = 0, max = 64, message = "物业项目长度必须介于 0 和 64 之间")
  public String getPropertyProjectId() {
    return propertyProjectId;
  }

  public void setPropertyProjectId(String propertyProjectId) {
    this.propertyProjectId = propertyProjectId;
  }

  @Length(min = 0, max = 64, message = "楼宇长度必须介于 0 和 64 之间")
  public String getBuildingId() {
    return buildingId;
  }

  public void setBuildingId(String buildingId) {
    this.buildingId = buildingId;
  }

  @Length(min = 0, max = 64, message = "房屋长度必须介于 0 和 64 之间")
  public String getHouseId() {
    return houseId;
  }

  public void setHouseId(String houseId) {
    this.houseId = houseId;
  }

  @Length(min = 0, max = 64, message = "房间长度必须介于 0 和 64 之间")
  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  @Length(min = 0, max = 64, message = "附件名称长度必须介于 0 和 64 之间")
  public String getAttachmentName() {
    return attachmentName;
  }

  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }

  @Length(min = 0, max = 64, message = "附件类型长度必须介于 0 和 64 之间")
  public String getAttachmentType() {
    return attachmentType;
  }

  public void setAttachmentType(String attachmentType) {
    this.attachmentType = attachmentType;
  }

  @Length(min = 0, max = 64, message = "附件地址长度必须介于 0 和 64 之间")
  public String getAttachmentPath() {
    return attachmentPath;
  }

  public void setAttachmentPath(String attachmentPath) {
    this.attachmentPath = attachmentPath;
  }

  public String getBizId() {
    return bizId;
  }

  public void setBizId(String bizId) {
    this.bizId = bizId;
  }

  public List<String> getTradingAccountsIdList() {
    return tradingAccountsIdList;
  }

  public void setTradingAccountsIdList(List<String> tradingAccountsIdList) {
    this.tradingAccountsIdList = tradingAccountsIdList;
  }
}
