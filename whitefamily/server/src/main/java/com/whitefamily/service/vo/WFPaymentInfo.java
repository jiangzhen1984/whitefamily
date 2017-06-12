package com.whitefamily.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.whitefamily.po.payment.PaymentInfo.PaymentState;

public class WFPaymentInfo {

	
	private Long id;
	
	private String pid;
	
	private String pid1;
	
	private String inventoryIds;
	
	private Date   createTime;	
	
	private Date   paymentTime;
	
	private PaymentState ps;
	
	private List<WFInventoryRequest> requests;
	
	

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

	public List<WFInventoryRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<WFInventoryRequest> requests) {
		if (this.requests == null) {
			this.requests = new ArrayList<WFInventoryRequest>();
		}
		this.requests.addAll(requests);
	}

	public String getPid1() {
		return pid1;
	}

	public void setPid1(String pid1) {
		this.pid1 = pid1;
	}
	
	public void AddWFIR(WFInventoryRequest r) {
		if (this.requests == null) {
			this.requests = new ArrayList<WFInventoryRequest>();
		}
		this.requests.add(r);
	}
	
}
