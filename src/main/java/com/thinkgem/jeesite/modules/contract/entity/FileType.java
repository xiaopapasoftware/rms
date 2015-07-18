package com.thinkgem.jeesite.modules.contract.entity;

public enum FileType {
	OWNER_ID("业主身份证", "0"),

	CERTIFICATE_FILE("委托证明", "1"),

	HOUSE_CERTIFICATE("业主房产证", "2"),

	HOUSE_AGREEMENT_CERTIFICATE("动迁协议", "3"),

	TENANT_ID("租客身份证", "4"),

	RECEIPT("收据凭证", "5"),

	LEASE_CONTRACT_TEMPLATE("承租合同模板", "6"),

	RENT_CONTRACT_TEMPLATE("出租合同模板", "7"),

	LEAVE_RECORD("请假单", "8"),

	BROCHURE("宣传资料", "9"),

	PROJECT_CHART("物业项目图", "10"),

	BUILDING_MAP("楼宇图", "11"),

	HOUSE_MAP("房屋图", "12"),

	ROOM_MAP("房间图", "13"),

	DEPOSITAGREEMENT_FILE("定金协议", "14"),

	DEPOSITRECEIPT_FILE("定金协议收据", "15"),

	RENTCONTRACT_FILE("出租合同", "16"),

	RENTCONTRACTRECEIPT_FILE("出租合同收据", "17");
	
	private String name;
	private String value;

	private FileType(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
