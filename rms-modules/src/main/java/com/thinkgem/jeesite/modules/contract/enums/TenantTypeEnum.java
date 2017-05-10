package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 租客类型
 * 
 * @author wangshujin
 */
public enum TenantTypeEnum {


  /**
   * 企业租客
   */
  AGENCY("1"),


  /**
   * 个人租客
   */
  PERSONAL("0");

  TenantTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }


}
