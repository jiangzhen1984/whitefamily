package com.whitefamily.web.bean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.whitefamily.service.CategoryService;
import com.whitefamily.service.ICategoryService;
import com.whitefamily.service.vo.WFCategory;

@ManagedBean(name = "damageBean", eager = false)
@RequestScoped
public class DamageBean {
	
	
	ICategoryService service;
	
	
	private float quality;
	private String shopName;
	private long shopId;
	private List<WFCategory> categoryList;
	private String error;
	private long categoryId;
	
	
	
	public DamageBean() {
		super();
		 service = new CategoryService();
		 categoryList = service.getSortedCategory();
	}

	public float getQuality() {
		return quality;
	}

	public void setQuality(float quality) {
		this.quality = quality;
	}
	
	
	
	
	public String getShopName() {
		return "测试店铺";
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public long getShopId() {
		return 1;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	
	

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	
	

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<WFCategory> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<WFCategory> categoryList) {
		this.categoryList = categoryList;
	}

}
