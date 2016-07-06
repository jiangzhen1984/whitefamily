package com.whitefamily.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.whitefamily.po.InventoryType;
import com.whitefamily.po.customer.User;

public class WFInventory {

	protected long id;

	protected Date datetime;

	protected User operator;

	protected InventoryType it;

	protected List<Item> itemList;

	public InventoryType getIt() {
		return it;
	}

	public void setIt(InventoryType it) {
		this.it = it;
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

	public void addInventoryItem(WFGoods goods, WFBrand brand, float count,
			float price, boolean persiste) {
		if (itemList == null) {
			itemList = new ArrayList<Item>(10);
		}
		itemList.add(new Item(count, price, goods, brand, persiste));
	}
	
	public int getItemCount() {
		return itemList == null? 0 : itemList.size();
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

		float price;

		WFGoods goods;

		WFBrand brand;

		boolean persisted;

		public Item(float count, float price, WFGoods goods, WFBrand brand,
				boolean persisted) {
			super();
			this.count = count;
			this.price = price;
			this.goods = goods;
			this.brand = brand;
			this.persisted = persisted;
		}

		public float getCount() {
			return count;
		}

		public void setCount(float count) {
			this.count = count;
		}

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public WFGoods getGoods() {
			return goods;
		}

		public void setGoods(WFGoods goods) {
			this.goods = goods;
		}

		public WFBrand getBrand() {
			return brand;
		}

		public void setBrand(WFBrand brand) {
			this.brand = brand;
		}

		public boolean isPersisted() {
			return persisted;
		}

		public void setPersisted(boolean persisted) {
			this.persisted = persisted;
		}
		
		

	}

}
