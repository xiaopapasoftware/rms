/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * APP用户Entity
 * 
 * @author xiao
 * @version 2018-01-14
 */
public class CustBindInfo extends DataEntity<CustBindInfo> {

  private static final long serialVersionUID = 1L;
  private String accountType;
  private String account;
  private String password;
  private String valid;
  private String customerId;

  public CustBindInfo() {
    super();
  }

  public CustBindInfo(String id) {
    super(id);
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getValid() {
    return valid;
  }

  public void setValid(String valid) {
    this.valid = valid;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
}
