package com.whitefamily.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.whitefamily.po.customer.User;


@Entity
@Table(name = "WF_SHOP_INVENTORY_STATISTICS_RECORD")
public class ShopInventoryStatisticsRecord extends Record {
	


	private Shop shop;
	
	
	
	private Long shopId;

	
	private String shopName;

	
	private String shopAddress;

	
	private Long userId;

	
	private String userName;
	
	
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
		if (operator == null) {
			operator = new User();
			operator.setId(this.userId);
			operator.setUsername(this.userName);
		}
		return super.getOperator();
	}

	@Override
	public void setOperator(User operator) {
		super.setOperator(operator);
		if (operator != null) {
			this.userId = operator.getId();
			this.userName = operator.getName();
		}
	}
	
	@Transient
	public Shop getShop() {
		if (shop == null) {
			shop = new Shop();
			shop.setId(this.shopId == null ? 0 : this.shopId);
			shop.setName(this.shopName);
			shop.setAddress(this.shopAddress);
		}
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
		if (shop != null) {
			this.shopId = shop.getId();
			this.shopName = shop.getName();
			this.shopAddress = shop.getAddress();
		}
	}

	@Column(name = "WF_SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column(name = "WF_SHOP_NAME", columnDefinition = "VARCHAR(100)")
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	@Column(name = "WF_SHOP_ADDRESS", columnDefinition = "VARCHAR(400)")
	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	@Column(name = "WF_OPER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "WF_USER_NAME", columnDefinition = "VARCHAR(100)")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	
	
	
	
}
