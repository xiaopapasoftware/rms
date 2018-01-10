package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 学历
 */
public enum DegreesTypeEnum {

  /**
   * 中专
   */
  ZZ("0"),

  /**
   * 大专
   */
  DZ("1"),

  /**
   * 本科
   */
  BK("2"),

  /**
   * 硕士研究生
   */
  SS("3"),

  /**
   * 博士
   */
  BS("4");

  DegreesTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
