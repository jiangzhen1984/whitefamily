package com.whitefamily.service.vo;

import com.whitefamily.po.Shop;

	
public class WFShop extends Shop {
	

	
	public WFShop() {
		super();
	}

	public WFShop(Shop s) {
		if (s == null) {
			throw new RuntimeException(" shop is null ");
		}
		this.id = s.getId();
		this.address = s.getAddress();
		this.name = s.getName();
		this.type = s.getType();
	}

	
}
