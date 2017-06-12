package com.whitefamily.po.payment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WF_PAY_INVEN_REC")
public class PaymentInventoryRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "WF_PAYMENT_ID")
	private Long payId;
	
	@Column(name = "WF_INVEN_REC_ID")
	private Long invecId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Long getInvecId() {
		return invecId;
	}

	public void setInvecId(Long invecId) {
		this.invecId = invecId;
	}
	
	

}
