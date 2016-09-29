package com.whitefamily.web.bean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.Result;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;

@ManagedBean(name = "goodsBean", eager = false)
@SessionScoped
public class GoodsBean {
	
	private static final int PAGE_COUNT = 100;
	
	private IGoodsService goodsService;
	
	private List<WFGoods> goodsList;
	
	private int pageNo = 1;
	
	// for create goods
	private String name;
	
	private long categoryId;
	
	private String categoryName;
	
	private int type;
	
	private String errMsg;
	
	private String goodsUnit;
	
	private String goodsDesc;
	
	private String goodsNameRequired;
	
	private String goodsCategoryRequired;
	
	
	// for brand
	private String brandName;
	private String brandNameRequired;
	
	private List<WFCategory> categoryList;

	private long goodsId;
	
	private WFGoods goods;
	
	//for update goods
	private long updateGoodsId;

	public GoodsBean() {
		goodsService = ServiceFactory.getGoodsService();
	}
	
	
	
	public List<WFGoods> getGoodsList() {
		return goodsService.queryGoods((pageNo - 1) * PAGE_COUNT, PAGE_COUNT, -1);
	}



	public int getPageNo() {
		return pageNo;
	}



	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}



	public IGoodsService getGoodsService() {
		return goodsService;
	}



	public void setGoodsService(IGoodsService goodsService) {
		this.goodsService = goodsService;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public long getCategoryId() {
		return categoryId;
	}



	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}



	public String getErrMsg() {
		return errMsg;
	}



	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	
	




	public String getBrandName() {
		return brandName;
	}



	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}



	public String getBrandNameRequired() {
		return brandNameRequired;
	}



	public void setBrandNameRequired(String brandNameRequired) {
		this.brandNameRequired = brandNameRequired;
	}



	public String getGoodsNameRequired() {
		return goodsNameRequired;
	}



	public void setGoodsNameRequired(String goodsNameRequired) {
		this.goodsNameRequired = goodsNameRequired;
	}



	public String getGoodsCategoryRequired() {
		return goodsCategoryRequired;
	}



	public void setGoodsCategoryRequired(String goodsCategoryRequired) {
		this.goodsCategoryRequired = goodsCategoryRequired;
	}



	public void setGoodsList(List<WFGoods> goodsList) {
		this.goodsList = goodsList;
	}
	
	
	
	
	
	
	public long getGoodsId() {
		return goodsId;
	}



	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}



	public List<WFCategory> getCategoryList() {
		return ServiceFactory.getCategoryService().getSortedCategory();
	}



	public void setCategoryList(List<WFCategory> categoryList) {
		this.categoryList = categoryList;
	}



	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}

	
	


	public String getGoodsDesc() {
		return goodsDesc;
	}



	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	
	
	
	
	
	public WFGoods getGoods() {
		return goods;
	}



	public void setGoods(WFGoods goods) {
		this.goods = goods;
	}
	
	



	public String getCategoryName() {
		return categoryName;
	}



	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}



	public long getUpdateGoodsId() {
		return updateGoodsId;
	}



	public void setUpdateGoodsId(long updateGoodsId) {
		this.updateGoodsId = updateGoodsId;
	}

	
	


	public String getGoodsUnit() {
		return goodsUnit;
	}



	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}



	public List<WFBrand> getGoodsBrand() {
		WFGoods goods = goodsService.getGoods(goodsId);
		if (goods == null) {
			return null;
		}
		this.goods = goods;
		if (!goods.isBrandsLoad()) {
			goodsService.queryBrands(goods, 0, 20);
		}
		
		return goods.getBrands();
	}


	
	

	public String createGoods() {
		errMsg = null;
		goodsCategoryRequired = null;
		goodsNameRequired = null;
		if (name == null || name.trim().isEmpty()) {
			errMsg = "创建失败";
			goodsNameRequired ="请输入产品名称";
			return "failed";
		}
		
		if (categoryId <= 0) {
			errMsg = "创建失败";
			goodsCategoryRequired ="请输入产品所属分类";
			return "failed";
		}
		
		if (updateGoodsId > 0) {
			WFGoods goods = goodsService.getGoods(updateGoodsId);
			goods.setName(name);
			goods.setCate(findCategory(getCategoryList(), categoryId));
			goods.setType(type);
			goods.setGoodsDesc(goodsDesc);
			goods.setUnit(goodsUnit);
			goodsService.updateGoods(goods);
		} else {
			WFGoods goods = new WFGoods();
			goods.setName(name);
			goods.setCate(findCategory(getCategoryList(), categoryId));
			goods.setType(type);
			goods.setUnit(goodsUnit);
			goods.setGoodsDesc(goodsDesc);
			goodsService.addGoods(goods);
		}
		

		name = null;
		errMsg = null;
		goodsCategoryRequired= null;
		goodsNameRequired =null;
		categoryId = 0;
		updateGoodsId = 0;
		categoryName = null;
		goodsUnit = null;
		return "list";
	}
	
	
	public void removeGoods() {
		Result ret = goodsService.removeGoods(goodsService.getGoods(goodsId));
		switch (ret) {
		case ERR_EXIST_DAMAGE_RECORD:
			errMsg = "该产品存在报损记录，无法删除";
			break;
		case ERR_EXIST_INVENTORY_RECORD:
			errMsg = "该产品存在补货记录，无法删除";
			break;
		case ERR_EXIST_INVENTORY_REQUEST_RECORD:
			errMsg = "该产品存在店铺补货申请记录，无法删除";
			break;
		case SUCCESS:
			errMsg = "删除成功";
			break;
		default:
			errMsg = "删除错误";
			break;
		}
	}
	
	
	
	public String createGoodsBrand() {
		errMsg = null;
		brandNameRequired = null;
		if (brandName == null || brandName.trim().isEmpty()) {
			errMsg = "创建失败";
			brandNameRequired ="请输入品牌名称";
			return "brandcreatefailed";
		}
		
		if (goods == null) {
			errMsg = "创建失败";
			return "brandcreatefailed";
		}
		
		WFBrand brand = new WFBrand();
		brand.setName(brandName);
		brand.setGoods(goods);
		goodsService.addGoodsBrand(goods, brand);
		

		brandName = null;
		errMsg = null;
		brandNameRequired= null;
		return "brandlist";
	}
	
	
	public String viewGoodsBrands() {
		return "viewbrands";
	}
	
	public String gotoAddGoodsBrands() {
		goods = goodsService.getGoods(goodsId);
		return "addbrands";
	}
	
	
	public String gotoAddGoods() {
		errMsg = null;
		goodsCategoryRequired = null;
		goodsNameRequired = null;
		name = "";
		categoryId = 0;
		categoryName = "";
		goodsDesc = "";
		type = 0;
		updateGoodsId = 0;
		return "gotoaddgoods";
	}
	
	public String viewGoods() {
		WFGoods wfg = goodsService.getGoods(updateGoodsId);
		name = wfg.getName();
		categoryId = wfg.getCate().getId();
		categoryName = wfg.getCate().getName();
		goodsDesc = wfg.getGoodsDesc();
		return "updategoods";
	}
	
	
	private WFCategory findCategory(List<WFCategory> list, long cateId) {
		if (list == null || list.size() <= 0) {
			return null;
		}
		for (WFCategory wf : list) {
			if (wf.getId() == cateId) {
				return wf;
			}
			WFCategory sub = findCategory(wf.getSubs(), cateId);
			if (sub != null) {
				return sub;
			}
		}
		return null;
	}
	

}
