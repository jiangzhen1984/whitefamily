package com.whitefamily.po.incoming;

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

import org.hibernate.annotations.Type;

import com.whitefamily.po.Shop;

@Entity
@Table(name="WF_INCOMING_DELIVERY")
public class Delivery {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_SHOP_ID", insertable = true, updatable = true, nullable = false)
	protected Shop shop;
	
	@Enumerated(EnumType.ORDINAL)
	protected DeliveryType deliveryType;
	
	@Column(name = "WF_DATE", columnDefinition = "DATE")
	@Type(type="date")
	protected Date date;
	
	@Column(name = "WF_INCOMING", columnDefinition = "NUMERIC(10,3)")
	protected float incoming;
	
	@Column(name = "WF_ONLINE_PAYMENT", columnDefinition = "NUMERIC(10,3)")
	protected float onlinePayment;
	
	@Column(name = "WF_REFUND", columnDefinition = "NUMERIC(10,3)")
	protected float refund;
	
	@Column(name = "WF_REFUND1", columnDefinition = "NUMERIC(10,3)")
	protected float refund1;
	
	@Column(name = "WF_SERVICE_FEE", columnDefinition = "NUMERIC(10,3)")
	protected float serviceFee;
	
	@Column(name = "WF_DELIVERY_FEE", columnDefinition = "NUMERIC(10,3)")
	protected float deliveryFee;
	
	@Column(name = "WF_VALID", columnDefinition = "NUMERIC(4,0)")
	protected int valid;
	
	@Column(name = "WF_DESC", columnDefinition = "VARCHAR(400)")
	protected String desc;
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}

	public float getIncoming() {
		return incoming;
	}

	public void setIncoming(float incoming) {
		this.incoming = incoming;
	}

	public float getOnlinePayment() {
		return onlinePayment;
	}

	public void setOnlinePayment(float onlinePayment) {
		this.onlinePayment = onlinePayment;
	}

	public float getRefund() {
		return refund;
	}

	public void setRefund(float refund) {
		this.refund = refund;
	}

	public float getRefund1() {
		return refund1;
	}

	public void setRefund1(float refund1) {
		this.refund1 = refund1;
	}

	public float getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(float serviceFee) {
		this.serviceFee = serviceFee;
	}

	public float getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(float deliveryFee) {
		this.deliveryFee = deliveryFee;
	}


	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	
	

}
