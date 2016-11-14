package com.whitefamily.po.customer;

public enum Role {

	ADMIN,
	MANAGER,
	DELIVER_STAFF,
	MARKETING,
	INVENTORY_STAFF,
	VEGETABLE_SUPPLIER,
	FRANCHISEE;
	
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
		case VEGETABLE_SUPPLIER:
			return "菜商";
		case FRANCHISEE:
			return "加盟商";
		default:
			return "未知";
		}
	}
	
	public String getStrValue() {
		return this.ordinal()+"";
	}
}
