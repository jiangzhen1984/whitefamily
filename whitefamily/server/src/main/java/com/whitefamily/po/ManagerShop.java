package com.whitefamily.po;

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
@Table(name="WF_MANAGER_SHOP")
public class ManagerShop {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_MANAGER_ID", insertable = true, updatable = true, nullable = true)
	private User manager;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WF_SHOP_ID", insertable = true, updatable = true, nullable = true)
	private Shop shop;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
	
	

}
