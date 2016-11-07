package com.whitefamily.service.vo;

import java.util.Date;

public class WFInventoryGoods {

	float count;

	float price;
	
	float remain;

	WFGoods goods;

	WFBrand brand;
	
	WFVendor vendor;
	
	Date date;

	public float getCount() {
		return count;
	}

	public void setCount(float count) {
		this.count = count;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getRemain() {
		return remain;
	}

	public void setRemain(float remain) {
		this.remain = remain;
	}

	public WFGoods getGoods() {
		return goods;
	}

	public void setGoods(WFGoods goods) {
		this.goods = goods;
	}

	public WFBrand getBrand() {
		return brand;
	}

	public void setBrand(WFBrand brand) {
		this.brand = brand;
	}

	public WFVendor getVendor() {
		return vendor;
	}

	public void setVendor(WFVendor vendor) {
		this.vendor = vendor;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	

}
