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
	
	//日用调料
	@Column(name = "WF_RYTL", columnDefinition = "NUMERIC(10,3)")
	private float rytl;
	
	//补菜
	@Column(name = "WF_BC", columnDefinition = "NUMERIC(10,3)")
	private float bc;
	
	//烧饼
	@Column(name = "WF_SB", columnDefinition = "NUMERIC(10,3)")
	private float sb;
	
	//伙食费
	@Column(name = "WF_HSF", columnDefinition = "NUMERIC(10,3)")
	private float hsf;
	
	//饮料
	@Column(name = "WF_YL", columnDefinition = "NUMERIC(10,3)")
	private float yl;
	
	//水费
	@Column(name = "WF_SF", columnDefinition = "NUMERIC(10,3)")
	private float sf;
	
	//电费
	@Column(name = "WF_DF", columnDefinition = "NUMERIC(10,3)")
	private float df;
	
	//房费
	@Column(name = "WF_FF", columnDefinition = "NUMERIC(10,3)")
	private float ff;
	
	//燃气费
	@Column(name = "WF_RQF", columnDefinition = "NUMERIC(10,3)")
	private float rqf;
	
	//工资
	@Column(name = "WF_GZ", columnDefinition = "NUMERIC(10,3)")
	private float gz;
	
	//日杂
	@Column(name = "WF_RZ", columnDefinition = "NUMERIC(10,3)")
	private float rz;
	
	//其他
	@Column(name = "WF_QT", columnDefinition = "NUMERIC(10,3)")
	private float qt;
	
	@Column(name = "WF_DESC", columnDefinition = "VARCHAR(400)")
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getRytl() {
		return rytl;
	}

	public void setRytl(float rytl) {
		this.rytl = rytl;
	}

	public float getBc() {
		return bc;
	}

	public void setBc(float bc) {
		this.bc = bc;
	}

	public float getSb() {
		return sb;
	}

	public void setSb(float sb) {
		this.sb = sb;
	}

	public float getHsf() {
		return hsf;
	}

	public void setHsf(float hsf) {
		this.hsf = hsf;
	}

	public float getYl() {
		return yl;
	}

	public void setYl(float yl) {
		this.yl = yl;
	}

	public float getSf() {
		return sf;
	}

	public void setSf(float sf) {
		this.sf = sf;
	}

	public float getDf() {
		return df;
	}

	public void setDf(float df) {
		this.df = df;
	}

	public float getFf() {
		return ff;
	}

	public void setFf(float ff) {
		this.ff = ff;
	}

	public float getRqf() {
		return rqf;
	}

	public void setRqf(float rqf) {
		this.rqf = rqf;
	}

	public float getGz() {
		return gz;
	}

	public void setGz(float gz) {
		this.gz = gz;
	}

	public float getRz() {
		return rz;
	}

	public void setRz(float rz) {
		this.rz = rz;
	}

	public float getQt() {
		return qt;
	}

	public void setQt(float qt) {
		this.qt = qt;
	}

	
	
	
	

}
