package com.whitefamily.po.incoming;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name="WF_INCOMING_GROUP")
public class GroupOn {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_SHOP_ID", insertable = true, updatable = true, nullable = false)
	protected Shop shop;
	
	@Enumerated(EnumType.ORDINAL)
	protected GroupOnType groupType;
	
	@Column(name = "WF_DATE", columnDefinition = "DATE")
	@Type(type="date")
	protected Date date;
	
	@Column(name = "WF_COUNT", columnDefinition = "NUMERIC(5)")
	protected int count;
	
	@Column(name = "WF_DJQ", columnDefinition = "NUMERIC(10,3)")
	protected float djq;
	
	@Column(name = "WF_GME", columnDefinition = "NUMERIC(10,3)")
	protected float gme;
	
	@Column(name = "WF_CE", columnDefinition = "NUMERIC(10,3)")
	protected float ce;
	
	@Column(name = "WF_SJE", columnDefinition = "NUMERIC(10,3)")
	protected float sje;
	
	@Column(name = "WF_OTHER", columnDefinition = "NUMERIC(10,3)")
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public float getDjq() {
		return djq;
	}

	public void setDjq(float djq) {
		this.djq = djq;
	}

	public float getGme() {
		return gme;
	}

	public void setGme(float gme) {
		this.gme = gme;
	}

	public float getCe() {
		return ce;
	}

	public void setCe(float ce) {
		this.ce = ce;
	}

	public float getSje() {
		return sje;
	}

	public void setSje(float sje) {
		this.sje = sje;
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

	public GroupOnType getGroupType() {
		return groupType;
	}

	public void setGroupType(GroupOnType groupType) {
		this.groupType = groupType;
	}


	

}
