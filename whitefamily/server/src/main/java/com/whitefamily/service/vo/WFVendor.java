package com.whitefamily.service.vo;

import com.whitefamily.po.Vendor;

public class WFVendor extends Vendor {

	public WFVendor(Vendor v) {
		this.setId(v.getId());
		this.setName(v.getName());
	}
	
	public WFVendor() {
	}
	

}
