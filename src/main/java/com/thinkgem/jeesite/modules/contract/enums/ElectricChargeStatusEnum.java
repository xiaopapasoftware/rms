
package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 
 * 智能电表充值状态
 * 
 * @author wangshujin
 *
 */
public enum ElectricChargeStatusEnum {

  /**
   * 充值中
   */
  PROCESSING("0"),

  /**
   * 充值成功
   */
  SUCCESSED("1"),

  /**
   * 充值失败
   */
  FAILED("2");


  ElectricChargeStatusEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
