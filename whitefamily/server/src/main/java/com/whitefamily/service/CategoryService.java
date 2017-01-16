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
		cate.setOrder(0);
		ca.setAbbr(PinyinHelper.getShortPinyin(ca.getName()));
		Session sess = openSession();
		beginTransaction(sess);
		sess.save(cate);
		commitTrans();
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
		cateCache.put(Long.valueOf(ca.getId()), ca);
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
		beginTransaction(sess);
		sess.delete(cate);
		commitTrans();
		
		WFCategory wfCache = cateCache.remove(ca.getId());
		if (wfCache != null) {
			if (wfCache.getParent() != null) {
				wfCache.getParent().removeSubCategory(wfCache);
			}
		}
		allCategoryList.remove(wfCache);
		cateCache.remove(Long.valueOf(wfCache.getId()));
		
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
		cate.setOrder(ca.getOrder());
		
		beginTransaction(sess);
		sess.update(cate);
		commitTrans();
		//FIXME update cache
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
		
		beginTransaction(sess);
		sess.update(cate);
		commitTrans();
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
			WFCategory wfc = getCategory(c.getId());
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
	
	
	
	
	public Result updateCateogryOrder(Long[] ids, int[] orders) {
		if (ids == null || orders == null) {
			return Result.ERR_GENERIC_FAILED;
		}
		
		if (ids.length != orders.length) {
			return Result.ERR_CATEGORY_UPDATE_MIMATCH;
		}
		
		Session sess = getSession();
		Category cate  = null;
		WFCategory cache = null;
		beginTransaction(sess);
		for (int idx = 0; idx < ids.length; idx++) {
			Long lid = ids[idx];
			int order = orders[idx];
			cate = (Category)sess.get(Category.class, lid);
			if (cate == null) {
				this.rollbackTrans();
				return Result.ERR_GENERIC_FAILED;
			}
			cate.setOrder(order);
			sess.update(cate);
			
			cache  = getCategory(lid);
			if (cache != null) {
				cache.setOrder(order);
			}
		}
		commitTrans();
		
		
		
		
		return Result.SUCCESS;
	}

	
	
}
