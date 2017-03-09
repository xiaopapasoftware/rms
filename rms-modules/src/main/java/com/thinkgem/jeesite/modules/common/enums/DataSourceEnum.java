package com.thinkgem.jeesite.modules.common.enums;

/**
 * 数据来源类型
 * 
 * @author wangshujin
 */
public enum DataSourceEnum {

    /**
     * 管理系统
     */
    BACK_SYSTEM("1"),

    /**
     * APP
     */
    FRONT_APP("2");

    DataSourceEnum(String value) {
	this.value = value;
    }

    private String value;

    public String getValue() {
	return value;
    }

}
