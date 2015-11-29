/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.person.entity.Partner;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 出租合同Entity
 * 
 * @author huangsc
 * @version 2015-06-11
 */
public class RentContract extends DataEntity<RentContract> {

	private static final long serialVersionUID = 1L;
	private String agreementId; // 原定金协议
	private String contractId; // 原出租合同
	private String contractCode; // 合同编号
	private String contractName; // 合同名称
	private String rentMode; // 出租方式
	private PropertyProject propertyProject; // 物业项目
	private Building building; // 楼宇
	private House house; // 房屋
	private Room room; // 房间
	private User user; // 销售
	private String contractSource; // 合同来源
	private Partner partner; // 合作人
	private Double rental; // 月租金
	private Date startDate; // 合同生效时间
	private Date expiredDate; // 合同过期时间
	private Date signDate; // 合同签订时间
	private String signType; // 合同签订类型
	private String hasTv; // 是否开通有线电视
	private Double tvFee; // 有线电视每月费用
	private String hasNet; // 是否开通宽带
	private Double netFee; // 每月宽带费用
	private Double waterFee; // 合租每月水费
	private Double serviceFee; // 服务费比例
	private Integer renMonths; // 首付房租月数
	private Integer depositMonths; // 房租押金月数
	private Double depositAmount; // 房租押金金额/房租押金续补金额
	private Double depositElectricAmount; // 水电押金金额/水电押金续补金额
	private String hasVisa; // 是否需办理居住证及落户
	private Double meterValue; // 入住分电表系数
	private Double totalMeterValue; // 入住总电表系数
	private Double peakMeterValue; // 入住峰电系数
	private Double flatMeterValue; // 入住平电系数
	private Double valleyMeterValue; // 入住谷电系数
	private Double coalValue; // 入住煤表系数
	private Double waterValue; // 入住水表系数
	private Date remindTime; // 续租提醒时间
	private String contractStatus; // 合同状态
	private String contractBusiStatus; // 合同业务状态

	private String projectName;
	private String buildingBame;
	private String houseNo;
	private String roomNo;

	private String validatorFlag;

	private List<Tenant> tenantList = new ArrayList<Tenant>();// 承租人
	private List<Tenant> liveList = new ArrayList<Tenant>();// 入住人

	private String chargeType;

	/**
	 * 值为1表示：从定金协议转化合同时，保存合同 值为0表示：直接新签合同的保存操作
	 * */
	private String saveSource;

	private String breakDown;

	private List<Accounting> accountList = new ArrayList<Accounting>();
	private List<Accounting> outAccountList = new ArrayList<Accounting>();

	private String tradeType;

	private String isSpecial;

	private String name;
	private String refAgreementName;// 原定金协议名称，转合同的定金协议
	private String refContractName;// 原合同名称，当前合同为续签合同时，保存原合同名称

	private String rentContractFile;// 出租合同文件
	private String rentContractCusIDFile;// 租客身份证
	private String rentContractOtherFile;// 出租合同其他附件
	private Double depositAgreementAmount; // 定金协议转合同，从定金协议带过来的定金金额

	private String oriEndDate;//为了实现续签合同的开始日期默认为原合同的结束日期，则把原合同的结束日期带到页面
	
	private String returnDate;
	
	private String returnRemark;//退租备注
	
	private String dataSource;

	public RentContract() {
		super();
	}

	public RentContract(String id) {
		super(id);
	}
	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	@Length(min = 0, max = 64, message = "原出租合同长度必须介于 0 和 64 之间")
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	@Length(min = 1, max = 100, message = "合同名称长度必须介于 1 和 100 之间")
	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	@Length(min = 1, max = 64, message = "出租方式长度必须介于 1 和 64 之间")
	public String getRentMode() {
		return rentMode;
	}

	public void setRentMode(String rentMode) {
		this.rentMode = rentMode;
	}

	@NotNull(message = "物业项目不能为空")
	public PropertyProject getPropertyProject() {
		return propertyProject;
	}

	public void setPropertyProject(PropertyProject propertyProject) {
		this.propertyProject = propertyProject;
	}

	@NotNull(message = "楼宇不能为空")
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	@NotNull(message = "房屋不能为空")
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Length(min = 1, max = 64, message = "合同来源长度必须介于 1 和 64 之间")
	public String getContractSource() {
		return contractSource;
	}

	public void setContractSource(String contractSource) {
		this.contractSource = contractSource;
	}
	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	@NotNull(message = "月租金不能为空")
	public Double getRental() {
		return rental;
	}

