package com.thinkgem.jeesite.modules.common.enums;

/**
 * 证件类型
 * 
 * @author wangshujin
 */
public enum CertTypeEnum {

  /**
   * 身份证
   */
  CERT_ID("0"),

  /**
   * 台胞证
   */
  TAI_BAO("1"),

  /**
   * 港澳通行证
   */
  GANG_AO("2"),

  /**
   * 护照
   */
  HU_ZHAO("3");

  CertTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }


}
