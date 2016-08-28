package com.thinkgem.jeesite.modules.common.enums;

/**
 * 
 * 逻辑删除标识
 * 
 * @author wangshujin
 *
 */
public enum ActFlagEnum {

    /**
     * 正常
     */
    NORMAL("0"),

    /**
     * 删除
     */
    DELETED("1"),

    /**
     * 审核
     */
    AUDIT("2");

    ActFlagEnum(String value) {
	this.value = value;
    }

    private String value;

    public String getValue() {
	return value;
    }

}
