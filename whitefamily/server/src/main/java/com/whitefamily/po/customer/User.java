package com.whitefamily.po.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WF_USER")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "WF_USER_NAME", columnDefinition = "VARCHAR(100)")
	private String username;
	
	@Column(name = "WF_USER_PWD", columnDefinition = "VARCHAR(100)")
	private String password;
	
	@Column(name = "WF_NAME", columnDefinition = "VARCHAR(100)")
	protected String name;
	
	@Enumerated(EnumType.ORDINAL)
	private Role role;
	
	@Enumerated(EnumType.ORDINAL)
	private AccountType accountType;
	
	@Column(name = "WF_SHOP_NAME", columnDefinition = "VARCHAR(100)")
	private String shopName;
	
	@Column(name = "WF_SHOP_ADDRESS", columnDefinition = "VARCHAR(400)")
	private String shopAddress;
	
	@Column(name = "WF_SHOP_ID")
	private Long  shopId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public long getShopId() {
		return shopId == null ? 0 : shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	

	
	
}
