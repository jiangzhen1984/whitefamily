package com.whitefamily.service.vo;

import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.whitefamily.po.Vendor;

public class WFVendor extends Vendor {
	
	
	private String abbr;

	public WFVendor(Vendor v) {
		this.setId(v.getId());
		this.setName(v.getName());
		if (this.name != null) {
			this.abbr =  PinyinHelper.getShortPinyin(this.name);
		}
	}
	
	public WFVendor() {
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		if (this.name != null) {
			this.abbr =  PinyinHelper.getShortPinyin(this.name);
		}
	}
	

	
}
