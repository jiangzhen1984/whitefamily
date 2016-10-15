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
@Table(name="WF_BRAND")
public class Brand {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_GOOD_ID", insertable = true, updatable = true, nullable = false)
	protected Goods goods;
	
	@Column(name="WF_BR_NAME", columnDefinition="VARCHAR(500)")
	protected String name;
	
	@Column(name="WF_BR_STYLE", columnDefinition="VARCHAR(500)")
	protected String style;
	
	@Column(name="WF_BR_UNIT", columnDefinition="VARCHAR(100)")
	protected String unit;
	
	@Column(name="WF_BR_SUB_UNIT", columnDefinition="VARCHAR(100)")
	protected String subUnit;
	
	@Column(name="WF_BR_SUB_Count", columnDefinition="NUMERIC(4,0)")
	protected Integer subCount;
	
	@Column(name="WF_BR_CALCULATION", columnDefinition="VARCHAR(200)")
	protected String calculation;

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


	public String getStyle() {
		return style;
	}


	public void setStyle(String style) {
		this.style = style;
	}


	public Goods getGoods() {
		return goods;
	}


	public void setGoods(Goods goods) {
		this.goods = goods;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public String getSubUnit() {
		return subUnit;
	}


	public void setSubUnit(String subUnit) {
		this.subUnit = subUnit;
	}


	public int getSubCount() {
		return subCount == null? 0 : subCount;
	}


	public void setSubCount(int subCount) {
		this.subCount = subCount;
	}


	public String getCalculation() {
		return calculation;
	}


	public void setCalculation(String calculation) {
		this.calculation = calculation;
	}



}
