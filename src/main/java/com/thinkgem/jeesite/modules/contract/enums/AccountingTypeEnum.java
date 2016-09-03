package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 核算类型
 * 
 * @author wangshujin
 *
 */
public enum AccountingTypeEnum {

  /**
   * 提前退租核算
   */
  ADVANCE_RETURN_ACCOUNT("0"),

  /**
   * 正常退租核算
   */
  NORMAL_RETURN_ACCOUNT("1"),

  /**
   * 逾期退租核算
   */
  LATE_RETURN_ACCOUNT("2"),

  /**
   * 特殊退租核算
   */
  SPECIAL_RETURN_ACCOUNT("3");

  AccountingTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
