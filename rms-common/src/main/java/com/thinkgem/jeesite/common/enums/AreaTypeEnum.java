package com.thinkgem.jeesite.common.enums;

/**
 * 区域类型
 */
public enum AreaTypeEnum {

  /**
   * 公司
   */
  COMPANY("1"),

  /**
   * 省份、直辖市
   */
  PROVINCE("2"),

  /**
   * 地市
   */
  CITY("3"),

  /**
   * 区县
   */
  COUNTY("4"),

  /**
   * 服务中心
   */
  CENTER("5"),

  /**
   * 营运区域
   */
  AREA("6");

  AreaTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
