package com.whitefamily.web.bean;

import java.util.ArrayList;
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
	
	
	public void addItem(WFGoods goods, float count) {
		Item item = map.get(goods);
		if (item == null) {
			item = new Item();
			item.goods = goods;
			map.put(goods, item);
		}
		item.count =count;
	}
	
	
	public void removeItem(WFGoods goods) {
		map.remove(goods);
	}
	
	
	public void addItemCount(WFGoods goods, float count) {
		Item item = map.get(goods);
		if (item == null) {
			item = new Item();
			item.goods = goods;
			map.put(goods, item);
		}
		item.count += count;
	}
	
	public void minusItemCount(WFGoods goods, float count) {
		Item item = map.get(goods);
		if (item == null) {
			return;
		}
		item.count -= count;
		if (item.count <= 0) {
			map.remove(goods);
		}
	}
	
	public List<Item> getItems() {
		return new ArrayList<Item>(map.values());
	}

}
