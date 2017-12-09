package com.thinkgem.jeesite.modules.report.entity;

/**
 * 
 * 费用类型标识
 * 
 * @author xiao
 *
 */
public enum FeeReportTypeEnum {

    /**
     * 电费
     */
    ELECTRICITY("0");

    FeeReportTypeEnum(String value) {
	this.value = value;
    }

    private String value;

    public String getValue() {
	return value;
    }

}
