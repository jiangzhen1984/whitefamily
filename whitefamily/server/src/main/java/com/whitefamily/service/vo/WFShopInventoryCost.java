package com.whitefamily.service.vo;

import java.util.HashMap;
import java.util.Map;

public class WFShopInventoryCost {
	
	private long id;
	
	private WFCategory cate;
	
	private double cost;
	
	private Map<WFGoods, GoodsCost> goodsCost;
	

	public WFShopInventoryCost(WFCategory cate) {
		super();
		if (cate == null) {
			throw new NullPointerException(" category is null");
		}
		this.cate = cate;
		this.id = this.cate.getId();
		goodsCost = new HashMap<WFGoods, GoodsCost>();
	}
	
	public void addCost(WFGoods wfg, double cost) {
		this.cost += cost;
		updateGoodsCost(wfg, cost);
	}
	
	public void addCost(WFGoods wfg, float cost) {
		this.cost += cost;
		updateGoodsCost(wfg, cost);
	}
	
	
	private void updateGoodsCost(WFGoods wfg, double cost) {
		GoodsCost gc = goodsCost.get(wfg);
		if (gc == null) {
			gc = new GoodsCost();
			goodsCost.put(wfg, gc);
		}
		gc.wfg = wfg;
		gc.cost += cost;
		gc.rate = gc.cost / this.cost * 100;
	}
	
	public void clearCost() {
		this.cost = 0;
	}
	
	public double getCost() {
		return cost;
	}
	
	

	public WFCategory getCate() {
		return cate;
	}
	
	

	public Map<WFGoods, GoodsCost> getGoodsCost() {
		return goodsCost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WFShopInventoryCost other = (WFShopInventoryCost) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
	public class GoodsCost {
		WFGoods wfg;
		double cost;
		double rate;
		public WFGoods getWfg() {
			return wfg;
		}
				public double getCost() {
			return cost;
		}
				public double getRate() {
					return rate;
				}
		
				
				
	}

}
