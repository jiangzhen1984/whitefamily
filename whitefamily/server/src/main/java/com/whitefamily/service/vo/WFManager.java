package com.whitefamily.service.vo;

public class WFManager extends WFUser {
	
	protected WFShop shop;

	public WFShop getShop() {
		return shop;
	}

	public void setShop(WFShop shop) {
		this.shop = shop;
	}
	
	
	public long getShopId() {
		return shop.getId();
	}

}
