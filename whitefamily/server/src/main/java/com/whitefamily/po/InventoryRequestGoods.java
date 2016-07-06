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
@Table(name = "WF_INVENTORY_REQUEST_GOODS")
public class InventoryRequestGoods {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_REC_ID", insertable = true, updatable = true, nullable = false)
	protected InventoryRequestRecord record;
	
	@Column(name="WF_UNIT_COUNT", columnDefinition="NUMERIC(10,2) default 0")
	protected float count;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_GOOD_ID", insertable = true, updatable = true, nullable = false)
	protected Goods goods;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getCount() {
		return count;
	}

	public void setCount(float count) {
		this.count = count;
	}


	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public InventoryRequestRecord getRecord() {
		return record;
	}

	public void setRecord(InventoryRequestRecord record) {
		this.record = record;
	}

	
	
  
	
}
