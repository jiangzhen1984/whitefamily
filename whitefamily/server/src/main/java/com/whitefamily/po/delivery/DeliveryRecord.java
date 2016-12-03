package com.whitefamily.po.delivery;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.whitefamily.po.InventoryStatus;
import com.whitefamily.po.Record;
import com.whitefamily.po.Shop;
import com.whitefamily.po.customer.User;

@Entity
@Table(name = "WF_DELIVERY_RECORD")
public class DeliveryRecord extends Record {
	
	
	private Shop shop;

	@Enumerated(EnumType.ORDINAL)
	private InventoryStatus status;

	@Column(name = "WF_SHOP_ID")
	private Long shopId;

	@Column(name = "WF_SHOP_NAME", columnDefinition = "VARCHAR(100)")
	private String shopName;

	@Column(name = "WF_SHOP_ADDRESS", columnDefinition = "VARCHAR(400)")
	private String shopAddress;

	@Column(name = "WF_OPER_ID")
	private Long userId;

	@Column(name = "WF_USER_NAME", columnDefinition = "VARCHAR(100)")
	private String userName;
	
	@Column(name = "WF_INVENT_REQ_ID")
	private Long inventoryRequestId;
	
	@Transient
	protected List<DeliveryRecord> subRecords;
	
	@Transient
	protected boolean loadSubRecord;
	
	@Transient
	protected DeliveryRecord parent;
	
	

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
	
	
	

	@Override
	@Column(name = "WF_PARENT_REC_ID")
	public long getParentId() {
		return super.getParentId();
	}

	@Override
	public void setParentId(Long parentId) {
		super.setParentId(parentId);
	}

	public InventoryStatus getStatus() {
		return status;
	}

	public void setStatus(InventoryStatus status) {
		this.status = status;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Transient
	public List<DeliveryRecord> getSubRecords() {
		return subRecords;
	}

	public void setSubRecords(List<DeliveryRecord> subRecords) {
		this.subRecords = subRecords;
	}

	public boolean isLoadSubRecord() {
		return loadSubRecord;
	}

	public void setLoadSubRecord(boolean loadSubRecord) {
		this.loadSubRecord = loadSubRecord;
	}

	@Transient
	public DeliveryRecord getParent() {
		return parent;
	}

	public void setParent(DeliveryRecord parent) {
		this.parent = parent;
		this.setParentId(this.parent == null ? 0 : this.parent.getId());
	}

	public Long getInventoryRequestId() {
		return inventoryRequestId;
	}

	public void setInventoryRequestId(Long inventoryRequestId) {
		this.inventoryRequestId = inventoryRequestId;
	}
	
	

}
