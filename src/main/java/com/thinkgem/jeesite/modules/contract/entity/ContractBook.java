/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 预约看房信息Entity
 * @author huangsc
 */
public class ContractBook extends DataEntity<ContractBook> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// user_id
	private String houseId;		// house_id
	private String roomId;		// room_id
	private String userName;		// user_name
	private String userPhone;		// user_phone
	private String userGender;		// user_gender
	private Date bookDate;		// book_date
	private String bookStatus;		// book_status
	private String salesId;		// sales_id
	private String salesName;
	
	private String projectName;
	private String buildingName;
	private String houseCode;
	private String roomNo;
	
	private String shortDesc;
	private String shortLocation;
	private String attachmentPath;
	
	private String depositId;
	private String contractId;
	
	private String contractCode;
	private Double rent;
	private String payWay;
	
	private String contractBusiStatus;
	
    private String updateUser;
	
	private Date endDate;
	
	private String idNo;
	
	public ContractBook() {
		super();
	}

	public ContractBook(String id){
		super(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}
	
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	
	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	
	public Date getBookDate() {
		return bookDate;
	}

	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}
	
	public String getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}
	
	public String getSalesId() {
		return salesId;
	}

	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getHouseCode() {
		return houseCode;
	}

	public void setHouseCode(String houseCode) {
		this.houseCode = houseCode;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public String getShortLocation() {
		return shortLocation;
	}

	public void setShortLocation(String shortLocation) {
		this.shortLocation = shortLocation;
	}

	public String getDepositId() {
		return depositId;
	}

	public void setDepositId(String depositId) {
		this.depositId = depositId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public Double getRent() {
		return rent;
	}

	public void setRent(Double rent) {
		this.rent = rent;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getContractBusiStatus() {
		return contractBusiStatus;
	}

	public void setContractBusiStatus(String contractBusiStatus) {
		this.contractBusiStatus = contractBusiStatus;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getSalesName() {
		return salesName;
	}

	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}