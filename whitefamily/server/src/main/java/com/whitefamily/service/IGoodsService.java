package com.whitefamily.service;

import java.util.List;

import com.whitefamily.po.InventoryType;
import com.whitefamily.po.customer.User;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;

public interface IGoodsService {
	
	
	public void addGoods(WFGoods goods);
	
	
	public void updateGoods(WFGoods wfg);
	
	
	public void removeGoods(WFGoods wfg);
	
	
	public WFGoods getGoods(long id);
	
	
	public WFBrand getBrand(long id);
	
	
	public void addGoodsBrand(WFGoods goods, WFBrand brand);
	
	
	public void removeGoodsBrand(WFGoods goods, WFBrand brand);
	
	
	
	public void updateGoodsPrice(WFGoods goods, User opter);
	
	
	public void updateGoodsInventory(WFGoods goods, WFBrand brand, float price, int count, InventoryType it, User opter);
	
	
	public List<WFGoods> queryGoods(int start, int count, int type);
	
	
	public List<WFGoods> queryGoods(WFCategory cate, int start, int count, int type);
	
	public List<WFGoods> queryGoods(List<WFCategory> cate, int start, int count, int type);
	
	
	
	public List<WFBrand> queryBrands(WFGoods goods, int start, int count);
}
