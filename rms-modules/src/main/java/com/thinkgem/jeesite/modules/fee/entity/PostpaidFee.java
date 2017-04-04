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
  private String paymentTransId;// 款项ID
  private Date payDate; // 付费时间
  private Double payAmount; // 付费金额
  private String payStatus;// 付费状态,0=付费中,1=付费成功,2=付费失败
  private String settleStatus;// 结算状态，0=待结算；1=结算待审核；2=审核拒绝；3=审核通过
  private String publicFeeType;// 电费=11， 水费=14，电视费=18，宽带费=20
  private String remarks;// 备注
  private Date startDate;// 专用来查询使用的选择的开始时间
  private Date endDate;// 专用来查询使用的选择的结束时间
  private String contractName;// 专用来查询使用的合同名变量

  public String getRentContractId() {
    return rentContractId;
  }

  public void setRentContractId(String rentContractId) {
    this.rentContractId = rentContractId;
  }

  public String getPaymentTransId() {
    return paymentTransId;
  }

  public void setPaymentTransId(String paymentTransId) {
    this.paymentTransId = paymentTransId;
  }

  public Date getPayDate() {
    return payDate;
  }

  public void setPayDate(Date payDate) {
    this.payDate = payDate;
  }

  public Double getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(Double payAmount) {
    this.payAmount = payAmount;
  }

  public String getPayStatus() {
    return payStatus;
  }

  public void setPayStatus(String payStatus) {
    this.payStatus = payStatus;
  }

  public String getSettleStatus() {
    return settleStatus;
  }

  public void setSettleStatus(String settleStatus) {
    this.settleStatus = settleStatus;
  }

  public String getPublicFeeType() {
    return publicFeeType;
  }

  public void setPublicFeeType(String publicFeeType) {
    this.publicFeeType = publicFeeType;
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
