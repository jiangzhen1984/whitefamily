package com.whitefamily.po.customer;

public enum Role {

	ADMIN,
	MANAGER,
	DELIVER_STAFF,
	MARKETING,
	INVENTORY_STAFF;
	
	public String getValue() {
		switch (this) {
		case ADMIN:
			return "管理员";
		case DELIVER_STAFF:
			return "配送";
		case INVENTORY_STAFF:
			return "库存管理";
		case MANAGER:
			return "店长";
		case MARKETING:
			return "市场分析";
		default:
			return "未知";
		}
	}
}
