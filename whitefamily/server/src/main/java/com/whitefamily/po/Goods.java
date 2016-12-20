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

import com.github.stuxuhai.jpinyin.PinyinHelper;

@Entity
@Table(name = "WF_GOODS")
public class Goods {

	public Goods() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

	@Column(name = "WF_GOODS_NAME", columnDefinition = "VARCHAR(500)")
	protected String name;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_GOOD_CATE_ID", insertable = true, updatable = true, nullable = false)
	protected Category cate;
	
	@Column(name = "WF_CTE_TYPE", columnDefinition = "tinyint default 0 ")
	protected int type;
	
	@Column(name = "WF_IMAGE_URI", columnDefinition = "VARCHAR(500)")
	protected String imageContextName;
	
	@Column(name = "WF_GOODS_UNIT", columnDefinition = "VARCHAR(100)")
	protected String unit;
	
	@Column(name = "WF_CTE_DESC", columnDefinition = "TEXT")
	protected String goodsDesc;
	
	@Column(name = "WF_PRICE", columnDefinition = "NUMERIC(9,2) default 0")
	protected Float price;
	
	@Column(name = "WF_PRICE1", columnDefinition = "NUMERIC(9,2) default 0 ")
	protected Float price1;
	
	
	@Column(name = "WF_PRICE2", columnDefinition = "NUMERIC(9,2) default 0 ")
	protected Float price2;
	
	@Column(name = "WF_PRICE3", columnDefinition = "NUMERIC(9,2) default 0 ")
	protected Float price3;
	
	@Column(name = "WF_STOCK", columnDefinition = "NUMERIC(9,2) default 0 ")
	protected Float stock;
	
	@Column(name = "WF_STOCK_BAR", columnDefinition = "NUMERIC(9,2) default 0 ")
	protected float stockBar;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Category getCate() {
		return cate;
	}

	public void setCate(Category cate) {
		this.cate = cate;
	}

	public String getImageContextName() {
		return imageContextName;
	}

	public void setImageContextName(String imageContextName) {
		this.imageContextName = imageContextName;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getPrice() {
		return price == null? 0:price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getPrice1() {
		return price1 == null ? 0 : price1;
	}

	public void setPrice1(Float price1) {
		this.price1 = price1;
	}

	public float getPrice2() {
		return price2 == null ?0  : price2;
	}

	public void setPrice2(Float price2) {
		this.price2 = price2;
	}

	public float getPrice3() {
		return price3 == null ? 0 : price3;
	}

	public void setPrice3(Float price3) {
		this.price3 = price3;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public float getStock() {
		return stock == null ? 0 : stock;
	}

	public void setStock(float stock) {
		this.stock = stock;
	}

	public float getStockBar() {
		return stockBar;
	}

	public void setStockBar(float stockBar) {
		this.stockBar = stockBar;
	}

	public void setStock(Float stock) {
		this.stock = stock;
	}
	
	
	
	

}
