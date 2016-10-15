package com.whitefamily.po.delivery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="WF_DELIVERY_SUPPLIER_CONF")
public class DeliverySupplierConfiguration {

	public enum MC {
		CATE,
		GOODS,
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Enumerated(EnumType.ORDINAL)
	private MC mc;
	
	
	@Column(name = "WF_MC_ID")
	private long mappingId;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public MC getMc() {
		return mc;
	}


	public void setMc(MC mc) {
		this.mc = mc;
	}


	public long getMappingId() {
		return mappingId;
	}


	public void setMappingId(long mappingId) {
		this.mappingId = mappingId;
	}
	
	
	
	
}
