package com.whitefamily.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "WF_INVENTORY_GOODS")
public class InventoryGoods {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_REC_ID", insertable = true, updatable = true, nullable = false)
	protected InventoryUpdateRecord record;
	
	@Column(name="WF_UNIT_COUNT", columnDefinition="NUMERIC(10,2) default 0")
	protected float count;
	
	@Column(name="WF_COUNT", columnDefinition="NUMERIC(10,2) default 0")
	protected float price;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_GOOD_ID", insertable = true, updatable = true, nullable = false)
	protected Goods goods;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_BRAND_ID", insertable = true, updatable = true, nullable = true)
	protected Brand  brand;
	
	
	
	


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getCount() {
		return count;
	}

	public void setCount(float count) {
		this.count = count;
	}


	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	
	
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public InventoryUpdateRecord getRecord() {
		return record;
	}

	public void setRecord(InventoryUpdateRecord record) {
		this.record = record;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	
	
	
}
