package com.whitefamily.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.whitefamily.po.Brand;
import com.whitefamily.po.Category;
import com.whitefamily.po.Goods;
import com.whitefamily.po.InventoryType;
import com.whitefamily.po.Vendor;
import com.whitefamily.po.artifact.ArtifactProduct;
import com.whitefamily.po.artifact.ArtifactStaff;
import com.whitefamily.po.artifact.ArtifactStaffType;
import com.whitefamily.po.customer.User;
import com.whitefamily.service.vo.WFArtifact;
import com.whitefamily.service.vo.WFArtifact.StaffGoods;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFGoodsVisible;
import com.whitefamily.service.vo.WFVendor;

public class GoodsService extends BaseService implements IGoodsService {
	
	
	private static Log logger = LogFactory.getLog(GoodsService.class);
	
	private static Map<Long, WFGoods> goodsCache;
	
	private static Map<Long, WFBrand> brandCache;
	
	private static Map<Long, WFVendor> vendorCache;
	
	private static Map<String, WFBrand> brandNameCache;
	
	private static Map<String, WFVendor> vendorNameCache;
	
	private static List<WFBrand> brandsList;
	
	private static List<WFVendor> vendorsList;
	
	private static boolean vendorLoad;
	
	private static boolean brandLoad;
	
	private static CacheGoods cacheGoods = new CacheGoods();
	
	private List<WFGoods> cacheList;
	
	private List<WFArtifact> cacheArtifactList;
	
	ICategoryService cateService;
	
	
	
	

	public GoodsService() {
		super();
		goodsCache = new HashMap<Long,WFGoods>(100); 
		brandCache= new HashMap<Long,WFBrand>(100); 
		brandNameCache= new HashMap<String,WFBrand>(100); 
		vendorCache= new HashMap<Long,WFVendor>(100); 
		vendorNameCache= new HashMap<String,WFVendor>(100); 
		
		brandsList = new ArrayList<WFBrand>(100);
		vendorsList = new ArrayList<WFVendor>(100);
		
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
		good.setGoodsDesc(goods.getGoodsDesc());
		good.setPrice(goods.getPrice());
		goods.setAbbr(PinyinHelper.getShortPinyin(goods.getName()));
		Category cate = new Category();
		cate.setId(goods.getCate().getId());
		good.setCate(cate);
		Session sess = getSession();
		beginTransaction(sess);
		sess.save(good);
		commitTrans();
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
		good.setPrice(wfg.getPrice());
		good.setPrice1(wfg.getPrice1());
		good.setPrice2(wfg.getPrice2());
		good.setPrice3(wfg.getPrice3());
		good.setStock(wfg.getStock());
		good.setStockBar(wfg.getStockBar());
		good.setGoodsDesc(wfg.getGoodsDesc());
		Category cate = new Category();
		cate.setId(wfg.getCate().getId());
		good.setCate(cate);
	
		beginTransaction(sess);
		sess.save(good);
		commitTrans();
	}
	
	
	public Result updateGoodsStockBar(WFGoods wfg, float stockbar) {
		if (wfg == null) {
			return Result.ERR_NO_SUCH_RECORD;
		}
		this.getGoods(wfg.getId()).setStockBar(stockbar);
		Session sess = getSession();
		Goods good = (Goods)sess.get(Goods.class, wfg.getId());
		good.setStockBar(stockbar);
		beginTransaction(sess);
		sess.update(good);
		commitTrans();
		return Result.SUCCESS;
	}

