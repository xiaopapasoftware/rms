package com.thinkgem.jeesite.modules.inventory.enums;

/**
 * 房间状态
 * 
 * @author wangshujin
 *
 */
public enum RoomStatusEnum {

  /** 待装修 */
  TO_RENOVATION("0"),

  /** 待出租可预订 */
  RENT_FOR_RESERVE("1"),

  /** 已预定 */
  BE_RESERVED("2"),

  /** 已出租 */
  RENTED("3");
  
  RoomStatusEnum(String value) {
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
