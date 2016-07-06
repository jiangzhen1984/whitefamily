package com.whitefamily.po;

public enum InventoryStatus {

	REQUEST,
	PREPARING_INVENTORY,
	DELIVERING,
	DELIVERYED;
	
	
	public String getValue() {
		switch (this) {
		case DELIVERING:
			return "派送中";
		case DELIVERYED:
			return "已派送";
		case PREPARING_INVENTORY:
			return "备货中";
		case REQUEST:
			return "已提交";
		default:
			return "未知";
		
		}
	}
}
