package com.thinkgem.jeesite.modules.funds.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * fundsEntity
 * 
 * @author funds
 * @version 2017-10-21
 */
public class PaymenttransDtl extends DataEntity<PaymenttransDtl> {

  private static final long serialVersionUID = 1L;
  private String transId; // 款项交易ID
  private Date startDate; // 开始时间
  private Date expiredDate; // 结束时间
  private Date actDate;
  private Double amount; // 所属金额
  private List<String> transIdList;

  public PaymenttransDtl() {
    super();
  }

  public PaymenttransDtl(String id) {
    super(id);
  }

  @Length(min = 0, max = 64, message = "款项交易ID长度必须介于 0 和 64 之间")
  public String getTransId() {
    return transId;
  }

  public void setTransId(String transId) {
    this.transId = transId;
  }

  @JsonFormat(pattern = "yyyy-MM-dd")
  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @JsonFormat(pattern = "yyyy-MM-dd")
  public Date getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Date expiredDate) {
    this.expiredDate = expiredDate;
  }

  @JsonFormat(pattern = "yyyy-MM-dd")
  public Date getActDate() {
    return actDate;
  }

  public void setActDate(Date actDate) {
    this.actDate = actDate;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public List<String> getTransIdList() {
    return transIdList;
  }

  public void setTransIdList(List<String> transIdList) {
    this.transIdList = transIdList;
  }

}
