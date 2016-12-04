package com.whitefamily.service.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.whitefamily.po.InventoryStatus;
import com.whitefamily.po.customer.User;
import com.whitefamily.po.delivery.DeliverySupplierConfiguration;
import com.whitefamily.service.ServiceFactory;

public class WFInventoryRequest {

	protected long id;

	protected Date datetime;

	protected User operator;
	
	protected WFShop shop;

	protected InventoryStatus is;

	protected List<Item> itemList;
	
	protected List<Item> supplierItemList;
	
	protected boolean isLoadItem;
	
	private List<WFSupplierMapping> mappingList;


	public WFInventoryRequest() {
		super();
		mappingList  =ServiceFactory.getSupplierService().getMappingList();
	}


	public InventoryStatus getIs() {
		return is;
	}
	

	public void setIs(InventoryStatus is) {
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
	
	

	public boolean isLoadItem() {
		return isLoadItem;
	}

	public void setLoadItem(boolean isLoadItem) {
		this.isLoadItem = isLoadItem;
	}

	public void addInventoryItem(WFGoods goods, float count, boolean persiste) {
		addInventoryItem(goods, count, 0, 0, persiste);
	}
	
	
	public void addInventoryItem(WFGoods goods, float count, float realCount, float pr, boolean persiste) {
		if (itemList == null) {
			itemList = new ArrayList<Item>(10);
			supplierItemList =  new ArrayList<Item>(10);
		}
		Item item = new Item(count, pr, realCount, goods, persiste);
		itemList.add(item);
		addToSupplierList(item);
	}
	
	
	
	private void addToSupplierList(Item item) {
		for (WFSupplierMapping wfs : mappingList) {
			if (item.goods.getId() == wfs.getMappingId() && wfs.getMc() == DeliverySupplierConfiguration.MC.GOODS) {
				supplierItemList.add(item);
			} else if (wfs.getMc() == DeliverySupplierConfiguration.MC.CATE) {
				WFCategory wf = (WFCategory)item.goods.getCate();
				while (wf != null) {
					if (wf.getId() == wfs.getMappingId()) {
						supplierItemList.add(item);
						break;
					} 
					
					wf = wf.getParent();
				}
			}
		}
	}
	
	
	public void updateInventoryItem(WFGoods goods, float realCount, float pr) {
		int size = itemList == null ? 0 : itemList.size();
		Item item;
		for (int i = 0; i < size; i++) {
			item = itemList.get(i);
			if (goods.getId() == item.getGoods().getId()) {
				item.price = pr;
				item.realCount = realCount;
				break;
			}
		}
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
		 Collections.sort(itemList);
		 return itemList;
	}
	
	

	public List<Item> getSupplierItemList() {
		 Collections.sort(supplierItemList);
		return supplierItemList;
	}



	public class Item implements Comparable<Item> {

		float count;

		WFGoods goods;

		boolean persisted;
		
		float price;
		
		float realCount;

		public Item(float count, WFGoods goods, boolean persisted) {
			super();
			this.count = count;
			this.goods = goods;
			this.persisted = persisted;
		}
		
		public Item(float count, float pr,float realCount, WFGoods goods, boolean persisted) {
			super();
			this.count = count;
			this.price = pr;
			this.realCount = realCount;
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

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public float getRealCount() {
			return realCount;
		}

		public void setRealCount(float realCount) {
			this.realCount = realCount;
		}
		
		@Override
		public int compareTo(Item o) {
			if (o.goods.getSortOrder() == this.goods.getSortOrder()) {
				return o.goods.getCateOrder() > this.goods.getCateOrder() ? -1: 1;
			} else {
				return o.goods.getSortOrder() > this.goods.getSortOrder() ? -1: 1;
			}
		}
		

	}

}
