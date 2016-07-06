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



}
