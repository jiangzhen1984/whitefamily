package com.whitefamily.po.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.whitefamily.po.Record;
import com.whitefamily.po.customer.User;

@Entity
@Table(name = "WF_FRANCHISEE_ORDER")
public class FranchiseeOrder extends Record {
	
	
	
	private Integer price;
	
	
	private OrderState os;
	
	
	private String orderSn;
	
	
	private Long shopId;
	
	
	private Long userId;
	
	
	private Date createTime;
	
	
	private Date payTime;

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
	@Transient
	public User getOperator() {
		return super.getOperator();
	}

	@Override
	public void setOperator(User operator) {
		super.setOperator(operator);
	}

	@Column(name = "WF_ORDER_PRICE", columnDefinition = "NUMERIC(32)")
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "WF_ORDER_STATE")
	public OrderState getOs() {
		return os;
	}

	public void setOs(OrderState os) {
		this.os = os;
	}

	@Column(name = "WF_ORDER_SN", columnDefinition = "VARCHAR(32)")
	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	@Column(name = "WF_ORDER_SHOP_ID", columnDefinition = "NUMERIC(32)")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column(name = "WF_ORDER_USER_ID", columnDefinition = "NUMERIC(32)")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "WF_PAY_TIMESTAMP", columnDefinition = "datetime")
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@Column(name = "WF_CREATE_TIMESTAMP", columnDefinition = "datetime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	
}
