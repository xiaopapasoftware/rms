package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 合同签订类型
 * 
 * @author wangshujin
 */
public enum ContractSignTypeEnum {

  /**
   * 新签
   */
  NEW_SIGN("0"),

  /**
   * 正常续签
   */
  RENEW_SIGN("1");

  ContractSignTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
