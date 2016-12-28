package com.whitefamily.web.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.whitefamily.po.customer.Role;
import com.whitefamily.service.ICategoryService;
import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFGoodsVisible;


@ManagedBean(name = "mallBean", eager = false)
@SessionScoped
public class MallBean {
	
	
	private ICategoryService cateservice;
	private IGoodsService goodsService;
	
	private List<WFCategory> categoryList;
	private List<WFCategory> sortedList;
	
	private WFCategory currentCate;
	private List<WFGoods> goodsList;
	
	private long cateId;
	private String searchText;
	
	
	@ManagedProperty("#{userBean}")
	private UserBean userBean;
	
	public MallBean() {
		cateservice = ServiceFactory.getCategoryService();
		goodsService = ServiceFactory.getGoodsService();
		
		categoryList = cateservice.getTopCategory();
		Collections.sort(categoryList);
		sortedList = cateservice.getSortedCategory();
		
		if (categoryList.size() > 0) {
			currentCate = sortedList.get(0);
			cateId = currentCate.getId();
		}
		
	}
	
	
	
	private void updateGoodsList(WFCategory cate) {
		goodsList = goodsService.queryGoods(getAllSubCates(cate), 0, IGoodsService.CATCH_SIZE,
				userBean.getUser().getRole() == Role.FRANCHISEE ? WFGoodsVisible.FRANCHISEE.ordinal() : WFGoodsVisible.SHOP.ordinal());
	}
	
	
	private List<WFCategory> getAllSubCates(WFCategory cate) {
		if (cate == null) {
			return null;
		}
		 List<WFCategory> list = new ArrayList<WFCategory>(20);
		 Stack<WFCategory> stack = new Stack<WFCategory>();
		 WFCategory el = cate;
		 list.add(el);
		 while (el != null) {
			 if (el.getSubs() != null) {
				 for (WFCategory wf : el.getSubs()) {
					 stack.push(wf);
					 list.add(wf);
				 }
			 }
			 if (!stack.isEmpty()) {
				 el = stack.pop();
			 } else {
				 el = null;
			 }
		 }
		 return list;
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



	public long getCateId() {
		return cateId;
	}



	public void setCateId(long cateId) {
		this.cateId = cateId;
		currentCate = findCategory(sortedList, cateId);
		updateGoodsList(currentCate);
	}


	
	

	public List<WFCategory> getCategoryList() {
		return categoryList;
	}



	public void setCategoryList(List<WFCategory> categoryList) {
		this.categoryList = categoryList;
	}



	public List<WFGoods> getGoodsList() {
		return goodsList;
	}



	public void setGoodsList(List<WFGoods> goodsList) {
		this.goodsList = goodsList;
	}



	public String getSearchText() {
		return searchText;
	}



	public void setSearchText(String searchText) {
		this.searchText = searchText;
		int type = WFGoodsVisible.FRANCHISEE.ordinal();
		if (this.userBean.getUser() != null && this.userBean.getUser().getRole() == Role.MANAGER) {
			type = WFGoodsVisible.SHOP.ordinal();
		}
		List<WFGoods> gList = goodsService.queryGoods(0, IGoodsService.CATCH_SIZE, IGoodsService.VISIBLE_ALL);
	
		List<WFGoods> list = new ArrayList<WFGoods>();
		if (this.searchText != null || !this.searchText.isEmpty()) {
			int len = gList.size();
			Pattern p = Pattern.compile("(" + searchText+ ")");
			WFGoods wfg = null;
			for (int i = 0; i < len; i++) {
				wfg = gList.get(i);
				if (p.matcher(wfg.getName()).find() && wfg.getType() >= type) {
					list.add(wfg);
				}
			}
		}
		
		goodsList = list;
	}



	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
		updateGoodsList(currentCate);
	}



	public UserBean getUserBean() {
		return userBean;
	}
	
	
	

}
