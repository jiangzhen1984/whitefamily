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
	
	@Column(name = "WF_PRICE", columnDefinition = "NUMERIC(9,2) ")
	protected Float price;

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
	
	
	
	

}
