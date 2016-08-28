package com.thinkgem.jeesite.modules.common.enums;

/**
 * 操作标识
 * 
 * @author wangshujin
 *
 */
public enum ValidatorFlagEnum {
    /**
     * 保存
     */
    SAVE("1"),

    /**
     * 暂存
     */
    TEMP_SAVE("0");

    ValidatorFlagEnum(String value) {
	this.value = value;
    }

    private String value;

    public String getValue() {
	return value;
    }

}