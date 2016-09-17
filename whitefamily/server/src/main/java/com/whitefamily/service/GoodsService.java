package com.whitefamily.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.whitefamily.po.Brand;
import com.whitefamily.po.Category;
import com.whitefamily.po.Goods;
import com.whitefamily.po.InventoryType;
import com.whitefamily.po.customer.User;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;

public class GoodsService extends BaseService implements IGoodsService {
	
	
	private static Map<Long, WFGoods> goodsCache;
	
	private static Map<Long, WFBrand> brandCache;
	
	private static CacheGoods cacheGoods = new CacheGoods();
	
	private List<WFGoods> cacheList;
	
	ICategoryService cateService;
	
	
	
	

	public GoodsService() {
		super();
		goodsCache = new HashMap<Long,WFGoods>(50); 
		brandCache= new HashMap<Long,WFBrand>(50); 
	}

	public ICategoryService getCateService() {
		return cateService;
	}

	public void setCateService(ICategoryService cateService) {
		this.cateService = cateService;
	}

	@Override
	public void addGoods(WFGoods goods) {
		Goods good = new Goods();
		good.setCate(goods.getCate());
		good.setName(goods.getName());
		good.setType(goods.getType());
		good.setUnit(goods.getUnit());
		goods.setAbbr(PinyinHelper.getShortPinyin(goods.getName()));
		good.setGoodsDesc(goods.getGoodsDesc());
		Category cate = new Category();
		cate.setId(goods.getCate().getId());
		good.setCate(cate);
		Session sess = getSession();
		Transaction tr = sess.beginTransaction();
		sess.save(good);
		tr.commit();
		sess.close();
		goods.setId(good.getId());
		goodsCache.put(Long.valueOf(goods.getId()), goods);
		cacheList.add(goods);
	}
	

	@Override
	public void updateGoods(WFGoods wfg) {
		Session sess = getSession();
		Goods good = (Goods)sess.get(Goods.class, wfg.getId());
		good.setCate(wfg.getCate());
		good.setName(wfg.getName());
		good.setType(wfg.getType());
		good.setUnit(wfg.getUnit());
		good.setGoodsDesc(wfg.getGoodsDesc());
		Category cate = new Category();
		cate.setId(wfg.getCate().getId());
		good.setCate(cate);
	
		Transaction tr = sess.beginTransaction();
		sess.save(good);
		tr.commit();
		sess.close();
	}

	@Override
	public void removeGoods(WFGoods wfg) {
		// TODO Auto-generated method stub

	}
	
	public WFGoods getGoods(long id) {
		if (goodsCache == null || goodsCache.size() <=0) {
			queryGoods(0, 300, -1);
		}
		return goodsCache.get(Long.valueOf(id));
	}
	
	
	public WFBrand getBrand(long id) {
		return brandCache.get(Long.valueOf(id));
	}

	@Override
	public void addGoodsBrand(WFGoods goods, WFBrand brand) {
		Brand br = new Brand();
		br.setName(brand.getName());
		br.setStyle(brand.getStyle());
		br.setGoods(goods);
		Session sess = getSession();
		Transaction tr = sess.beginTransaction();
		sess.save(br);
		tr.commit();
		sess.close();
		goods.addBrand(brand);

	}

