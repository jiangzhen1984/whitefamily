package com.whitefamily.web.bean;

import java.util.ArrayList;
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

	private static final int PAGE_COUNT = 200;

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
	private float goodsPrice;
	private String goodsNameRequired;
	private String goodsCategoryRequired;

	// for brand
	private long brandId;
	private String brandName;
	private String brandNameRequired;
	private String brandUnit;
	private String brandSubUnit;
	private int brandSubCount;
	private String brandCalculation;

	private List<WFCategory> categoryList;

	private long goodsId;

	private WFGoods goods;

	// for update goods
	private long updateGoodsId;

	// for filter goods
	private String filterGoodsName;
	private String filterCateName;
	private long filterCateId;

	public GoodsBean() {
		goodsService = ServiceFactory.getGoodsService();
		filterGoods();
	}

	public List<WFGoods> getGoodsList() {
		return goodsList;
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

	public float getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getBrandUnit() {
		return brandUnit;
	}

	public void setBrandUnit(String brandUnit) {
		this.brandUnit = brandUnit;
	}

	public String getBrandSubUnit() {
		return brandSubUnit;
	}

	public void setBrandSubUnit(String brandSubUnit) {
		this.brandSubUnit = brandSubUnit;
	}

	public int getBrandSubCount() {
		return brandSubCount;
	}

	public void setBrandSubCount(int brandSubCount) {
		this.brandSubCount = brandSubCount;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getBrandCalculation() {
		return brandCalculation;
	}

	public void setBrandCalculation(String brandCalculation) {
		this.brandCalculation = brandCalculation;
	}

	public String getFilterGoodsName() {
		return filterGoodsName;
	}

	public void setFilterGoodsName(String goodsName) {
		this.filterGoodsName = goodsName;
	}

	public long getFilterCateId() {
		return filterCateId;
	}

	public void setFilterCateId(long filterCateId) {
		this.filterCateId = filterCateId;
	}
	
	

	public String getFilterCateName() {
		return filterCateName;
	}

	public void setFilterCateName(String filterCateName) {
		this.filterCateName = filterCateName;
	}

	public void filterGoods() {
		// filter goods
		List<WFGoods> newList = new ArrayList<WFGoods>(PAGE_COUNT);

		List<WFGoods> dbList = goodsService.queryGoods((pageNo - 1)
				* PAGE_COUNT, PAGE_COUNT, -1);
		if (this.filterCateId > 0) {
			int len = dbList.size();
			boolean matchFlag = false;
			for (int i = 0; i < len; i++) {
				WFGoods wf = dbList.get(i);
				WFCategory wfc = (WFCategory) wf.getCate();
				while (wfc != null) {
					if (wfc.getId() == filterCateId) {
						newList.add(wf);
						matchFlag = true;
						break;
					}
					wfc = wfc.getParent();
				}

			}
			this.goodsList = newList;
			return;
		}

		if (filterGoodsName != null && !this.filterGoodsName.isEmpty()) {
			int len = dbList.size();
			for (int i = 0; i < len; i++) {
				WFGoods wf = dbList.get(i);
				if (wf.getName().contains(this.filterGoodsName)
						|| wf.getAbbr().contains(this.filterGoodsName)) {
					newList.add(wf);
				}
			}
			this.goodsList = newList;
			return;
		}
		
		this.goodsList = dbList;
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
			goodsNameRequired = "请输入产品名称";
			return "failed";
		}

		if (categoryId <= 0) {
			errMsg = "创建失败";
			goodsCategoryRequired = "请输入产品所属分类";
			return "failed";
		}

		if (updateGoodsId > 0) {
			WFGoods goods = goodsService.getGoods(updateGoodsId);
			goods.setName(name);
			goods.setCate(findCategory(getCategoryList(), categoryId));
			goods.setType(type);
			goods.setGoodsDesc(goodsDesc);
			goods.setUnit(goodsUnit);
			goods.setPrice(goodsPrice);
			goodsService.updateGoods(goods);
		} else {
			WFGoods goods = new WFGoods();
			goods.setName(name);
			goods.setCate(findCategory(getCategoryList(), categoryId));
			goods.setType(type);
			goods.setUnit(goodsUnit);
			goods.setPrice(goodsPrice);
			goods.setGoodsDesc(goodsDesc);
			goodsService.addGoods(goods);
		}

		name = null;
		errMsg = null;
		goodsCategoryRequired = null;
		goodsNameRequired = null;
		categoryId = 0;
		updateGoodsId = 0;
		goodsPrice = 0;
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

	public String createOrSaveGoodsBrand() {
		errMsg = null;
		brandNameRequired = null;
		if (brandName == null || brandName.trim().isEmpty()) {
			errMsg = "操作失败";
			brandNameRequired = "请输入品牌名称";
			return "brandcreatefailed";
		}

		if (goods == null) {
			errMsg = "操作失败，没有关联产品";
			return "brandcreatefailed";
		}
		WFBrand brand = null;
		if (brandId <= 0) {
			brand = new WFBrand();
		} else {
			brand = findBrand(goods);
			if (brand == null) {
				errMsg = "操作失败，没有关联产品";
				return "brandviewfailed";
			}
		}
		brand.setName(brandName);
		brand.setGoods(goods);
		brand.setSubCount(brandSubCount);
		brand.setSubUnit(brandSubUnit);
		brand.setUnit(brandUnit);
		brand.setCalculation(brandCalculation);
		if (brandId <= 0) {
			goodsService.addGoodsBrand(goods, brand);
		} else {
			goodsService.updateGoodsBrand(goods, brand);
		}

		brandName = null;
		errMsg = null;
		brandNameRequired = null;
		brandSubUnit = null;
		brandUnit = null;
		brandSubCount = 0;
		brandId = 0;
		brandCalculation = null;
		return "brandlist";
	}

	private WFBrand findBrand(WFGoods goods) {
		List<WFBrand> brlist = goods.getBrands();
		if (brlist == null) {
			return null;
		}
		for (WFBrand wfb : brlist) {
			if (wfb.getId() == brandId) {
				return wfb;
			}
		}
		return null;
	}

	public String viewGoodsBrands() {
		return "viewbrands";
	}

	public String gotoAddGoodsBrands() {
		goods = goodsService.getGoods(goodsId);
		return "addbrands";
	}

	public String gotoUpdateBrands() {
		WFBrand wfb = findBrand(goods);
		if (wfb == null) {
			errMsg = "操作失败，没有关联产品";
			return "brandviewfailed";
		}
		brandName = wfb.getName();
		brandSubCount = wfb.getSubCount();
		brandSubUnit = wfb.getSubUnit();
		brandUnit = wfb.getUnit();
		return "gotoUpdateBrands";
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
		goodsPrice = 0;
		goodsUnit = "";
		updateGoodsId = 0;
		return "gotoaddgoods";
	}

	public String viewGoods() {
		WFGoods wfg = goodsService.getGoods(updateGoodsId);
		name = wfg.getName();
		categoryId = wfg.getCate().getId();
		categoryName = wfg.getCate().getName();
		goodsDesc = wfg.getGoodsDesc();
		goodsPrice = wfg.getPrice();
		goodsUnit = wfg.getUnit();
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
