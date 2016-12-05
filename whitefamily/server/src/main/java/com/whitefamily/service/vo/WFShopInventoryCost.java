package com.whitefamily.service.vo;

public class WFShopInventoryCost {
	
	private long id;
	
	private WFCategory cate;
	
	private double cost;
	
	

	public WFShopInventoryCost(WFCategory cate) {
		super();
		if (cate == null) {
			throw new NullPointerException(" category is null");
		}
		this.cate = cate;
		this.id = this.cate.getId();
	}
	
	public void addCost(double cost) {
		this.cost += cost;
	}
	
	public void addCost(float cost) {
		this.cost += cost;
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
	
	

}
