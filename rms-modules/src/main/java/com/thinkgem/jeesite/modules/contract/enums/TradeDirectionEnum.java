package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 交易款方向
 * 
 * @author wangshujin
 */
public enum TradeDirectionEnum {

  /**
   * 应出
   */
  OUT("0"),

  /**
   * 应收
   */
  IN("1");

  TradeDirectionEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
