package com.thinkgem.jeesite.modules.app.enums;

/**
 * 房源上下架
 * 
 * @author xiao
 */
public enum UpEnum {

  /**
   * 下架
   */
  DOWN(0),

  /**
   * 上架
   */
  UP(1);

  UpEnum(Integer value) {
    this.value = value;
  }

  private Integer value;

  public Integer getValue() {
    return value;
  }

}
