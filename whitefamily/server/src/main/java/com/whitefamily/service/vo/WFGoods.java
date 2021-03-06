package com.whitefamily.service.vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.whitefamily.po.Goods;

public class WFGoods extends Goods {
	
	private boolean brandsLoad;

	private List<WFBrand> brands;
	
	private String abbr;
	
	private float currentPrice;
	
	private int currentStock;
	

	public WFGoods() {
		super();
	}
	

	public WFGoods(Goods c) {
		super();
		this.setId(c.getId());
		this.setName(c.getName());
		this.setType(c.getType());
		this.setUnit(c.getUnit());
		this.setGoodsDesc(c.getGoodsDesc());
		this.setPrice(c.getPrice());
		this.setPrice1(c.getPrice1());
		this.setStock(c.getStock());
		this.setStockBar(c.getStockBar());
		this.abbr =  PinyinHelper.getShortPinyin(this.name);
	}
	
	
	
	public String getTypeDisMessage() {
		if (type == WFGoodsVisible.SHOP.ordinal()) {
			return "店长可见";
		} else if (type == WFGoodsVisible.FRANCHISEE.ordinal()){
			return "加盟商可见";
		} else if (type == WFGoodsVisible.DELIVERY_STAFF.ordinal()){
			return "库管可见";
		} else {
			return "未知";
		}
	}
	
	
	
	
	@Override
	public void setName(String name) {
		super.setName(name);
		this.abbr =  PinyinHelper.getShortPinyin(this.name);
	}


	public String getAbbr() {
		return abbr;
	}


	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	
	


	public float getCurrentPrice() {
		return currentPrice;
	}


	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}


	public int getCurrentStock() {
		return currentStock;
	}


	public void setCurrentStock(int currentStock) {
		this.currentStock = currentStock;
	}


	public List<WFBrand> getBrands() {
		return brands;
	}


	public void setBrands(List<WFBrand> brands) {
		this.brands = brands;
	}
	
	


	public boolean isBrandsLoad() {
		return brandsLoad;
	}


	public void setBrandsLoad(boolean brandsLoad) {
		this.brandsLoad = brandsLoad;
	}

	
	public void clearBrandCache() {
		if (this.brands != null) {
			this.brands.clear();
		}
	}

	public void addBrand(WFBrand brand) {
		if (this.brands == null) {
			this.brands = new ArrayList<WFBrand>();
		}
		this.brands.add(brand);
	}

	
	public void removeBrand(WFBrand brand) {
		if (this.brands == null) {
			return;
		}
		Iterator<WFBrand> it = brands.iterator();
		while (it.hasNext()) {
			if (it.next().getId() == brand.getId()) {
				it.remove();
				break;
			}
		}
	}
	
	
	public int getSortOrder() {
		WFCategory wf = (WFCategory) this.cate;
		WFCategory  parent = null;
		if (wf != null) {
			while (wf != null) {
				parent = wf;
				wf = wf.getParent();
			}
		}
		
		return parent == null ? 1000 : parent.getOrder();
	}
	
	public int getCateOrder() {
		if (this.cate == null) {
			return 1000;
		} else {
			return this.cate.getOrder();
		}
	}
	
	
	public WFCategory getRootCategory() {
		WFCategory wf = (WFCategory) this.cate;
		return wf.getRootCategory();
	}
}
