package com.whitefamily.service.vo;

import java.util.Date;

import com.whitefamily.po.order.OrderState;

public class WFOrder {

	private Long id;
	
	private Integer price;
	
	private OrderState os;
	
	private String orderSn;
	
	private WFShop shop;
	
	private WFUser manager;
	
	private Date createTime;
	
	private Date payTime;
	
	private WFPaymentInfo payment;
	
	private WFDelivery delivery;
	
	private WFInventoryRequest request;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public OrderState getOs() {
		return os;
	}

	public void setOs(OrderState os) {
		this.os = os;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public WFShop getShop() {
		return shop;
	}

	public void setShop(WFShop shop) {
		this.shop = shop;
	}

	public WFUser getManager() {
		return manager;
	}

	public void setManager(WFUser manager) {
		this.manager = manager;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public WFPaymentInfo getPayment() {
		return payment;
	}

	public void setPayment(WFPaymentInfo payment) {
		this.payment = payment;
	}

	public WFDelivery getDelivery() {
		return delivery;
	}

	public void setDelivery(WFDelivery delivery) {
		this.delivery = delivery;
	}

	public WFInventoryRequest getRequest() {
		return request;
	}

	public void setRequest(WFInventoryRequest request) {
		this.request = request;
	}
	
	
	
}