	@Override
	public void removeGoodsBrand(WFGoods goods, WFBrand brand) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateGoodsPrice(WFGoods goods, User opter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateGoodsInventory(WFGoods goods, WFBrand brand, float price, int count, InventoryType it, User opter) {
		// TODO Auto-generated method stub

	}
	
	
	
	public List<WFGoods> queryGoods(int start, int count, int type) {
//		int newStart = start;
//		int newCount = start + count;
//		if (cacheGoods.start <= start && cacheGoods.end >= newCount) {
//			return cacheGoods.caches.subList(start, start + count); 
//		} else if (cacheGoods.start > start && cacheGoods.end >= newCount) {
//			synchronized(cacheGoods.startLock) {
//				newCount = cacheGoods.start - start;
//				cacheGoods.start -= newCount;
//			}
//			
// 		} else if (cacheGoods.start <= start && cacheGoods.end < newCount) {
// 			synchronized(cacheGoods.endLock) {
// 				newStart = cacheGoods.end;
// 				newCount = count + start - newStart;
// 			}
// 		} else {
// 			synchronized(cacheGoods.startLock) {
//				newCount = cacheGoods.start - start;
//				cacheGoods.start -= newCount;
//			}
// 			synchronized(cacheGoods.endLock) {
// 				newStart = cacheGoods.end;
// 				newCount = count + start - newStart;
// 			}
// 		}
//		
//		
		if (cacheList != null && cacheList.size() >0) {
			return cacheList;
		}
		Session sess = getSession();
//		
//		
//		if (newStart != start) {
//			Query query = sess.createQuery(" from Goods order by id ");
//			query.setFirstResult(newStart);
//			query.setMaxResults(start - newStart);
//			List<Goods> list = query.list();
//			List<WFGoods> wfList = new ArrayList<WFGoods>(list.size());
//			for (Goods g : list) {
//				WFGoods wf = new WFGoods(g);
//				wf.setCate(cateService.getCategory(g.getCate().getId()));
//				wfList.add(wf);
//			}
//			cacheGoods.caches.addAll(0, wfList);
//		} 
//		
//		if (newCount != count) {
//			Query query = sess.createQuery(" from Goods order by id ");
//			query.setFirstResult(newStart);
//			query.setMaxResults(newCount);
//			List<Goods> list = query.list();
//			List<WFGoods> wfList = new ArrayList<WFGoods>(list.size());
//			for (Goods g : list) {
//				WFGoods wf = new WFGoods(g);
//				wf.setCate(cateService.getCategory(g.getCate().getId()));
//				wfList.add(wf);
//			}
//			cacheGoods.caches.addAll(wfList);
//		}
//		
//		if (newStart == start && newCount == count) {
//			Query query = sess.createQuery(" from Goods order by id ");
//			query.setFirstResult(newStart);
//			query.setMaxResults(newCount);
//			List<Goods> list = query.list();
//			List<WFGoods> wfList = new ArrayList<WFGoods>(list.size());
//			for (Goods g : list) {
//				WFGoods wf = new WFGoods(g);
//				wf.setCate(cateService.getCategory(g.getCate().getId()));
//				wfList.add(wf);
//			}
//			cacheGoods.caches.addAll(wfList);
//		}
		
		String sql = type == -1 ? "from Goods order by id" : "from Goods g where g.type = ? order by id ";
		Query query = sess.createQuery(sql);
//		query.setFirstResult(start);
//		query.setMaxResults(start - count);
		if (type != -1) {
			query.setInteger(0, type);
		}
		List<Goods> list = query.list();
		List<WFGoods> wfList = new ArrayList<WFGoods>(list.size());
		for (Goods g : list) {
			WFGoods wf = new WFGoods(g);
			wf.setCate(cateService.getCategory(g.getCate().getId()));
			wfList.add(wf);
			goodsCache.put(wf.getId(), wf);
		}
//		
		sess.close();
//		int size = cacheGoods.caches.size();
//		return cacheGoods.caches.subList(start, size > start + count ? start + count : size);
		cacheList = wfList;
		return cacheList;
		
	}
	
	public List<WFGoods> queryGoods(WFCategory cate, int start, int count, int type) {
		List<WFCategory> list = new ArrayList<WFCategory>(1);
		list.add(cate);
		return queryGoods(list, start, count, type);
	}
	
	
	public List<WFGoods> queryGoods(List<WFCategory> cates, int start, int count, int type) {
		if (cates == null || cates.size() <= 0 ) {
			return null;
		}
		StringBuffer hsql = new StringBuffer();
		hsql.append(" from Goods g where ( ");
		int len =  cates.size();
		while(--len >= 0) {
			if (len == 0) {
				hsql.append("  g.cate = ? ) ");
			} else {
				hsql.append(" g.cate = ? or ");
			}
		}
		
		if (type != -1) {
			hsql.append(" and g.type = ? ");
		}
		
		hsql.append(" order by id ");
		Session sess = getSession();
		Query query = sess.createQuery(hsql.toString());
		len =  cates.size();
		while(--len >= 0) {
			WFCategory wf = cates.get(len);
			query.setLong(len, wf.getId());
		}
		if (type != -1) {
			query.setInteger(cates.size(), type);
		}
		query.setFirstResult(start);
		query.setMaxResults(count);
		query.setFetchSize(count);
		List<Goods> list = query.list();
		List<WFGoods> wfList = new ArrayList<WFGoods>(list.size());
		for (Goods g : list) {
			WFGoods wf = new WFGoods(g);
			wf.setCate(cateService.getCategory(g.getCate().getId()));
			wfList.add(wf);
		}
		
		sess.close();
		return wfList;
	}
	
	
	
	public List<WFBrand> queryBrands(WFGoods goods, int start, int count) {
		Session sess = getSession();
		Query query = sess.createQuery(" from Brand g where g.goods= ? order by id ");
		query.setLong(0, goods.getId());
		query.setFirstResult(start);
		query.setMaxResults(count);
		query.setFetchSize(count);
		List<Brand> list = query.list();
		for (Brand g : list) {
			WFBrand wf = new WFBrand(g);
			wf.setGoods(goods);
			goods.addBrand(wf);
			//put cache
			brandCache.put(wf.getId(), wf);
		}
		goods.setBrandsLoad(true);
		sess.close();
		return goods.getBrands();
	}

	
	
	
	static class CacheGoods {
		Object startLock = new Object();
		Object endLock = new Object();
		int start;
		int end;
		
		int newStartOffset;
		int newEndOffset;
		List<WFGoods> caches = new ArrayList<WFGoods>();
		
	}
}
