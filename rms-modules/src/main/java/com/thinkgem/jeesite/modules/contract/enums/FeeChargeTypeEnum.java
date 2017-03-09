package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 计费方式
 * 
 * @author wangshujin
 */
public enum FeeChargeTypeEnum {

  /**
   * 预付费
   */
  PRE_CHARGE("0"),


  /**
   * 后付费
   */
  LATE_CHARGE("1");

  FeeChargeTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
