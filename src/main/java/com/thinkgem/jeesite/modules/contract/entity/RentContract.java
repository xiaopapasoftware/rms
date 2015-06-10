/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 出租合同Entity
 * @author huangsc
 * @version 2015-06-11
 */
public class RentContract extends DataEntity<RentContract> {
	
	private static final long serialVersionUID = 1L;
	private String contractId;		// 原出租合同
	private String contractName;		// 合同名称
	private String rentMode;		// 出租方式
	private PropertyProject propertyProject;		// 物业项目
	private Building building;		// 楼宇
	private House house;		// 房屋
	private Room room;		// 房间
	private User user;		// 销售
	private String contractSource;		// 合同来源
	private String parnter;		// 合作人
	private Double rental;		// 月租金
	private Date startDate;		// 合同生效时间
	private Date expiredDate;		// 合同过期时间
	private Date signDate;		// 合同签订时间
	private String signType;		// 合同签订类型
	private String hasTv;		// 是否开通有线电视
	private Double tvFee;		// 有线电视每月费用
	private String hasNet;		// 是否开通宽带
	private Double netFee;		// 每月宽带费用
	private Double waterFee;		// 合租每月水费
	private Double serviceFee;		// 服务费比例
	private Integer renMonths;		// 首付房租月数
	private Integer depositMonths;		// 房租押金月数
	private Double depositAmount;		// 房租押金金额
	private Double depositElectricAmount;		// 水电押金金额
	private String hasVisa;		// 是否需办理居住证及落户
	private Double meterValue;		// 入住分电表系数
	private Double totalMeterValue;		// 入住总电表系数
	private Double peakMeterValue;		// 入住峰电系数
	private Double flatMeterValue;		// 入住平电系数
	private Double valleyMeterValue;		// 入住谷电系数
	private Double coalValue;		// 入住煤表系数
	private Double waterValue;		// 入住水表系数
	private Double remindTime;		// 续租提醒时间
	private String contractStatus;		// 合同状态
	private String contractBusiStatus;		// 合同业务状态
	
	public RentContract() {
		super();
	}

	public RentContract(String id){
		super(id);
	}

	@Length(min=0, max=64, message="原出租合同长度必须介于 0 和 64 之间")
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
	@Length(min=1, max=100, message="合同名称长度必须介于 1 和 100 之间")
	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	
	@Length(min=1, max=64, message="出租方式长度必须介于 1 和 64 之间")
	public String getRentMode() {
		return rentMode;
	}

	public void setRentMode(String rentMode) {
		this.rentMode = rentMode;
	}
	
	@NotNull(message="物业项目不能为空")
	public PropertyProject getPropertyProject() {
		return propertyProject;
	}

	public void setPropertyProject(PropertyProject propertyProject) {
		this.propertyProject = propertyProject;
	}
	
	@NotNull(message="楼宇不能为空")
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}
	
	@NotNull(message="房屋不能为空")
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}
	
	@NotNull(message="房间不能为空")
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
	
	@Length(min=1, max=64, message="合同来源长度必须介于 1 和 64 之间")
	public String getContractSource() {
		return contractSource;
	}

	public void setContractSource(String contractSource) {
		this.contractSource = contractSource;
	}
	
	@Length(min=0, max=64, message="合作人长度必须介于 0 和 64 之间")
	public String getParnter() {
		return parnter;
	}

	public void setParnter(String parnter) {
		this.parnter = parnter;
	}
	
	@NotNull(message="月租金不能为空")
	public Double getRental() {
		return rental;
	}

	public void setRental(Double rental) {
		this.rental = rental;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="合同生效时间不能为空")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="合同过期时间不能为空")
	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="合同签订时间不能为空")
	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	
	@Length(min=0, max=64, message="合同签订类型长度必须介于 0 和 64 之间")
	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}
	
	@Length(min=0, max=64, message="是否开通有线电视长度必须介于 0 和 64 之间")
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
	
	@Length(min=0, max=64, message="是否开通宽带长度必须介于 0 和 64 之间")
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
	
	@Length(min=0, max=64, message="是否需办理居住证及落户长度必须介于 0 和 64 之间")
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
	
	public Double getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Double remindTime) {
		this.remindTime = remindTime;
	}
	
	@Length(min=0, max=64, message="合同状态长度必须介于 0 和 64 之间")
	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}
	
	@Length(min=0, max=64, message="合同业务状态长度必须介于 0 和 64 之间")
	public String getContractBusiStatus() {
		return contractBusiStatus;
	}

	public void setContractBusiStatus(String contractBusiStatus) {
		this.contractBusiStatus = contractBusiStatus;
	}
	
}