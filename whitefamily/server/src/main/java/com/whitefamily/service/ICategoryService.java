package com.whitefamily.service;

import java.util.List;

import com.whitefamily.service.vo.WFCategory;

public interface ICategoryService {
	
	
	public WFCategory getCategory(long cateId);

	public WFCategory addCategory(WFCategory ca);

	public Result removeCategory(WFCategory ca);

	public void updateCategory(WFCategory ca);

	public void updateCategoryParent(WFCategory ca, WFCategory newParent);
	
	
	public List<WFCategory> getTopCategory();
	
	public List<WFCategory> getSortedCategory();
	
	
	public List<WFCategory> getAllCategory();
	
	
}



