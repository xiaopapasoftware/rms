package com.thinkgem.jeesite.modules.inventory.enums;

/**
 * 房屋状态
 * 
 * @author wangshujin
 */
public enum HouseStatusEnum {

  /** 待装修 */
  TO_RENOVATION("0"),

  /** 待出租可预订 */
  RENT_FOR_RESERVE("1"),

  /** 已预定 */
  BE_RESERVED("2"),

  /** 部分出租 */
  PART_RENT("3"),

  /** 完全出租 */
  WHOLE_RENT("4");

//  /** 已退待租 */
//  RETURN_FOR_RENT("5"),
//
//  /** 已损坏 */
//  DAMAGED("6");

  HouseStatusEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
