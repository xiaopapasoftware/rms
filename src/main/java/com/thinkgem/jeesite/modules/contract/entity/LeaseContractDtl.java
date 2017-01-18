/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 承租合同明细Entity
 * 
 * @author huangsc
 * @version 2015-06-14
 */
public class LeaseContractDtl extends DataEntity<LeaseContractDtl> {

  private static final long serialVersionUID = 1L;
  private String leaseContractId; // 承租合同
  private Date startDate; // 起始时间
  private Date endDate; // 结束时间
  private Double deposit; // 月承租价

  @SuppressWarnings("unused")
  private String startDateStr;
  @SuppressWarnings("unused")
  private String endDateStr;

  public LeaseContractDtl() {
    super();
  }

  public LeaseContractDtl(String id) {
    super(id);
  }

  @Length(min = 0, max = 64, message = "承租合同长度必须介于 0 和 64 之间")
  public String getLeaseContractId() {
    return leaseContractId;
  }

  public void setLeaseContractId(String leaseContractId) {
    this.leaseContractId = leaseContractId;
  }

  @JsonFormat(pattern = "yyyy-MM-dd")
  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @JsonFormat(pattern = "yyyy-MM-dd")
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Double getDeposit() {
    return deposit;
  }

  public void setDeposit(Double deposit) {
    this.deposit = deposit;
  }

  public String getStartDateStr() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    if (this.startDate != null) {
      return sdf.format(this.startDate);
    } else {
      return "";
    }
  }

  public String getEndDateStr() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    if (this.endDate != null) {
      return sdf.format(this.endDate);
    } else {
      return "";
    }
  }

}
