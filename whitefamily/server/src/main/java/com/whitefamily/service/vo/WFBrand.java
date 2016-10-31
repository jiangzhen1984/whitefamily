package com.whitefamily.service.vo;

import com.whitefamily.po.Brand;

public class WFBrand extends Brand {

	public WFBrand() {
		super();
	}

	public WFBrand(Brand br) {
		super();
		this.setId(br.getId());
		this.setName(br.getName());
		this.setStyle(br.getStyle());
		this.setUnit(br.getUnit());
	}

	
}
