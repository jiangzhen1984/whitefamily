package com.whitefamily.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="WF_SHOP")
public class Shop {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected long id;
	
	@Column(name="WF_BR_NAME", columnDefinition="VARCHAR(500)")
	protected String name;
	
	@Column(name="WF_BR_ADDR", columnDefinition="VARCHAR(1000)")
	protected String address;
	
	@Column(name="WF_SHOP_TYPE", columnDefinition="NUMERIC(1) default 0")
	@Enumerated(EnumType.ORDINAL)
	protected ShopType type;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ShopType getType() {
		return type;
	}

	public void setType(ShopType type) {
		this.type = type;
	}
	
	
	
	
}
