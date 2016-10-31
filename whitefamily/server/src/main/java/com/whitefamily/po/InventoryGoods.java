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
	
	@Column(name = "WF_BRAND_ID")
	protected Long  brandId;
	
	@Column(name = "WF_BRAND_NAME" , columnDefinition="VARCHAR(500)")
	protected String  brandName;
	
	
	@Column(name = "WF_VENDOR_ID")
	protected Long  vendorId;
	
	@Column(name = "WF_VENDOR_NAME" , columnDefinition="VARCHAR(500)")
	protected String  vendorName;
	
	@Column(name="WF_UNIT_REM_COUNT", columnDefinition="NUMERIC(10,2) default 0")
	protected float remCount;
	
	@Column(name="WF_RATE", columnDefinition="NUMERIC(3) default 0")
	protected float rate;
	
	@Column(name="WF_RATE1", columnDefinition="NUMERIC(3) default 0")
	protected float rate1;
	
	@Column(name="WF_RATE2", columnDefinition="NUMERIC(3) default 0")
	protected float rate2;

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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	
	

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public float getRemCount() {
		return remCount;
	}

	public void setRemCount(float remCount) {
		this.remCount = remCount;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public float getRate1() {
		return rate1;
	}

	public void setRate1(float rate1) {
		this.rate1 = rate1;
	}

	public float getRate2() {
		return rate2;
	}

	public void setRate2(float rate2) {
		this.rate2 = rate2;
	}
	
	
	
	
	
	
}
