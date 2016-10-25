package com.whitefamily.po.incoming;

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

import org.hibernate.annotations.Type;

import com.whitefamily.po.Shop;

@Entity
@Table(name="WF_INCOMING")
public class Incoming {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_SHOP_ID", insertable = true, updatable = true, nullable = false)
	protected Shop shop;
	
	@Column(name = "WF_DATE", columnDefinition = "DATE")
	@Type(type="date")
	protected Date date;
	
	@Column(name = "WF_ZLS", columnDefinition = "NUMERIC(10,3) default 0")
	protected float zls;
	
	@Column(name = "WF_CASH", columnDefinition = "NUMERIC(10,3) default 0")
	protected float cash;
	
	@Column(name = "WF_WEIXIN", columnDefinition = "NUMERIC(10,3) default 0")
	protected float weixin;
	
	@Column(name = "WF_GROUP", columnDefinition = "NUMERIC(10,3) default 0")
	protected float group;
	
	@Column(name = "WF_DELIVERY", columnDefinition = "NUMERIC(10,3) default 0")
	protected float delivery;
	
	@Column(name = "WF_ALI", columnDefinition = "NUMERIC(10,3) default 0")
	protected float ali;
	
	@Column(name = "WF_NUOMI", columnDefinition = "NUMERIC(10,3) default 0")
	protected float nuomi;
	
	@Column(name = "WF_NUOMI_AF_FEE", columnDefinition = "NUMERIC(10,3) default 0")
	protected float nuomiaf;
	
	@Column(name = "WF_DAZHONG", columnDefinition = "NUMERIC(10,3) default 0")
	protected float dazhong;
	
	@Column(name = "WF_DAZHONG_AF_FEE", columnDefinition = "NUMERIC(10,3) default 0")
	protected float dazhongaf;
	
	@Column(name = "WF_OTHER", columnDefinition = "NUMERIC(10,3) default 0")
	protected float other;
	
	@Column(name = "WF_DESC", columnDefinition = "VARCHAR(400)")
	protected String desc;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getCash() {
		return cash;
	}

	public void setCash(float cash) {
		this.cash = cash;
	}

	public float getWeixin() {
		return weixin;
	}

	public void setWeixin(float weixin) {
		this.weixin = weixin;
	}

	public float getGroup() {
		return group;
	}

	public void setGroup(float group) {
		this.group = group;
	}

	public float getDelivery() {
		return delivery;
	}

	public void setDelivery(float delivery) {
		this.delivery = delivery;
	}

	public float getAli() {
		return ali;
	}

	public void setAli(float ali) {
		this.ali = ali;
	}

	public float getNuomi() {
		return nuomi;
	}

	public void setNuomi(float nuomi) {
		this.nuomi = nuomi;
	}

	public float getDazhong() {
		return dazhong;
	}

	public void setDazhong(float dazhong) {
		this.dazhong = dazhong;
	}

	public float getOther() {
		return other;
	}

	public void setOther(float other) {
		this.other = other;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getZls() {
		return zls;
	}

	public void setZls(float zls) {
		this.zls = zls;
	}

	public float getNuomiaf() {
		return nuomiaf;
	}

	public void setNuomiaf(float nuomiaf) {
		this.nuomiaf = nuomiaf;
	}

	public float getDazhongaf() {
		return dazhongaf;
	}

	public void setDazhongaf(float dazhongaf) {
		this.dazhongaf = dazhongaf;
	}
	
	
	
	
	

}
