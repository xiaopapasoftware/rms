package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 租赁付费方式
 * 
 * @author wangshujin
 */
public enum RentPayTypeEnum {

  /**
   * 付三押一
   */
  PAY_3_DEPOSIT_3("0"),

  /**
   * 付二押二
   */
  PAY_2_DEPOSIT_2("1");

  RentPayTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }
}
