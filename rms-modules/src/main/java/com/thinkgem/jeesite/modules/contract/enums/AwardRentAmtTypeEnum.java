package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 是否促销返租合同
 */
public enum AwardRentAmtTypeEnum {

  Y("1"),

  N("0");

  AwardRentAmtTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
