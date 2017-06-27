package com.thinkgem.jeesite.modules.fee.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 公共事业费后付费实体对象
 * 
 * @author wangshujin
 */
public class PostpaidFee extends DataEntity<PostpaidFee> {
  private static final long serialVersionUID = -8735779649332180454L;
  private String rentContractId; // 出租合同
  private Date payDate; // 付费时间
  private String payStatus;// 1=到账收据待登记;4=到账收据待审核;5=到账收据审核拒绝;6=到账收据审核通过
  private Double electricSelfAmt;// 电费自用金额
  private Date electricSelfAmtStartDate;
  private Date electricSelfAmtEndDate;
  private Double electricShareAmt;
  private Date electricShareAmtStartDate;
  private Date electricShareAmtEndDate;
  private Double waterAmt;
  private Date waterAmtStartDate;
  private Date waterAmtEndDate;
  private Double gasAmt;
  private Date gasAmtStartDate;
  private Date gasAmtEndDate;
  private Double tvAmt;
  private Date tvAmtStartDate;
  private Date tvAmtEndDate;
  private Double netAmt;
  private Date netAmtStartDate;
  private Date netAmtEndDate;
  private Double serviceAmt;
  private Date serviceAmtStartDate;
  private Date serviceAmtEndDate;
  private String remarks;// 备注
  private Date startDate;// 专用来查询使用的选择的开始时间
  private Date endDate;// 专用来查询使用的选择的结束时间
  private String contractName;// 专用来查询使用的合同名变量
  private String contractCode;// 专用来查询使用的合同名变量


  public Double getElectricSelfAmt() {
    return electricSelfAmt;
  }

  public void setElectricSelfAmt(Double electricSelfAmt) {
    this.electricSelfAmt = electricSelfAmt;
  }

  public Date getElectricSelfAmtStartDate() {
    return electricSelfAmtStartDate;
  }

  public void setElectricSelfAmtStartDate(Date electricSelfAmtStartDate) {
    this.electricSelfAmtStartDate = electricSelfAmtStartDate;
  }

  public Date getElectricSelfAmtEndDate() {
    return electricSelfAmtEndDate;
  }

  public void setElectricSelfAmtEndDate(Date electricSelfAmtEndDate) {
    this.electricSelfAmtEndDate = electricSelfAmtEndDate;
  }

  public Double getElectricShareAmt() {
    return electricShareAmt;
  }

  public void setElectricShareAmt(Double electricShareAmt) {
    this.electricShareAmt = electricShareAmt;
  }

  public Double getWaterAmt() {
    return waterAmt;
  }

  public void setWaterAmt(Double waterAmt) {
    this.waterAmt = waterAmt;
  }

  public Double getGasAmt() {
    return gasAmt;
  }

  public void setGasAmt(Double gasAmt) {
    this.gasAmt = gasAmt;
  }

  public Double getTvAmt() {
    return tvAmt;
  }

  public void setTvAmt(Double tvAmt) {
    this.tvAmt = tvAmt;
  }

  public Double getNetAmt() {
    return netAmt;
  }

  public void setNetAmt(Double netAmt) {
    this.netAmt = netAmt;
  }

  public Double getServiceAmt() {
    return serviceAmt;
  }

  public void setServiceAmt(Double serviceAmt) {
    this.serviceAmt = serviceAmt;
  }

  public String getRentContractId() {
    return rentContractId;
  }

  public void setRentContractId(String rentContractId) {
    this.rentContractId = rentContractId;
  }

  public Date getPayDate() {
    return payDate;
  }

  public void setPayDate(Date payDate) {
    this.payDate = payDate;
  }

  public String getPayStatus() {
    return payStatus;
  }

  public void setPayStatus(String payStatus) {
    this.payStatus = payStatus;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
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

  public String getContractName() {
    return contractName;
  }

  public void setContractName(String contractName) {
    this.contractName = contractName;
  }

  public PostpaidFee() {
    super();
  }

  public PostpaidFee(String id) {
    super(id);
  }

  public Date getElectricShareAmtStartDate() {
    return electricShareAmtStartDate;
  }

  public void setElectricShareAmtStartDate(Date electricShareAmtStartDate) {
    this.electricShareAmtStartDate = electricShareAmtStartDate;
  }

  public Date getElectricShareAmtEndDate() {
    return electricShareAmtEndDate;
  }

  public void setElectricShareAmtEndDate(Date electricShareAmtEndDate) {
    this.electricShareAmtEndDate = electricShareAmtEndDate;
  }

  public Date getWaterAmtStartDate() {
    return waterAmtStartDate;
  }

  public void setWaterAmtStartDate(Date waterAmtStartDate) {
    this.waterAmtStartDate = waterAmtStartDate;
  }

  public Date getWaterAmtEndDate() {
    return waterAmtEndDate;
  }

  public void setWaterAmtEndDate(Date waterAmtEndDate) {
    this.waterAmtEndDate = waterAmtEndDate;
  }

  public Date getGasAmtStartDate() {
    return gasAmtStartDate;
  }

  public void setGasAmtStartDate(Date gasAmtStartDate) {
    this.gasAmtStartDate = gasAmtStartDate;
  }

  public Date getGasAmtEndDate() {
    return gasAmtEndDate;
  }

  public void setGasAmtEndDate(Date gasAmtEndDate) {
    this.gasAmtEndDate = gasAmtEndDate;
  }

  public Date getTvAmtStartDate() {
    return tvAmtStartDate;
  }

  public void setTvAmtStartDate(Date tvAmtStartDate) {
    this.tvAmtStartDate = tvAmtStartDate;
  }

  public Date getTvAmtEndDate() {
    return tvAmtEndDate;
  }

  public void setTvAmtEndDate(Date tvAmtEndDate) {
    this.tvAmtEndDate = tvAmtEndDate;
  }

  public Date getNetAmtStartDate() {
    return netAmtStartDate;
  }

  public void setNetAmtStartDate(Date netAmtStartDate) {
    this.netAmtStartDate = netAmtStartDate;
  }

  public Date getNetAmtEndDate() {
    return netAmtEndDate;
  }

  public void setNetAmtEndDate(Date netAmtEndDate) {
    this.netAmtEndDate = netAmtEndDate;
  }

  public Date getServiceAmtStartDate() {
    return serviceAmtStartDate;
  }

  public void setServiceAmtStartDate(Date serviceAmtStartDate) {
    this.serviceAmtStartDate = serviceAmtStartDate;
  }

  public Date getServiceAmtEndDate() {
    return serviceAmtEndDate;
  }

  public void setServiceAmtEndDate(Date serviceAmtEndDate) {
    this.serviceAmtEndDate = serviceAmtEndDate;
  }

  public String getContractCode() {
    return contractCode;
  }

  public void setContractCode(String contractCode) {
    this.contractCode = contractCode;
  }

}
