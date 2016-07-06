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
@Table(name = "WF_INVENTORY_RECORD")
public class InventoryUpdateRecord extends Record {
	

	@Enumerated(EnumType.ORDINAL)
	protected InventoryType it;
	
	
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

	public InventoryType getIt() {
		return it;
	}

	public void setIt(InventoryType it) {
		this.it = it;
	}

	
	
}
