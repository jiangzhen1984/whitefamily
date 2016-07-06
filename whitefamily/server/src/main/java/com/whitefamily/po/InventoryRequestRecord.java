package com.whitefamily.po;

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

import com.whitefamily.po.customer.User;


@Entity
@Table(name = "WF_INVENTORY_REQUEST_RECORD")
public class InventoryRequestRecord extends Record {
	

	

	
	private Shop shop;
	
	
	@Enumerated(EnumType.ORDINAL)
	private InventoryStatus status;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Override
	public long getId() {
		return super.getId();
	}

	@Override
	public void setId(long id) {
		super.setId(id);
	}

	@Column(name = "WF_OPT_TIMESTAMP", columnDefinition = "timestamp")
	@Override
	public Date getDatetime() {
		return super.getDatetime();
	}

	@Override
	public void setDatetime(Date datetime) {
		super.setDatetime(datetime);
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_OPER_ID", insertable = true, updatable = true, nullable = true)
	public User getOperator() {
		return super.getOperator();
	}

	@Override
	public void setOperator(User operator) {
		super.setOperator(operator);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_SHOP_ID", insertable = true, updatable = true, nullable = true)
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public InventoryStatus getStatus() {
		return status;
	}

	public void setStatus(InventoryStatus status) {
		this.status = status;
	}

	
	
}
