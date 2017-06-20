package com.whitefamily.po.payment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WF_PAYMENT_INFO")
public class PaymentInfo {
	
	public enum PaymentState {
		UNPAY,
		CANCEL,
		PAIED,
		PAY_ERROR
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "OUTER_ID", columnDefinition = "VARCHAR(500)")
	private String pid;
	
	@Column(name = "OUTER_ID1", columnDefinition = "VARCHAR(500)")
	private String pid1;
	
	@Column(name = "WF_INVEN_REC_IDS", columnDefinition = "VARCHAR(500)")
	private String inventoryIds;
	
	@Column(name = "WF_CREATE_TIME", columnDefinition = "timestamp")
	private Date   createTime;	
	
	@Column(name = "WF_PAY_TIME", columnDefinition = "timestamp")
	private Date   paymentTime;
	
	@Enumerated(EnumType.ORDINAL)
	private PaymentState ps;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getInventoryIds() {
		return inventoryIds;
	}

	public void setInventoryIds(String inventoryIds) {
		this.inventoryIds = inventoryIds;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public PaymentState getPs() {
		return ps;
	}

	public void setPs(PaymentState ps) {
		this.ps = ps;
	}
	
	

}
