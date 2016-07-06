package com.whitefamily.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.whitefamily.po.DamageStatus;
import com.whitefamily.po.customer.User;

public class WFDamageReport {

	protected long id;

	protected Date datetime;

	protected User operator;
	
	protected WFShop shop;

	protected DamageStatus is;

	protected List<Item> itemList;


	public DamageStatus getIs() {
		return is;
	}

	public void setIs(DamageStatus is) {
		this.is = is;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}
	
	

	public WFShop getShop() {
		return shop;
	}

	public void setShop(WFShop shop) {
		this.shop = shop;
	}

	public void addDamageItem(WFGoods goods, float count, boolean persiste) {
		if (itemList == null) {
			itemList = new ArrayList<Item>(10);
		}
		itemList.add(new Item(count, goods, persiste));
	}

	public int getItemCount() {
		return itemList == null ? 0 : itemList.size();
	}

	public Item getItem(int idx) {
		if (itemList == null || itemList.size() <= idx) {
			throw new RuntimeException("incorrect idx");
		}
		return itemList.get(idx);
	}

	public void clearItems() {
		if (itemList != null && itemList.size() > 0) {
			itemList.clear();
		}
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public class Item {

		float count;

		WFGoods goods;

		boolean persisted;

		public Item(float count, WFGoods goods, boolean persisted) {
			super();
			this.count = count;
			this.goods = goods;
			this.persisted = persisted;
		}

		public float getCount() {
			return count;
		}

		public void setCount(float count) {
			this.count = count;
		}

		public WFGoods getGoods() {
			return goods;
		}

		public void setGoods(WFGoods goods) {
			this.goods = goods;
		}

		public boolean isPersisted() {
			return persisted;
		}

		public void setPersisted(boolean persisted) {
			this.persisted = persisted;
		}

	}

}