	@Override
	public Result removeGoods(WFGoods wfg) {
		Session sess = getSession();
		Query query = sess.createQuery(" select count(*) from InventoryGoods where goods.id = ? ");
		query.setLong(0, wfg.getId());
		int count =((Long) query.uniqueResult()).intValue();
		if (count > 0) {
			return Result.ERR_EXIST_INVENTORY_RECORD;
		}
		
		query = sess.createQuery(" select count(*) from DamageReportGoods where goods.id = ? ");
		query.setLong(0, wfg.getId());
		count =((Long) query.uniqueResult()).intValue();
		if (count > 0) {
			return Result.ERR_EXIST_DAMAGE_RECORD;
		}
		
		
		query = sess.createQuery(" select count(*) from InventoryRequestGoods where goods.id = ? ");
		query.setLong(0, wfg.getId());
		count =((Long) query.uniqueResult()).intValue();
		if (count > 0) {
			return Result.ERR_EXIST_INVENTORY_REQUEST_RECORD;
		}
		
		beginTransaction(sess);
		Goods g = (Goods)sess.get(Goods.class, wfg.getId());
		sess.delete(g);
		commitTrans();
		
		goodsCache.remove(wfg.getId());
		cacheList.remove(wfg);
		return Result.SUCCESS;

	}
	
	public WFGoods getGoods(long id) {
		if (goodsCache == null || goodsCache.size() <=0) {
			queryGoods(0, IGoodsService.CATCH_SIZE, IGoodsService.VISIBLE_ALL);
		}
		return goodsCache.get(Long.valueOf(id));
	}
	
	
	public WFBrand getBrand(long id) {
		return brandCache.get(Long.valueOf(id));
	}
	
	
	public WFVendor getVendor(long id) {
		return vendorCache.get(Long.valueOf(id));
	}
	
	
	public WFBrand getBrand(String name) {
		return brandNameCache.get(name);
	}
	
