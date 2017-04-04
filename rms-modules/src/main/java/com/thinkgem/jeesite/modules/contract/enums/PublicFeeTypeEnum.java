package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 公共事业费类型
 * 
 * @author wangshujin
 */
public enum PublicFeeTypeEnum {

  /**
   * 自用电费
   */
  ELECT_SELF_AMOUNT("11"),

  /**
   * 分摊电费
   */
  ELECT_SHARE_AMOUNT("12"),

  /**
   * 水费
   */
  WATER_AMOUNT("14"),

  /**
   * 燃气费
   */
  GAS_AMOUNT("16"),

  /**
   * 有线电视费
   */
  TV_AMOUNT("18"),

  /**
   * 宽带费
   */
  NET_AMOUNT("20"),

  /**
   * 服务费
   */
  SERVICE_AMOUNT("22");

  PublicFeeTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }



}
