package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 租赁类型
 * 
 * @author wangshujin
 */
public enum RentModelTypeEnum {

    /**
     * 整套整租
     */
    WHOLE_RENT("0"),

    /**
     * 单间合租
     */
    JOINT_RENT("1");

    RentModelTypeEnum(String value) {
	this.value = value;
    }

    private String value;

    public String getValue() {
	return value;
    }

}