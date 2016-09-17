package com.whitefamily.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.whitefamily.service.ICategoryService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFCategory;

@ManagedBean(name = "categoryBean", eager = false)
@SessionScoped
public class CategoryBean {

	private List<WFCategory> categoryList;
	private long categoryId;
	private String name;
	private long parentCategoryId;

	private String errMsg;

	ICategoryService service;

	public CategoryBean() {
		super();
		service = ServiceFactory.getCategoryService();
		categoryList = service.getAllCategory();
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
		return "list";
	}

	public String editCategory() {
		errMsg = null;

		Long id = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));

		WFCategory cate = findCategory(categoryList, id);
		if (cate == null) {
			errMsg = "异常错误, 没有找到分类信息";
			return "failed";
		}

		name = cate.getName();
		categoryId = cate.getId();
		parentCategoryId = cate.getParentId();
		//
		return "editcate";
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
