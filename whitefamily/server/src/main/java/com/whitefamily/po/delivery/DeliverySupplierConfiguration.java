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
		GOODS;
		
		public String getStrValue() {
			switch (this) {
			case CATE:
				return "品类";
			case GOODS:
				return "产品";
			default:
				return "未知";
			
			}
		}
		
		public int getOrdinal() {
			return this.ordinal();
		}
	}
	
	
	public enum SupplierType {
		UNKNOW,
		VEGETABLE,
	}
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Enumerated(EnumType.ORDINAL)
	private MC mc;
	
	
	@Column(name = "WF_MC_ID")
	private long mappingId;
	
	@Column(name = "WF_SUPPLIER_ID", columnDefinition = "NUMERIC(12) DEFAULT 0")
	private long supplierId;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "WF_SUPPLIER_TYPE", columnDefinition = "int(2) DEFAULT 0")
	private SupplierType supplierType;

	@Column(name = "WF_SUPPLIER_BOUND", columnDefinition = "CHAR(1) DEFAULT '0'")
	private Boolean bounds;


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


	public long getSupplierId() {
		return supplierId;
	}


	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}


	public SupplierType getSupplierType() {
		return supplierType;
	}


	public void setSupplierType(SupplierType supplierType) {
		this.supplierType = supplierType;
	}


	public Boolean getBounds() {
		return bounds;
	}


	public void setBounds(Boolean bounds) {
		this.bounds = bounds;
	}


	
	
	
}
