package com.whitefamily.po;

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

import com.whitefamily.po.customer.User;

@Entity
@Table(name = "WF_PRICE_RECORD")
public class PriceRecord extends Record {
	
	@Column(name = "WF_OPT_TIMESTAMP", columnDefinition = "timestamp")
	private float pr;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return super.getId();
	}

	@Override
	public void setId(long id) {
		super.setId(id);
	}

	@Override
	@Column(name = "WF_OPT_TIMESTAMP", columnDefinition = "timestamp")
	public Date getDatetime() {
		return super.getDatetime();
	}

	@Override
	public void setDatetime(Date datetime) {
		super.setDatetime(datetime);
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_USER_ID", insertable = true, updatable = true, nullable = false)
	public User getOperator() {
		return super.getOperator();
	}

	@Override
	public void setOperator(User operator) {
		super.setOperator(operator);
	}
	
	
	

}