	public WFVendor getVendor(String name) {
		return vendorNameCache.get(name);
	}
	
	
	public Result addVendor(WFVendor wfv) {
		if (wfv == null || vendorNameCache.get(wfv.getName()) != null) {
			return Result.ERR_VENDOR_EXISTS;
		}
		Session sess = getSession();
		beginTransaction(sess);
		Vendor v = new Vendor();
		v.setName(wfv.getName());
		sess.save(v);
		commitTrans();
		wfv.setId(v.getId());
		
		vendorNameCache.put(wfv.getName(), wfv);
		vendorCache.put(Long.valueOf(wfv.getId()), wfv);
		vendorsList.add(wfv);
		return Result.SUCCESS;
	}
	
	
	public Result addBrand(WFBrand wfb) {
		if (wfb == null || brandNameCache.get(wfb.getName()) != null) {
			return Result.ERR_BRAND_EXISTS;
		}
		Session sess = getSession();
		beginTransaction(sess);
		Brand v = new Brand();
		v.setName(wfb.getName());
		sess.save(v);
		commitTrans();
		wfb.setId(v.getId());
		
		brandNameCache.put(wfb.getName(), wfb);
		brandCache.put(Long.valueOf(wfb.getId()), wfb);
		brandsList.add(wfb);
		return Result.SUCCESS;
	}
	
	
	@Override
	public void updateGoodsPrice(WFGoods goods, float newPrice, float newPrice1,  float newPrice2,  float newPrice3, User opter) {
		if (goods == null) {
			return;
		}
		WFGoods wfg = goodsCache.get(Long.valueOf(goods.getId()));
		if (wfg == null) {
			logger.warn(" No such goods in cache :" + goods.getId() +"  name:"+ goods.getName());
			return;
		}
		
		wfg.setPrice(newPrice);
		wfg.setPrice1(newPrice1);
		wfg.setPrice2(newPrice2);
		wfg.setPrice3(newPrice2);
		updateGoods(wfg);
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
		if (cacheList != null && cacheList.size() >0 && type < WFGoodsVisible.DELIVERY_STAFF.ordinal()) {
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
		
		String sql = type == VISIBLE_ALL ? "from Goods order by id" : "from Goods g where g.type >= ? order by id ";
		Query query = sess.createQuery(sql);
//		query.setFirstResult(start);
//		query.setMaxResults(start - count);
		if (type != VISIBLE_ALL) {
			query.setInteger(0, type);
		}
		List<Goods> list = query.list();
		List<WFGoods> wfList = new ArrayList<WFGoods>(list.size());
		for (Goods g : list) {
			WFGoods wf = new WFGoods(g);
			wf.setCate(cateService.getCategory(g.getCate().getId()));
			wfList.add(wf);
			if (type == VISIBLE_ALL) {
				goodsCache.put(wf.getId(), wf);
			}
		}
		
		if (type == VISIBLE_ALL) {
			this.cacheList = wfList;
		}
			
	//		
	//		int size = cacheGoods.caches.size();
	//		return cacheGoods.caches.subList(start, size > start + count ? start + count : size);
		return wfList;
		
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
		
		if (type != VISIBLE_ALL) {
			hsql.append(" and g.type >= ? ");
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
		goods.clearBrandCache();
		for (Brand g : list) {
			WFBrand wf = new WFBrand(g);
			goods.addBrand(wf);
			//put cache
			brandCache.put(wf.getId(), wf);
		}
		goods.setBrandsLoad(true);
		sess.close();
		return goods.getBrands();
	}

	
	
	
	public void init() {
		Session sess = getSession();
		Query query = sess.createQuery(" from Brand  ");
		List<Brand> list = query.list();
		for (Brand b : list) {
			WFBrand wf = new WFBrand(b);
			brandCache.put(wf.getId(), wf);
			brandNameCache.put(wf.getName(), wf);
			brandsList.add(wf);
		}
		
		
		query = sess.createQuery(" from Vendor  ");
		List<Vendor> listv = query.list();
		for (Vendor v : listv) {
			WFVendor wf = new WFVendor(v);
			vendorCache.put(wf.getId(), wf);
			vendorNameCache.put(wf.getName(), wf);
			vendorsList.add(wf);
		}
		
		this.queryGoods(0, IGoodsService.CATCH_SIZE, IGoodsService.VISIBLE_ALL);
		
		this.loadArtifacts();
	}
	
	
	

	public List<WFBrand> searchBrand(String name, int count) {
		if (name == null || name.isEmpty()) {
			int endsize = brandsList.size();
			return brandsList.subList(0, count > endsize ? endsize : count);
		}
		
		List<WFBrand> list = new ArrayList<WFBrand>(count);
		WFBrand wfb = null;
		for (int i = 0; i < brandsList.size(); i++) {
			wfb = brandsList.get(i);
			if (wfb.getName().contains(name)) {
				list.add(wfb);
				if (list.size() >= count) {
					break;
				}
			}
		}
		return list;
		
	}
	
	
	public List<WFVendor> searchVendor(String name, int count) {
		if (name == null || name.isEmpty()) {
			int endsize = vendorsList.size();
			return vendorsList.subList(0, count > endsize ? endsize : count);
		}
		
		List<WFVendor> list = new ArrayList<WFVendor>(count);
		WFVendor wfb = null;
		for (int i = 0; i < vendorsList.size(); i++) {
			wfb = vendorsList.get(i);
			if (wfb.getName().contains(name)) {
				list.add(wfb);
				if (list.size() >= count) {
					break;
				}
			}
		}
		return list;
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
	
	

	@Override
	public Result createWFArtifact(WFArtifact wff) {
		
		ArtifactProduct ap = new ArtifactProduct();
		ap.setDesc(wff.getDesc());
		Session sess = getSession();
		beginTransaction(sess);
		sess.save(ap);
		List<StaffGoods> input = wff.getStaffGoods(ArtifactStaffType.INPUT);
		if (input != null) {
			ArtifactStaff as = null;
			StaffGoods sg = null;
			for (int i = 0; i < input.size(); i++) {
				sg = input.get(i);
				as = new ArtifactStaff();
				as.setArtifact(ap);
				as.setType(ArtifactStaffType.INPUT);
				as.setProductId(sg.wfg.getId());
				as.setStyle(sg.getStyle());
				as.setMinalProduceUnit(sg.getMinialProduceUnit());
				as.setUnit(sg.unit);
				ap.addStaffs(as);
				sess.save(as);
			}
		} else {
			this.rollbackTrans();
			return Result.ERR_ARTIFACT_CREATE_FAILED;
		}
		
		
		List<StaffGoods> output = wff.getStaffGoods(ArtifactStaffType.OUTPUT);
		if (output != null) {
			ArtifactStaff as = null;
			StaffGoods sg = null;
			for (int i = 0; i < output.size(); i++) {
				sg = output.get(i);
				as = new ArtifactStaff();
				as.setArtifact(ap);
				as.setType(ArtifactStaffType.OUTPUT);
				as.setProductId(sg.wfg.getId());
				as.setStyle(sg.getStyle());
				as.setMinalProduceUnit(sg.getMinialProduceUnit());
				as.setUnit(sg.unit);
				ap.addStaffs(as);
				sess.save(as);
			}
		} else {
			this.rollbackTrans();
			return Result.ERR_ARTIFACT_CREATE_FAILED;
		}
		
		List<StaffGoods> procuced = wff.getStaffGoods(ArtifactStaffType.PRODUCED);
		if (procuced != null) {
			ArtifactStaff as = null;
			StaffGoods sg = null;
			for (int i = 0; i < procuced.size(); i++) {
				sg = procuced.get(i);
				as = new ArtifactStaff();
				as.setArtifact(ap);
				as.setType(ArtifactStaffType.PRODUCED);
				as.setProductId(sg.wfg.getId());
				as.setStyle(sg.getStyle());
				as.setMinalProduceUnit(sg.getMinialProduceUnit());
				as.setUnit(sg.unit);
				ap.addStaffs(as);
				sess.save(as);
			}
		}
		
		commitTrans();
		wff.setId(ap.getId());
		if (cacheArtifactList != null && cacheArtifactList.size() > 0) {
			synchronized(cacheArtifactList) {
				cacheArtifactList.add(wff);
			}
		}
		return Result.SUCCESS;
	}
	
	
	public WFArtifact loadArtifact(long id) {
		Session sess = getSession();
		ArtifactProduct ap = (ArtifactProduct)sess.get(ArtifactProduct.class, id);
		if (ap == null) {
			return null;
		}
		
		WFArtifact wfa = new WFArtifact();
		wfa.setId(ap.getId());
		wfa.setDesc(wfa.getDesc());
		Set<ArtifactStaff> sets = ap.getStaffs();
		WFGoods wfg = null;
		for (ArtifactStaff as : sets) {
			wfg = this.getGoods(as.getProductId());
			wfa.addWFGoods(as.getType(), wfg, as.getUnit(), as.getMinalProduceUnit(), as.getStyle());
		}
		return wfa;
	}
	
	
	public List<WFArtifact> loadArtifacts() {
		if (cacheArtifactList != null && cacheArtifactList.size() > 0) { 
			return cacheArtifactList;
		}
		Session sess = getSession();
		Query query = sess.createQuery(" from ArtifactProduct");
		List<ArtifactProduct> apList = query.list();
		List<WFArtifact> list = new ArrayList<WFArtifact>(apList.size());
		
		WFArtifact wfa = null;
		for (ArtifactProduct ap : apList) {
			wfa = new WFArtifact();
			wfa.setDesc(ap.getDesc());
			wfa.setId(ap.getId());
			list.add(wfa);
			for (ArtifactStaff as : ap.getStaffs()) {
				wfa.addWFGoods(as.getType(), getGoods(as.getProductId()), as.getUnit(), as.getMinalProduceUnit(), as.getStyle());
			}
		}
		cacheArtifactList = list;
		return cacheArtifactList;
		
	}
	
	
	public List<WFArtifact> searchWFArtifactGoods(String name) {
		List<WFArtifact> matchedList = new ArrayList<WFArtifact>();
		List<WFArtifact> list = loadArtifacts();
		for (WFArtifact wfa : list) {
			boolean flag = false;
			for (StaffGoods sg : wfa.getTargetGoods()) {
				if (sg.wfg.getName().contains(name) || sg.wfg.getAbbr().contains(name)) {
					flag = true;
					break;
				}
			}
			if (flag) {
				matchedList.add(wfa);
			}
		}
		return matchedList;
	}
}
