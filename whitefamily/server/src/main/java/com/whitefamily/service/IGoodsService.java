package com.whitefamily.service;

import java.util.List;

import com.whitefamily.po.InventoryType;
import com.whitefamily.po.customer.User;
import com.whitefamily.service.vo.WFArtifact;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFVendor;

public interface IGoodsService {
	
	
	public void addGoods(WFGoods goods);
	
	
	public void updateGoods(WFGoods wfg);
	
	
	public Result updateGoodsStockBar(WFGoods wfg, float stockbar);
	
	public Result removeGoods(WFGoods wfg);
	
	
	public WFGoods getGoods(long id);
	
	
	public WFBrand getBrand(long id);
	
	public WFVendor getVendor(long id);
	
	
	public WFBrand getBrand(String name);
	
	public WFVendor getVendor(String name);
	
	
	public Result addVendor(WFVendor wfv);
	
	
	public Result addBrand(WFBrand wfb);
	
	
	
	public List<WFBrand> searchBrand(String name, int count);
	
	
	public List<WFVendor> searchVendor(String name, int count);
	
	
	public void updateGoodsPrice(WFGoods goods, float newPrice, float newPrice1,  float newPrice2,  float newPrice3, User opter);
	
	
	public void updateGoodsInventory(WFGoods goods, WFBrand brand, float price, int count, InventoryType it, User opter);
	
	
	public List<WFGoods> queryGoods(int start, int count, int type);
	
	
	public List<WFGoods> queryGoods(WFCategory cate, int start, int count, int type);
	
	public List<WFGoods> queryGoods(List<WFCategory> cate, int start, int count, int type);
	
	
	
	public List<WFBrand> queryBrands(WFGoods goods, int start, int count);
	
	
	public void init();
	
	
	public Result createWFArtifact(WFArtifact wff);
	
	
	public WFArtifact loadArtifact(long id);
	
	
	public List<WFArtifact> loadArtifacts();
	
	
	public List<WFArtifact> searchWFArtifactGoods(String name);

}
