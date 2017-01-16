package com.whitefamily.web.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.whitefamily.service.ICategoryService;
import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.Result;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFGoodsVisible;

@ManagedBean(name = "categoryBean", eager = false)
@SessionScoped
public class CategoryBean {

	private Log logger = LogFactory.getLog(CategoryBean.class);
	private List<WFCategory> categoryList;
	private long categoryId;
	private String name;
	private int type;
	private long parentCategoryId;
	private WFCategory subCate;

	private String errMsg;
	private List<WFGoods> goodsList;
	private IGoodsService goodsService;
	
	ICategoryService service;

	public CategoryBean() {
		super();
		service = ServiceFactory.getCategoryService();
		service.getAllCategory();
		categoryList = service.getTopCategory();
		goodsService = ServiceFactory.getGoodsService();
		goodsVisible = new SelectItem[]{new SelectItem(WFGoodsVisible.DELIVERY_STAFF.ordinal()+"", "库管可见"),
				new SelectItem(WFGoodsVisible.SHOP.ordinal()+"", "店长可见"),
				new SelectItem(WFGoodsVisible.FRANCHISEE.ordinal()+"", "加盟商可见")};
	}
	
	
	private SelectItem[]  goodsVisible;

	
	

	public SelectItem[] getGoodsVisible() {
		return goodsVisible;
	}

	public List<WFCategory> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<WFCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public ICategoryService getService() {
		return service;
	}

	public void setService(ICategoryService service) {
		this.service = service;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String createCategory() {
		errMsg = null;
		if (name == null || name.trim().isEmpty()) {
			errMsg = "请输入分类名称";
			return "failed";
		}
		boolean addFlag = false;
		WFCategory wf = null;
		if (categoryId > 0) {
			wf = findCategory(categoryList, categoryId);
		} else {
			wf = new WFCategory();
			addFlag = true;
		}

		wf.setName(name);
		wf.setType(type);
		if (parentCategoryId > 0) {
			WFCategory wfParent = findCategory(categoryList, parentCategoryId);
			wf.setParent(wfParent);
		}
		if (addFlag) {
			service.addCategory(wf);
			if (categoryList == null) {
				categoryList = new ArrayList<WFCategory>();
			}
		} else {
			service.updateCategory(wf);
		}
		categoryId = 0;
		parentCategoryId = 0;
		name = null;
		//re-fetch category
		categoryList = service.getTopCategory();
		
		return "list";
	}

	public String editCategory() {
		errMsg = null;

		Long id = Long.parseLong(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("id"));

		WFCategory cate = findCategory(categoryList, id);
		if (cate == null) {
			errMsg = "异常错误, 没有找到分类信息";
			return "failed";
		}

		name = cate.getName();
		categoryId = cate.getId();
		type = cate.getType();
		parentCategoryId = cate.getParentId();
		//
		return "editcate";
	}

	public void removeCategory() {
		Result ret = service.removeCategory(service.getCategory(categoryId));
		switch (ret) {
		case ERR_EXIST_GOODS:
			errMsg = " 该产品分类下存在产品， 无法删除分类，请先删除该分类下产品后，再删除品类";
			break;
		case ERR_EXIST_PARENT_CATEGORY:
			errMsg = "存在下属分类， 无法删除";
			break;
		case SUCCESS:
			errMsg = "删除成功";
			break;
		default:
			errMsg = "删除错误";
		}
		categoryId = 0;
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
	
	
	
	public String updateCategoryOrder() {
		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] cate_ids = map.get("c_id");
		String[] cate_orders = map.get("c_count");
		

		if (cate_ids == null || cate_ids.length <= 0) {
			errMsg = "没有选择目录";
			return "updatecateorderfailed";
		}
		
		if (cate_orders == null || cate_orders.length <=0) {
			errMsg = "没有输入排序";
			return "updatecateorderfailed";
		}
		
		if (cate_orders.length != cate_ids.length) {
			errMsg = "目录和排序值不匹配";
			return "updatecateorderfailed";
		}
		
		Pattern pat = Pattern.compile("([0-9{1, 2}])");
		
		
		Long[] ids = new Long[cate_ids.length];
		int[] orders = new int[cate_orders.length];
		Matcher m  = null;
		for (int i = 0; i < ids.length; i++) {
			if (cate_ids[i] == null || cate_ids[i].isEmpty()) {
				errMsg = "请选择分类";
				return "updatecateorderfailed";
			}
			ids[i] = Long.parseLong(cate_ids[i]);
			m = pat.matcher(cate_orders[i]);
			if (!m.matches()) {
				errMsg = "排序必须为数字";
				return "updatecateorderfailed";
			}
			orders[i] = Integer.parseInt(cate_orders[i]);
		}
		
		Result ret = service.updateCateogryOrder(ids, orders);
		if (ret != Result.SUCCESS) {
			errMsg = "更新失败";
			return "updatecateorderfailed";
		}
		errMsg = null;
		return "list";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	
	public String showSubCategory() {
		logger.info("==>" + categoryList.size());
		
		subCate = findCategory(categoryList, categoryId);
		
		if (subCate != null && subCate.getParent() != null) {
			prepareGoods(subCate);
		} else {
			goodsList = null;
		}
		return "subcategory";
	}

	public WFCategory getSubCate() {
		return subCate;
	}
	
	
	
	public List<WFGoods> getGoodsList() {
		return goodsList;
	}

	public void prepareGoods(WFCategory cate) {
		if (cate == null) {
			return;
		}
		goodsList = goodsService.queryGoods(getAllSubCates(cate), 0, 200, 0);
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
	
	
}
