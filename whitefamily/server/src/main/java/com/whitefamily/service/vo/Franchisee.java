package com.whitefamily.service.vo;

public class Franchisee extends WFManager {
	
	private WFShop shop;

	public WFShop getShop() {
		return shop;
	}

	public void setShop(WFShop shop) {
		this.shop = shop;
	}
	
	
	public boolean getFranchiseeType() {
		return true;
	}

}
