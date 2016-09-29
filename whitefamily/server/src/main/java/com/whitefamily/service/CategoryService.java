package com.whitefamily.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.whitefamily.po.Category;
import com.whitefamily.service.vo.WFCategory;

public class CategoryService extends BaseService implements ICategoryService {
	
	private static Map<Long, WFCategory> cateCache;
	private static List<WFCategory> orderCategoryList;
	private static List<WFCategory> allCategoryList;
	
	
	
	
	public CategoryService() {
		super();
		cateCache = new HashMap<Long,WFCategory>(50); 
		
	}

	public WFCategory getCategory(long cateId) {
		if (cateCache.isEmpty()) {
			this.getSortedCategory();
		}
		return cateCache.get(Long.valueOf(cateId));
	}

	@Override
	public WFCategory addCategory(WFCategory ca) {
		if (ca == null) {
			return null;
		}
		Category cate = new Category();
		cate.setLevel(ca.getLevel());
		cate.setName(ca.getName());
		cate.setType(ca.getType());
		cate.setParentId(ca.getParentId());
		ca.setAbbr(PinyinHelper.getShortPinyin(ca.getName()));
		Session sess = openSession();
		Transaction tr = sess.beginTransaction();
		sess.save(cate);
		tr.commit();
		sess.close();
		ca.setId(cate.getId());
		WFCategory wfParent = cateCache.get(ca.getParentId());
		if (wfParent != null) {
			wfParent.addSubCategory(ca);
			ca.setParent(wfParent);
		} else {
			if (orderCategoryList != null) {
				orderCategoryList.add(ca);
			}
		}
		allCategoryList.add(ca);
		return ca;
	}

	@Override
	public Result removeCategory(WFCategory ca) {
		if (ca == null) {
			return Result.SUCCESS;
		}
		
		
		Session sess = openSession();
		Query query = sess.createQuery(" select count(*) from Goods where cate.id = ? ");
		query.setLong(0, ca.getId());
		int count =((Long) query.uniqueResult()).intValue();
		if (count > 0) {
			return Result.ERR_EXIST_GOODS;
		}
		
		
		query = sess.createQuery(" select count(*) from Category where parentId = ? ");
		query.setLong(0, ca.getId());
		count =((Long) query.uniqueResult()).intValue();
		if (count > 0) {
			return Result.ERR_EXIST_PARENT_CATEGORY;
		}
		
		Category cate = (Category)sess.get(Category.class, ca.getId());
		Transaction tr = sess.beginTransaction();
		sess.delete(cate);
		tr.commit();
		
		WFCategory wfCache = cateCache.remove(ca.getId());
		if (wfCache != null) {
			if (wfCache.getParent() != null) {
				wfCache.getParent().removeSubCategory(wfCache);
			}
		}
		allCategoryList.remove(wfCache);
		
		return Result.SUCCESS;

	}

	@Override
	public void updateCategory(WFCategory ca) {
		
		
		Session sess = openSession();
		Category cate = (Category)sess.get(Category.class, ca.getId());
		cate.setLevel(ca.getLevel());
		cate.setName(ca.getName());
		cate.setType(ca.getType());
		cate.setParentId(ca.getParentId());
		
		Transaction tr = sess.beginTransaction();
		sess.update(cate);
		tr.commit();
	}

	@Override
	public void updateCategoryParent(WFCategory ca, WFCategory newParent) {
		WFCategory oldParent = ca.getParent();
		if (oldParent != null) {
			oldParent.removeSubCategory(ca);
		}
		ca.setParent(newParent);
		newParent.addSubCategory(ca);
		
		Session sess = openSession();
		Category cate = (Category)sess.get(Category.class, ca.getId());
		cate.setParentId(ca.getParentId());
		
		Transaction tr = sess.beginTransaction();
		sess.update(cate);
		tr.commit();
		sess.close();
		
		WFCategory wfCache = cateCache.remove(ca.getId());
		if (wfCache != null) {
			if (wfCache.getParent() != null) {
				wfCache.getParent().removeSubCategory(wfCache);
			}
			if (newParent != null) {
				WFCategory wfParent = cateCache.get(newParent.getId());
				if (wfParent != null) {
					wfParent.addSubCategory(ca);
					ca.setParent(wfParent);
				}
			}
		} else {
			//TODO log no cache illegal
		}

	}

	@Override
	public List<WFCategory> getTopCategory() {
		Session sess = openSession();
		Query query = sess.createQuery(" from Category where level = ? order by id");
		query.setInteger(0, 0);
		List<Category> list = (List<Category>)query.list();
		List<WFCategory> newList = new ArrayList<WFCategory>(list.size());
		for (Category c : list) {
			WFCategory wfc = new WFCategory(c);
			newList.add(wfc);
		}
		return newList;
	}
	
	
	
	public List<WFCategory> getAllCategory() {
		if (allCategoryList == null) {
			getSortedCategory();
		}
		return allCategoryList;
	}
	
	

	@Override
	public List<WFCategory> getSortedCategory() {
		if (orderCategoryList != null) {
			return orderCategoryList;
		} 
		

		if (allCategoryList == null) {
			allCategoryList = new ArrayList<WFCategory>(100); 
		}
		Session sess = openSession();
		Query query = sess.createQuery(" from Category order by level");
		
		List<Category> list = (List<Category>)query.list();
		List<List<WFCategory>> levelList = new ArrayList<List<WFCategory>>(20);
		
		int topLevel = 0;
		for (Category c : list) {
			WFCategory wfc = new WFCategory(c);
			allCategoryList.add(wfc);
			cateCache.put(Long.valueOf(wfc.getId()), wfc);
			if (levelList.size() <= wfc.getLevel()) {
				List<WFCategory> newList =  new ArrayList<WFCategory>(10);
				levelList.add(wfc.getLevel(), newList);
			}
			levelList.get(wfc.getLevel()).add(wfc);
			
			if (wfc.getLevel() <= topLevel) {
				continue;
			}
			
			List<WFCategory> parentList = levelList.get(wfc.getLevel() - 1);
			for (WFCategory parent : parentList) {
				if (wfc.getParentId() == parent.getId()) {
					wfc.setParent(parent);
					parent.addSubCategory(wfc);
					break;
				}
			}
			
		}
		orderCategoryList = (levelList.size() > topLevel ?  levelList.get(topLevel) : null);
		return orderCategoryList;
	}

	
	
}
