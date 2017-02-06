package com.whitefamily.service.vo;

import java.util.Date;

public class WFGoodsStatistic {
	
	
	private WFGoods goods;
	
	private float in;
	
	private float out;
	
	private float stock;
	
	private Date date;

	public WFGoodsStatistic(WFGoods goods) {
		super();
		this.goods = goods;
	}
	
	
	public void addIn(float in) {
		this.in += in;
	}
	
	public void addOut(float out) {
		this.out += out;
	}

	public float getIn() {
		return in;
	}

	public void setIn(float in) {
		this.in = in;
	}

	public float getOut() {
		return out;
	}

	public void setOut(float out) {
		this.out = out;
	}

	public float getStock() {
		return stock;
	}

	public void setStock(float stock) {
		this.stock = stock;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	public WFGoods getGoods() {
		return goods;
	}
	
	
	

}
