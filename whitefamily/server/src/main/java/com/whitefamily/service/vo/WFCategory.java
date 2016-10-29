package com.whitefamily.service.vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.whitefamily.po.Category;

public class WFCategory extends Category {
	
	private WFCategory parent;
	
	private List<WFCategory> subs;
	
	private String abbr;
	
	public WFCategory() {
		super();
	}
	

	public WFCategory(Category c) {
		super();
		this.setId(c.getId());
		this.setLevel(c.getLevel());
		this.setParentId(c.getParentId());
		this.setName(c.getName());
		this.setType(c.getType());
		this.setAbbr(PinyinHelper.getShortPinyin(this.name));
		this.setOrder(c.getOrder());
	}

	public WFCategory getParent() {
		return parent;
	}

	public void setParent(WFCategory parent) {
		this.parent = parent;
		this.setParentId(this.parent == null ? 0 : this.parent.getId());
		this.setLevel(parent == null ? 0 : parent.getLevel() + 1);
	}
	
	


	public String getAbbr() {
		return abbr;
	}


	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}


	public List<WFCategory> getSubs() {
		return subs;
	}


	public void setSubs(List<WFCategory> subs) {
		this.subs = subs;
	}
	
	
	public void addSubCategory(WFCategory wfc) {
		if (this.subs == null) {
			this.subs = new ArrayList<WFCategory>(10);
		}
		this.subs.add(wfc);
	}
	
	
	
	public void removeSubCategory(WFCategory wfc) {
		if (this.subs == null) {
			return;
		}
		Iterator<WFCategory> it = subs.iterator();
		while (it.hasNext()) {
			if (it.next().getId() == wfc.getId()) {
				it.remove();
				break;
			}
		}
	}
	
	
	
}
