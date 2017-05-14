package com.whitefamily.web.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whitefamily.service.vo.WFGoods;

public class Cart {

	public class Item {
		public WFGoods goods;

		public float count;

		public WFGoods getGoods() {
			return goods;
		}

		public void setGoods(WFGoods goods) {
			this.goods = goods;
		}

		public float getCount() {
			return count;
		}

		public void setCount(float count) {
			this.count = count;
		}
		
		
	}
	
	
	
	
	public Cart() {
		super();
		map = new HashMap<WFGoods, Item>();
	}

	private Map<WFGoods, Item> map;
	
	private int totalCount;
	
	
	public void addItem(WFGoods goods, float count) {
		Item item = map.get(goods);
		if (item == null) {
			item = new Item();
			item.goods = goods;
			map.put(goods, item);
		}
		item.count =count;
		totalCount += count;
	}
	
	
	public void removeItem(WFGoods goods) {
		map.remove(goods);
	}
	
	
	public int addItemCount(WFGoods goods, float count) {
		Item item = map.get(goods);
		if (item == null) {
			item = new Item();
			item.goods = goods;
			map.put(goods, item);
		}
		item.count += count;
		totalCount += count;
		return (int)item.count;
	}
	
	public int minusItemCount(WFGoods goods, float count) {
		Item item = map.get(goods);
		if (item == null) {
			return 0;
		}
		item.count -= count;
		totalCount -= count;
		if (item.count <= 0) {
			map.remove(goods);
		}
		return (int)item.count;
	}
	
	public List<Item> getItems() {
		return new ArrayList<Item>(map.values());
	}


	public int getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
	public int getItemCount() {
		return map.values().size();
	}

	
	public float getTotalPrice() {
		float total = 0;
		Collection<Item> col = map.values();
		for (Item it : col) {
			total += it.count * it.goods.getPrice();
		}
		return total;
	}
	
	public float getTotalPrice1() {
		float total = 0;
		Collection<Item> col = map.values();
		for (Item it : col) {
			total += it.count * it.goods.getPrice1();
		}
		return total;
	}
	
	public float getTotalPrice2() {
		float total = 0;
		Collection<Item> col = map.values();
		for (Item it : col) {
			total += it.count * it.goods.getPrice2();
		}
		return total;
	}
}
