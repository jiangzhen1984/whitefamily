package com.whitefamily.service.vo;

import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.whitefamily.po.Brand;

public class WFBrand extends Brand {
	
	private String abbr;

	public WFBrand() {
		super();
	}

	public WFBrand(Brand br) {
		super();
		this.setId(br.getId());
		this.setName(br.getName());
		this.setStyle(br.getStyle());
		this.setUnit(br.getUnit());
		if (this.name != null) {
			this.abbr =  PinyinHelper.getShortPinyin(this.name);
		}
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
