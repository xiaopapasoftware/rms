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
  private Double electricShareAmt;
  private Double waterAmt;
  private Double gasAmt;
  private Double tvAmt;
  private Double netAmt;
  private Double serviceAmt;
  private String remarks;// 备注
  private Date startDate;// 专用来查询使用的选择的开始时间
  private Date endDate;// 专用来查询使用的选择的结束时间
  private String contractName;// 专用来查询使用的合同名变量

  public Double getElectricSelfAmt() {
    return electricSelfAmt;
  }

  public void setElectricSelfAmt(Double electricSelfAmt) {
    this.electricSelfAmt = electricSelfAmt;
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
}
