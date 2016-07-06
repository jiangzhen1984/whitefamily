package com.whitefamily.po;

import java.util.Date;

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
@Table(name="WF_DAMAGE_INVENTORY")
public class DamageInventory {
	
	public static final int TYPE_DAMAGE = 1;
	
	public static final int TYPE_INVENTORY = 2;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_BR_CATE_ID", insertable = true, updatable = true, nullable = false)
	protected Category cate;
	
	@Column(name="WF_BR_QUA", columnDefinition="numeric(6,2)")
	protected float quality;
	
	@Column(name="WF_DA_TIME", columnDefinition="timestamp")
	protected Date time;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_BR_SHOP_ID", insertable = true, updatable = true, nullable = false)
	protected Shop shop;
	
	@Column(name="WF_TYPE", columnDefinition="tinyint")
	protected int type;
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Category getCate() {
		return cate;
	}

	public void setCate(Category cate) {
		this.cate = cate;
	}

	public float getQuality() {
		return quality;
	}

	public void setQuality(float quality) {
		this.quality = quality;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	
}