	public void setRental(Double rental) {
		this.rental = rental;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message = "合同生效时间不能为空")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message = "合同过期时间不能为空")
	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message = "合同签订时间不能为空")
	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	@Length(min = 0, max = 64, message = "合同签订类型长度必须介于 0 和 64 之间")
	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	@Length(min = 0, max = 64, message = "是否开通有线电视长度必须介于 0 和 64 之间")
	public String getHasTv() {
		return hasTv;
	}

	public void setHasTv(String hasTv) {
		this.hasTv = hasTv;
	}

	public Double getTvFee() {
		return tvFee;
	}

	public void setTvFee(Double tvFee) {
		this.tvFee = tvFee;
	}

	@Length(min = 0, max = 64, message = "是否开通宽带长度必须介于 0 和 64 之间")
	public String getHasNet() {
		return hasNet;
	}

	public void setHasNet(String hasNet) {
		this.hasNet = hasNet;
	}

	public Double getNetFee() {
		return netFee;
	}

	public void setNetFee(Double netFee) {
		this.netFee = netFee;
	}

	public Double getWaterFee() {
		return waterFee;
	}

	public void setWaterFee(Double waterFee) {
		this.waterFee = waterFee;
	}

	public Double getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}

	public Integer getRenMonths() {
		return renMonths;
	}

	public void setRenMonths(Integer renMonths) {
		this.renMonths = renMonths;
	}

	public Integer getDepositMonths() {
		return depositMonths;
	}

	public void setDepositMonths(Integer depositMonths) {
		this.depositMonths = depositMonths;
	}

	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public Double getDepositElectricAmount() {
		return depositElectricAmount;
	}

	public void setDepositElectricAmount(Double depositElectricAmount) {
		this.depositElectricAmount = depositElectricAmount;
	}

	@Length(min = 0, max = 64, message = "是否需办理居住证及落户长度必须介于 0 和 64 之间")
	public String getHasVisa() {
		return hasVisa;
	}

	public void setHasVisa(String hasVisa) {
		this.hasVisa = hasVisa;
	}

	public Double getMeterValue() {
		return meterValue;
	}

	public void setMeterValue(Double meterValue) {
		this.meterValue = meterValue;
	}

	public Double getTotalMeterValue() {
		return totalMeterValue;
	}

	public void setTotalMeterValue(Double totalMeterValue) {
		this.totalMeterValue = totalMeterValue;
	}

	public Double getPeakMeterValue() {
		return peakMeterValue;
	}

	public void setPeakMeterValue(Double peakMeterValue) {
		this.peakMeterValue = peakMeterValue;
	}

	public Double getFlatMeterValue() {
		return flatMeterValue;
	}

	public void setFlatMeterValue(Double flatMeterValue) {
		this.flatMeterValue = flatMeterValue;
	}

	public Double getValleyMeterValue() {
		return valleyMeterValue;
	}

	public void setValleyMeterValue(Double valleyMeterValue) {
		this.valleyMeterValue = valleyMeterValue;
	}

	public Double getCoalValue() {
		return coalValue;
	}

	public void setCoalValue(Double coalValue) {
		this.coalValue = coalValue;
	}

	public Double getWaterValue() {
		return waterValue;
	}

	public void setWaterValue(Double waterValue) {
		this.waterValue = waterValue;
	}

	public Date getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}

	@Length(min = 0, max = 64, message = "合同状态长度必须介于 0 和 64 之间")
	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	@Length(min = 0, max = 64, message = "合同业务状态长度必须介于 0 和 64 之间")
	public String getContractBusiStatus() {
		return contractBusiStatus;
	}

	public void setContractBusiStatus(String contractBusiStatus) {
		this.contractBusiStatus = contractBusiStatus;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBuildingBame() {
		return buildingBame;
	}

	public void setBuildingBame(String buildingBame) {
		this.buildingBame = buildingBame;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public String getValidatorFlag() {
		return validatorFlag;
	}

	public void setValidatorFlag(String validatorFlag) {
		this.validatorFlag = validatorFlag;
	}

	public List<Tenant> getTenantList() {
		return tenantList;
	}

	public void setTenantList(List<Tenant> tenantList) {
		this.tenantList = tenantList;
	}

	public List<Tenant> getLiveList() {
		return liveList;
	}

	public void setLiveList(List<Tenant> liveList) {
		this.liveList = liveList;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getSaveSource() {
		return saveSource;
	}

	public void setSaveSource(String saveSource) {
		this.saveSource = saveSource;
	}

	public String getBreakDown() {
		return breakDown;
	}

	public void setBreakDown(String breakDown) {
		this.breakDown = breakDown;
	}

	public List<Accounting> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Accounting> accountList) {
		this.accountList = accountList;
	}

	public List<Accounting> getOutAccountList() {
		return outAccountList;
	}

	public void setOutAccountList(List<Accounting> outAccountList) {
		this.outAccountList = outAccountList;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getRefAgreementName() {
		return refAgreementName;
	}

	public void setRefAgreementName(String refAgreementName) {
		this.refAgreementName = refAgreementName;
	}
	public String getRefContractName() {
		return refContractName;
	}

	public void setRefContractName(String refContractName) {
		this.refContractName = refContractName;
	}
	public String getRentContractFile() {
		return rentContractFile;
	}

	public void setRentContractFile(String rentContractFile) {
		this.rentContractFile = rentContractFile;
	}

	public String getRentContractCusIDFile() {
		return rentContractCusIDFile;
	}

	public void setRentContractCusIDFile(String rentContractCusIDFile) {
		this.rentContractCusIDFile = rentContractCusIDFile;
	}

	public String getRentContractOtherFile() {
		return rentContractOtherFile;
	}

	public void setRentContractOtherFile(String rentContractOtherFile) {
		this.rentContractOtherFile = rentContractOtherFile;
	}

	public Double getDepositAgreementAmount() {
		return depositAgreementAmount;
	}

	public void setDepositAgreementAmount(Double depositAgreementAmount) {
		this.depositAgreementAmount = depositAgreementAmount;
	}
	public String getOriEndDate() {
		return oriEndDate;
	}

	public void setOriEndDate(String oriEndDate) {
		this.oriEndDate = oriEndDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnRemark() {
		return returnRemark;
	}

	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
}