package com.whitefamily.po;

public enum VisibilityType {

	SHOP_MANAGER_VISIBLE, STOCK_MANGER_VISIBLE;

	public String getViewValue() {
		switch (this) {
		case SHOP_MANAGER_VISIBLE:
			return "店长可见";
		case STOCK_MANGER_VISIBLE:
			return "库存管理人员可见";
		default:
			return "未知";
		}
	}
}
