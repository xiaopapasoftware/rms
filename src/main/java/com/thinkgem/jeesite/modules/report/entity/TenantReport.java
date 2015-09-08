package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.person.entity.Partner;
import com.thinkgem.jeesite.modules.sys.entity.User;

public class TenantReport extends DataEntity<TenantReport> {
	private String agreementId; // 原定金协议
	private String contractId; // 原出租合同
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

	private String tenant;//承租人
	private String tenantCellPhone;
	private String live;//入住人
	private String liveCellPhone;
	
	private String name;
	
	public String getAgreementId() {
		return agreementId;
	}
	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getRentMode() {
		return rentMode;
	}
	public void setRentMode(String rentMode) {
		this.rentMode = rentMode;
	}
	public PropertyProject getPropertyProject() {
		return propertyProject;
	}
	public void setPropertyProject(PropertyProject propertyProject) {
		this.propertyProject = propertyProject;
	}
	public Building getBuilding() {
		return building;
	}
	public void setBuilding(Building building) {
		this.building = building;
	}
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
	public Double getRental() {
		return rental;
	}
	public void setRental(Double rental) {
		this.rental = rental;
	}
	@ExcelField(title="出租合同开始日期", align=2, sort=8)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@ExcelField(title="出租合同结束日期", align=2, sort=9)
	public Date getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
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
	public String getContractStatus() {
		return contractStatus;
	}
	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}
	public String getContractBusiStatus() {
		return contractBusiStatus;
	}
	public void setContractBusiStatus(String contractBusiStatus) {
		this.contractBusiStatus = contractBusiStatus;
	}
	@ExcelField(title="物业项目名称", align=2, sort=1)
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@ExcelField(title="楼宇", align=2, sort=2)
	public String getBuildingBame() {
		return buildingBame;
	}
	public void setBuildingBame(String buildingBame) {
		this.buildingBame = buildingBame;
	}
	@ExcelField(title="房屋", align=2, sort=3)
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
	@ExcelField(title="承租人姓名", align=2, sort=4)
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	@ExcelField(title="入住人", align=2, sort=6)
	public String getLive() {
		return live;
	}
	public void setLive(String live) {
		this.live = live;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="承租人手机号码", align=2, sort=5)
	public String getTenantCellPhone() {
		return tenantCellPhone;
	}
	public void setTenantCellPhone(String tenantCellPhone) {
		this.tenantCellPhone = tenantCellPhone;
	}
	@ExcelField(title="入住人手机号码", align=2, sort=7)
	public String getLiveCellPhone() {
		return liveCellPhone;
	}
	public void setLiveCellPhone(String liveCellPhone) {
		this.liveCellPhone = liveCellPhone;
	}
}