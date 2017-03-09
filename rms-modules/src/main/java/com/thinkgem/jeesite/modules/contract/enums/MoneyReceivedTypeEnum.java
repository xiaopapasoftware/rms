package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 收款人类型
 * 
 * @author wangshujin
 */
public enum MoneyReceivedTypeEnum {


  /**
   * 单位
   */
  AGENCY("0"),


  /**
   * 个人
   */
  PERSONAL("1");

  MoneyReceivedTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }


}
