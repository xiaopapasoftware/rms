package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 合同来源
 * 
 * @author wangshujin
 */
public enum ContractSourceEnum {

  /**
   * 合作
   */
  PARTENER("0"),


  /**
   * 本部
   */
  SELF("1");

  ContractSourceEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }


}
