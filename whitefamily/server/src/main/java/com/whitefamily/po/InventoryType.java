package com.whitefamily.po;

public enum InventoryType {
	
	IN,
	OUT;
	

	
	public String getValue() {
		return this == IN ? "入库" : "出库";
	}
}
