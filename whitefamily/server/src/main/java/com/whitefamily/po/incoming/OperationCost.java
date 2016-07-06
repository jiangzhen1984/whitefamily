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

import com.whitefamily.po.Shop;

@Entity
@Table(name="WF_OPERATION_COST")
public class OperationCost {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_SHOP_ID", insertable = true, updatable = true, nullable = false)
	private Shop shop;
	
	@Column(name = "WF_DATE", columnDefinition = "date")
	private Date date;
	
	@Column(name = "WF_FEE_REN", columnDefinition = "NUMERIC(10,3)")
	private float rent;
	
	@Column(name = "WF_COST_SD", columnDefinition = "NUMERIC(10,3)")
	private float sd;
	
	@Column(name = "WF_COST_SALARY", columnDefinition = "NUMERIC(10,3)")
	private float salary;
	
	@Column(name = "WF_HOUR_SALARY", columnDefinition = "NUMERIC(10,3)")
	private float tmpSalary;
	
	@Column(name = "WF_WRAPPER", columnDefinition = "NUMERIC(10,3)")
	private float wrapper;
	
	@Column(name = "WF_RQ", columnDefinition = "NUMERIC(10,3)")
	private float rq;
	
	@Column(name = "WF_OTHER", columnDefinition = "NUMERIC(10,3)")
	private float other;
	
	@Column(name = "WF_DESC", columnDefinition = "VARCHAR2(400)")
	private String desc;

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

	public float getRent() {
		return rent;
	}

	public void setRent(float rent) {
		this.rent = rent;
	}

	public float getSd() {
		return sd;
	}

	public void setSd(float sd) {
		this.sd = sd;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public float getTmpSalary() {
		return tmpSalary;
	}

	public void setTmpSalary(float tmpSalary) {
		this.tmpSalary = tmpSalary;
	}

	public float getWrapper() {
		return wrapper;
	}

	public void setWrapper(float wrapper) {
		this.wrapper = wrapper;
	}

	public float getRq() {
		return rq;
	}

	public void setRq(float rq) {
		this.rq = rq;
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

	
	
	
	

}
