package com.whitefamily.po.delivery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.whitefamily.po.Goods;


@Entity
@Table(name = "WF_DELIVER_RECORD_GOODS")
public class DeliveryRecordGoods {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_REC_ID", insertable = true, updatable = true, nullable = false)
	protected DeliveryRecord record;
	
	@Column(name="WF_UNIT_COUNT", columnDefinition="NUMERIC(10,2) default 0")
	protected float count;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_GOOD_ID", insertable = true, updatable = true, nullable = false)
	protected Goods goods;
	
	@Column(name="WF_BRAND_NAME", columnDefinition="VARCHAR(500)")
	protected String brandName;
	
	@Column(name="WF_VENDOR_NAME", columnDefinition="VARCHAR(500)")
	protected String vendorName;
	
	@Column(name="WF_INVEN_PRICE", columnDefinition="NUMERIC(10,2) default 0")
	protected float inventoryPrice;
	
	@Column(name="WF_DE_PRICE", columnDefinition="NUMERIC(10,2) default 0")
	protected float price;
	
	@Column(name="WF_DE_COUNT", columnDefinition="NUMERIC(10,2) default 0")
	protected float deliverCount;
	
	@Column(name="WF_INVENTORY_ID", columnDefinition="NUMERIC(10,0) default 0")
	protected long inventoryId;
	

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

	public DeliveryRecord getRecord() {
		return record;
	}

	public void setRecord(DeliveryRecord record) {
		this.record = record;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDeliverCount() {
		return deliverCount;
	}

	public void setDeliverCount(float deliverCount) {
		this.deliverCount = deliverCount;
	}

	public long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public float getInventoryPrice() {
		return inventoryPrice;
	}

	public void setInventoryPrice(float inventoryPrice) {
		this.inventoryPrice = inventoryPrice;
	}

	
	
  
	
}
