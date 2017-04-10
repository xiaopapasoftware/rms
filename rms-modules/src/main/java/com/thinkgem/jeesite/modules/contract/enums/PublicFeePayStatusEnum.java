package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 公共事业费付费状态
 * 
 * @author wangshujin
 */
public enum PublicFeePayStatusEnum {

  /**
   * 到账收据待登记
   */
  TO_SIGN("1"),

  /**
   * 到账收据待审核
   */
  TO_AUDIT("4"),

  /**
   * 到账收据审核拒绝
   */
  AUDITED_REFUSE("5"),

  /**
   * 到账收据审核通过
   */
  AUDITED_PASS("6");

  PublicFeePayStatusEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
