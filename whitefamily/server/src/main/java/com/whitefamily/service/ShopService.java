package com.whitefamily.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.whitefamily.ServerConstants;
import com.whitefamily.po.DamageReportGoods;
import com.whitefamily.po.DamageReportRecord;
import com.whitefamily.po.DamageStatus;
import com.whitefamily.po.InventoryGoods;
import com.whitefamily.po.InventoryRequestGoods;
import com.whitefamily.po.InventoryRequestRecord;
import com.whitefamily.po.InventoryStatus;
import com.whitefamily.po.InventoryType;
import com.whitefamily.po.InventoryUpdateRecord;
import com.whitefamily.po.Shop;
import com.whitefamily.po.delivery.DeliveryRecord;
import com.whitefamily.po.delivery.DeliveryRecordGoods;
import com.whitefamily.po.delivery.DeliverySupplierConfiguration;
import com.whitefamily.po.delivery.InternalDeliveryRecord;
import com.whitefamily.po.delivery.InternalDeliveryRecordGoods;
import com.whitefamily.po.incoming.Delivery;
import com.whitefamily.po.incoming.GroupOn;
import com.whitefamily.po.incoming.Incoming;
import com.whitefamily.po.incoming.OperationCost;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFDamageReport;
import com.whitefamily.service.vo.WFDelivery;
import com.whitefamily.service.vo.WFDelivery.Item;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFIncoming.DeliveryItem;
import com.whitefamily.service.vo.WFIncoming.GroupOnItem;
import com.whitefamily.service.vo.WFInventory;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFManager;
import com.whitefamily.service.vo.WFOperationCost;
import com.whitefamily.service.vo.WFShop;
import com.whitefamily.service.vo.WFShopInventoryCost;
import com.whitefamily.service.vo.WFSupplierMapping;
import com.whitefamily.service.vo.WFUser;

public class ShopService extends BaseService implements IShopService {

	private static Log logger = LogFactory.getLog("ShopService");
	private static boolean cachedShop;
	private static Map<Long, WFShop> shopCache;
	

	private IGoodsService goodsService;
	private IUserService userService;
	private ISupplierService supplierService;
	

	public IGoodsService getGoodsService() {
		return goodsService;
	}

	public void setGoodsService(IGoodsService goodsService) {
		this.goodsService = goodsService;
	}

	
	public ISupplierService getSupplierService() {
		return supplierService;
	}

	public void setSupplierService(ISupplierService supplierService) {
		this.supplierService = supplierService;
	}
	
	
	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public ShopService() {
		super();
		shopCache = new HashMap<Long, WFShop>();
	}

	@Override
	public WFShop getShop(long id) {
		WFShop wf = shopCache.get(Long.valueOf(id));
		if (wf == null) {
			Session sess = getSession();
			Shop s  = (Shop)sess.get(Shop.class, id);
			if (s != null) {
				wf = new WFShop(s);
				shopCache.put(id, wf);
			}
		}
		
		//Do not close session, let proxy to close session
		
		return wf;
	}
	
	@Override
	public WFShop addShop(WFShop shop) {
		Shop s = new Shop();
		s.setName(shop.getName());
		s.setAddress(shop.getAddress());
		
		Session sess = openSession();
		Transaction tr = sess.beginTransaction();
		sess.save(s);
		tr.commit();
		sess.close();
		shop.setId(s.getId());
		shopCache.put(s.getId(), shop);
		return shop;
	}
	
	public WFShop updateShop(WFShop shop) {
		if (shop == null) {
			throw new NullPointerException(" shop is null");
		}
		Session sess = getSession();
		Shop s = (Shop)sess.get(Shop.class, shop.getId());
		if (s == null) {
			sess.close();
			throw new RuntimeException(" no such shop:" + shop.getId());
		}
		
		s.setName(shop.getName());
		s.setAddress(shop.getAddress());
		Transaction tr = sess.beginTransaction();
		sess.update(s);
		tr.commit();
		sess.close();
		return shop;
	}

	@Override
	public void removeShop(WFShop shop) {
		Session sess = getSession();
		Shop s = (Shop)sess.get(Shop.class, shop.getId());
		Transaction tr = sess.beginTransaction();
		sess.delete(s);
		tr.commit();
		shopCache.remove(s.getId());
	}
	
	
	public WFShop getShop(WFManager wfm) {
		WFShop wfs = new WFShop();
		wfs.setId(wfm.getShopId());
		wfs.setName(wfm.getShopName());
		wfs.setAddress(wfm.getShopAddress());
		return wfs;
	}


	
	public List<WFShop> getShopList() {
		if (cachedShop) {
			return new ArrayList<WFShop>(shopCache.values());
		}
		Session sess = openSession();
		Query query = sess.createQuery(" from Shop ");
		List<Shop> shopList = query.list();
		List<WFShop> newShopList = new ArrayList<WFShop>(shopList.size());
		for (Shop s : shopList) {
			WFShop wfs = new WFShop(s);
			shopCache.put(s.getId(), wfs);
			newShopList.add(wfs);
		}
		cachedShop = true;
		return newShopList;
	}
	
	
	
	public List<WFDamageReport> queryDamageReport(int start, int count) {
		return queryDamageReport(start, count, null, null);
	}
	
	public List<WFDamageReport> queryDamageReport(int start, int count, DamageStatus ds) {
		return queryDamageReport(start, count, null, ds);
	}
	
	public List<WFDamageReport> queryDamageReport(int start, int count, WFShop shop) {
		return queryDamageReport(start, count, shop, null);
	}
	
	public List<WFDamageReport> queryDamageReport(int start, int count, WFShop shop,  DamageStatus ds) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" from DamageReportRecord ");
		if (shop != null && ds != null) {
			buffer.append(" where 1  ");
		}
		
		if (shop != null) {
			buffer.append(" and shop.id =? ");
		}
		if (ds != null) {
			buffer.append(" and status =? ");
		}
		
		Session sess = getSession();
		Query query = sess.createQuery(buffer.toString());
		if (shop != null && ds != null) {
			query.setLong(0, shop.getId());
			query.setLong(1, ds.ordinal());
		} else if (shop != null) {
			query.setLong(0, shop.getId());
		} else if (ds != null) {
			query.setLong(0, ds.ordinal());
		}
		
		List<DamageReportRecord> rlist = query.list();
		List<WFDamageReport> list = new ArrayList<WFDamageReport>(rlist.size());
		for (DamageReportRecord r : rlist) {
			WFDamageReport report = new WFDamageReport();
			report.setId(r.getId());
			report.setDatetime(r.getDatetime());
			report.setIs(r.getStatus());
			report.setOperator(userService.getUser(r.getOperator().getId()));
			report.setShop(getShop(r.getShop().getId()));
			list.add(report);
		}
		
		sess.close();
		return list;
		
	}
	
	public void queryDamageReportDetail(WFDamageReport wr) {
		Session sess = getSession();
		Query query = sess
				.createQuery(" from DamageReportGoods  where record.id = ? ");
		query.setLong(0, wr.getId());
		List<DamageReportGoods> list = query.list();
		for (DamageReportGoods iur : list) {
			wr.addDamageItem(goodsService.getGoods(iur.getGoods().getId()), iur.getCount(), true);
		}
		sess.close();
	}


	public Result reportDamage(WFDamageReport report, WFShop shop, WFUser manager) {
		DamageReportRecord record = new DamageReportRecord();
		record.setDatetime(report.getDatetime());
		record.setOperator(manager);
		record.setStatus(DamageStatus.REPORTED);
		record.setShop(shop);

		Session sess = getSession();
		Transaction tr = sess.beginTransaction();
		sess.save(record);
		sess.flush();
		int count = report.getItemCount();
		DamageReportGoods ig = null;
		for (int i = 0; i < count; i++) {
			WFDamageReport.Item wi = report.getItem(i);
			if (wi.isPersisted()) {
				continue;
			}
			ig = new DamageReportGoods();
			ig.setGoods(goodsService.getGoods(wi.getGoods().getId()));
			ig.setCount(wi.getCount());
			ig.setRecord(record);
			sess.save(ig);
			wi.setPersisted(true);
		}

		tr.commit();
		sess.close();
		return Result.SUCCESS;
	}
	
	
	
	public void reportIncoming(WFShop shop, WFIncoming incoming, WFOperationCost cost, WFUser manager) {
		Session sess = getSession();
		Incoming inco = new Incoming();
		inco.setDate(incoming.getDate());
		inco.setCash(incoming.getCash());
		inco.setAli(incoming.getAli());
		inco.setDazhong(incoming.getDazhong());
		inco.setDelivery(incoming.getDelivery());
		inco.setNuomi(incoming.getNuomi());
		inco.setWeixin(incoming.getWeixin());
		inco.setOther(incoming.getOther());
		inco.setZls(incoming.getZls());
		inco.setNuomiaf(incoming.getNuomiaf());
		inco.setDazhongaf(incoming.getDazhongaf());
		inco.setShop(shop);
		Transaction tr = sess.beginTransaction();
		sess.save(inco);
		
		List<DeliveryItem> deliList = incoming.getDelis();
		Delivery d = null;
		for (DeliveryItem item : deliList) {
			d = new Delivery();
			d.setShop(shop);
			d.setDate(incoming.getDate());
			d.setDeliveryFee(item.getDeliveryFee());
			d.setDeliveryType(item.getDeliveryType());
			d.setIncoming(item.getIncoming());
			d.setOnlinePayment(item.getOnlinePayment());
			d.setRefund(item.getRefund());
			d.setRefund1(item.getRefund1());
			d.setServiceFee(item.getServiceFee());
			d.setValid(item.getValid());
			sess.save(d);
		}
		
		List<GroupOnItem> groups = incoming.getGroups();
		GroupOn go = null;
		for (GroupOnItem item : groups) {
			go = new GroupOn();
			go.setDate(incoming.getDate());
			go.setShop(shop);
			go.setCe(item.getCe());
			go.setCount(item.getCount());
			go.setDjq(item.getDjq());
			go.setGme(item.getGme());
			go.setGroupType(item.getGroupType());
			go.setOther(item.getOther());
			go.setSje(item.getSje());
			go.setDesc(item.getDesc());
			sess.save(go);
		}
		
		OperationCost oc = new OperationCost();
		oc.setDate(new Date());
		oc.setShop(shop);
		oc.setRytl(cost.getRytl());
		oc.setSb(cost.getSb());
		oc.setBc(cost.getBc());
		oc.setHsf(cost.getHsf());
		oc.setYl(cost.getYl());
		oc.setSf(cost.getSf());
		oc.setDf(cost.getDf());
		oc.setRqf(cost.getRqf());
		oc.setFf(cost.getFf());
		oc.setGz(cost.getGz());
		oc.setRz(cost.getRz());
		oc.setQt(cost.getQt());
		sess.save(oc);
		
		tr.commit();
		sess.flush();
		
	}
	
	
	public Result requestInventory(WFInventoryRequest inventory, WFShop shop, WFUser manager) {
		InventoryRequestRecord record = null;
		

		Session sess = getSession();
		
		Query query = sess.createQuery(" from InventoryRequestRecord where shopId = ? and requestDate =? ");
		query.setLong(0, shop.getId());
		query.setDate(1, inventory.getDatetime());
		Transaction tr = sess.beginTransaction();
		List<InventoryRequestRecord> irrList = query.list();
		if (irrList == null || irrList.size() <= 0) {
			record = new InventoryRequestRecord();
			record.setDatetime(inventory.getDatetime());
			record.setOperator(manager);
			record.setStatus(InventoryStatus.REQUEST);
			record.setShop(shop);
			record.setRequestDate(inventory.getDatetime());
			
			sess.save(record);
			sess.flush();
		} else {
			record = irrList.iterator().next();
		}
		
		List<WFSupplierMapping> mappingList = supplierService.getMappingList();
		Map<WFSupplierMapping, LocalMappingSubRecord> mapping = new HashMap<WFSupplierMapping, LocalMappingSubRecord>();
		
		int count = inventory.getItemCount();
		InventoryRequestGoods ig = null;
		for (int i = 0; i < count; i++) {
			WFInventoryRequest.Item wi = inventory.getItem(i);
			if (wi.isPersisted()) {
				continue;
			}
			
			WFGoods wfg = goodsService.getGoods(wi.getGoods().getId());
			//check 
			handleSubRecordsGoods(record, mapping, wfg, mappingList,wi);
			
			
			ig = new InventoryRequestGoods();
			ig.setGoods(wfg);
			ig.setCount(wi.getCount());
			ig.setRecord(record);
			sess.save(ig);
			wi.setPersisted(true);
		}
//		
//		for (LocalMappingSubRecord lmsr : mapping.values()) {
//			saveSubRecord(lmsr);
//		}

		tr.commit();
		sess.close();
		return Result.SUCCESS;
	}
	
	
	
	private void handleSubRecordsGoods(InventoryRequestRecord parent, Map<WFSupplierMapping, LocalMappingSubRecord> map, WFGoods wfg, List<WFSupplierMapping> ml, WFInventoryRequest.Item item) {
		for (WFSupplierMapping wfs : ml) {
			if (wfg.getId() == wfs.getMappingId() && wfs.getMc() == DeliverySupplierConfiguration.MC.GOODS) {
				LocalMappingSubRecord lmsr = map.get(wfs);
				if (lmsr == null) {
					lmsr = new LocalMappingSubRecord();
					lmsr.parent = parent;
					map.put(wfs, lmsr);
				}
				lmsr.addWFGoods(wfg, item);
			} else if (wfs.getMc() == DeliverySupplierConfiguration.MC.CATE) {
				WFCategory wf = (WFCategory)wfg.getCate();
				while (wf != null) {
					if (wf.getId() == wfs.getMappingId()) {
						LocalMappingSubRecord lmsr = map.get(wfs);
						if (lmsr == null) {
							lmsr = new LocalMappingSubRecord();
							lmsr.parent = parent;
							map.put(wfs, lmsr);
						}
						lmsr.addWFGoods(wfg, item);
						break;
					} 
					
					wf = wf.getParent();
				}
			}
		}
	}
	
	
	private void saveSubRecord(LocalMappingSubRecord subRecord) {
		Session sess = getSession();
		
		InventoryRequestRecord record = new InventoryRequestRecord();
		record.setDatetime(subRecord.parent.getDatetime());
		record.setOperator(subRecord.parent.getOperator());
		record.setShop(subRecord.parent.getShop());
		record.setParent(subRecord.parent);
		record.setStatus(InventoryStatus.REQUEST);
		record.setSupplierRecd(InventoryRequestRecord.TYPE_IS_SUPPLIER);
		record.setRequestDate(subRecord.parent.getDatetime());
		sess.save(record);
		sess.flush();
		
		int len = subRecord.items.size();
		for (int i = 0; i < len; i++) {
			WFGoods wfg = subRecord.goodsList.get(i);
			WFInventoryRequest.Item wi = subRecord.items.get(i);
			
			InventoryRequestGoods ig = new InventoryRequestGoods();
			ig = new InventoryRequestGoods();
			ig.setGoods(wfg);
			ig.setCount(wi.getCount());
			ig.setRecord(record);
			sess.save(ig);
		}
	}

	
	final static class LocalMappingSubRecord {
		InventoryRequestRecord parent;
		WFSupplierMapping wfm;
		List<WFInventoryRequest.Item> items;
		List<WFGoods> goodsList;
		
		public void addWFGoods(WFGoods wfg, WFInventoryRequest.Item item) {
			if (goodsList == null || items == null) {
				goodsList = new ArrayList<WFGoods>(20);
				items = new ArrayList<WFInventoryRequest.Item>(20);
			}
			goodsList.add(wfg);
			items.add(item);
		}
	}
	
	
	
	public WFIncoming queryShopIncoming(WFShop shop, Date date) {
		if (shop == null || date == null) {
			return null;
		}
		WFIncoming wf = null;
		Session sess = getSession();
		Query query = sess.createQuery(" from Incoming where shop.id = ? and date = ? ");
		query.setLong(0, shop.getId());
		query.setDate(1, date);
		List<Incoming> inList = (List<Incoming>)query.list();
		if (inList.size() > 0) {
			 wf = new WFIncoming();
			Incoming in = inList.get(0);
			wf.setZls(in.getZls());
			wf.setCash(in.getCash());
			wf.setAli(in.getAli());
			wf.setDate(date);
			wf.setDazhong(in.getDazhong());
			wf.setNuomi(in.getNuomi());
			wf.setOther(in.getOther());
			wf.setWeixin(in.getWeixin());
			wf.setDesc(in.getDesc());
			wf.setNuomiaf(in.getNuomiaf());
			wf.setDaZhongaf(in.getDazhongaf());
		}
		
		query = sess
				.createQuery(" from GroupOn where shop.id = ? and date = ? ");
		query.setLong(0, shop.getId());
		query.setDate(1, date);
		List<GroupOn> gList = (List<GroupOn>) query.list();
		if (gList.size() > 0  && wf == null) {
			 wf = new WFIncoming();
		}
		for (GroupOn g : gList) {
			wf.addShopOnItem(g.getGroupType(), g.getCount(), g.getDjq(),
					g.getGme(), g.getCe(), g.getSje(), g.getOther());
		}

		query = sess
				.createQuery(" from Delivery where shop.id = ? and date = ? ");
		query.setLong(0, shop.getId());
		query.setDate(1, date);
		List<Delivery> dList = (List<Delivery>) query.list();
		if (dList.size() > 0  && wf == null) {
			 wf = new WFIncoming();
		}
		for (Delivery d : dList) {
			wf.addDeliveryData(d.getDeliveryType(), d.getIncoming(),
					d.getOnlinePayment(), d.getRefund(), d.getRefund1(),
					d.getServiceFee(), d.getDeliveryFee(), d.getValid());
		}
		return wf;
	}
	
	
	public WFOperationCost queryShopOperationCost(WFShop shop, Date date) {
		if (shop == null || date == null) {
			return null;
		}
		WFOperationCost cost = null;
		Session sess = getSession();
		Query query = sess.createQuery(" from OperationCost where shop.id = ? and date = ? ");
		query.setLong(0, shop.getId());
		query.setDate(1, date);
		List<OperationCost> inList = (List<OperationCost>)query.list();
		if (inList.size() > 0) {
			 cost = new WFOperationCost();
			 OperationCost in = inList.get(0);
			//日用调料
			cost.setRytl(in.getRytl());
			//烧饼
			cost.setSb(in.getSb());
			//补菜
			cost.setBc(in.getBc());
			//伙食费
			cost.setHsf(in.getHsf());
			//饮料
			cost.setYl(in.getYl());
			//水费
			cost.setSf(in.getSf());
			//电费
			cost.setDf(in.getDf());
			//燃气费
			cost.setRqf(in.getRqf());
			//房费
			cost.setFf(in.getFf());
			//工资
			cost.setGz(in.getGz());
			//日杂
			cost.setRz(in.getRz());
			//其他
			cost.setQt(in.getQt());
		}
		return cost;
	}
	
	
	
	public List<WFIncoming> queryShopIncoming(WFShop shop, Date start, Date end) {
		if (shop == null || start == null || end == null) {
			return null;
		}
		Session sess = getSession();
		Query query = sess.createQuery(" from Incoming where shop.id = ? and date >= ?  and date <=? order by date asc ");
		query.setLong(0, shop.getId());
		query.setDate(1, start);
		query.setDate(2, end);
		List<Incoming> inList = (List<Incoming>)query.list();
		List<WFIncoming> incomingList = new ArrayList<WFIncoming>(inList.size());
		WFIncoming wf = null;
		for (Incoming in : inList) {
			wf = new WFIncoming();
			wf.setZls(in.getZls());
			wf.setCash( in.getCash());
			wf.setAli( in.getAli());
			wf.setDazhong( in.getDazhong());
			wf.setNuomi( in.getNuomi());
			wf.setOther(in.getOther());
			wf.setWeixin(in.getWeixin());
			wf.setDate(in.getDate());
			wf.setNuomiaf(in.getNuomiaf());
			wf.setDaZhongaf(in.getDazhongaf());
			incomingList.add(wf);
		}
		return incomingList;
	}
	
	public  List<WFOperationCost> queryShopOperationCost(WFShop shop, Date start, Date end) {
		if (shop == null || start == null || end == null) {
			return null;
		}
		WFOperationCost cost = null;
		Session sess = getSession();
		Query query = sess.createQuery(" from OperationCost where shop.id = ? and date >= ?   and date <=? order by date asc  ");
		query.setLong(0, shop.getId());
		query.setDate(1, start);
		query.setDate(2, end);
		List<OperationCost> inList = (List<OperationCost>)query.list();
		List<WFOperationCost> costList = new ArrayList<WFOperationCost> (inList.size());
		for (OperationCost in: inList) {
			 cost = new WFOperationCost();
			 cost.setDate(in.getDate());
			//日用调料
			cost.setRytl(in.getRytl());
			//烧饼
			cost.setSb(in.getSb());
			//补菜
			cost.setBc(in.getBc());
			//伙食费
			cost.setHsf(in.getHsf());
			//饮料
			cost.setYl(in.getYl());
			//水费
			cost.setSf(in.getSf());
			//电费
			cost.setDf(in.getDf());
			//燃气费
			cost.setRqf(in.getRqf());
			//房费
			cost.setFf(in.getFf());
			//工资
			cost.setGz(in.getGz());
			//日杂
			cost.setRz(in.getRz());
			//其他
			cost.setQt(in.getQt());
			costList.add(cost);
		}
		return costList;
	}
	
	
	public  List<WFIncoming>  queryShopIncoming(Date start, Date end) {
		if (start == null || end == null) {
			return null;
		}
		Session sess = getSession();
		Query query = sess.createQuery(" from Incoming where  date >= ?  and date <=? order by date, shop.id asc ");
		query.setDate(0, start);
		query.setDate(1, end);
		List<Incoming> inList = (List<Incoming>)query.list();
		List<WFIncoming> incomingList = new ArrayList<WFIncoming>(inList.size());
		WFIncoming wf = null;
		for (Incoming in : inList) {
			wf = new WFIncoming();
			wf.setShop(this.getShop(in.getShop().getId()));
			wf.setZls(in.getZls());
			wf.setCash( in.getCash());
			wf.setAli( in.getAli());
			wf.setDazhong( in.getDazhong());
			wf.setNuomi( in.getNuomi());
			wf.setOther(in.getOther());
			wf.setWeixin(in.getWeixin());
			wf.setDate(in.getDate());
			wf.setNuomiaf(in.getNuomiaf());
			wf.setDaZhongaf(in.getDazhongaf());
			incomingList.add(wf);
		}
		return incomingList;
	}
	
	public  List<WFOperationCost>   queryShopOperationCost(Date start, Date end) {
		if (start == null || end == null) {
			return null;
		}
		WFOperationCost cost = null;
		Session sess = getSession();
		Query query = sess.createQuery(" from OperationCost where date >= ?   and date <=? order by date. shop.id asc  ");
		query.setDate(0, start);
		query.setDate(1, end);
		List<OperationCost> inList = (List<OperationCost>)query.list();
		List<WFOperationCost> costList = new ArrayList<WFOperationCost> (inList.size());
		for (OperationCost in: inList) {
			cost = new WFOperationCost();
			cost.setDate(in.getDate());
			cost.setShop(getShop(in.getShop().getId()));
			//日用调料
			cost.setRytl(in.getRytl());
			//烧饼
			cost.setSb(in.getSb());
			//补菜
			cost.setBc(in.getBc());
			//伙食费
			cost.setHsf(in.getHsf());
			//饮料
			cost.setYl(in.getYl());
			//水费
			cost.setSf(in.getSf());
			//电费
			cost.setDf(in.getDf());
			//燃气费
			cost.setRqf(in.getRqf());
			//房费
			cost.setFf(in.getFf());
			//工资
			cost.setGz(in.getGz());
			//日杂
			cost.setRz(in.getRz());
			//其他
			cost.setQt(in.getQt());
			costList.add(cost);
		}
		return costList;
	}
	
	
	
	public List<WFInventoryRequest> queryInventoryRequestList(int start, int count) {
		return queryInventoryRequestList(null, null, null, false, start, count);
	}
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop, Date date) {
		return queryInventoryRequestList(shop, date, null, true, 0, 30);
	}
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop, Date start, Date end) {
		return queryInventoryRequestList(shop, start, end, true, 0, 30);
	}
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop) {
		return queryInventoryRequestList(shop, null, null, false, 0, 30);
	}
	
	public List<WFInventoryRequest> queryInventoryRequestList(Date date) {
		return queryInventoryRequestList(null, date, null, false, 0, 30);
	}
	
	public List<WFInventoryRequest> queryInventoryRequestList(Date start, Date end) {
		return queryInventoryRequestList(null, start, end, true, 0, 30);
	}
	
	
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop, Date start, Date end, boolean specificDate, int offset, int count) {
		
		StringBuffer hqlBuf = new StringBuffer();
		hqlBuf.append(" from InventoryRequestRecord where 1 = 1 and WF_PARENT_REC_ID = 0 ");
		if (shop != null) {
			hqlBuf.append(" and shopId = ? ");
		}
		
		if (specificDate) {
			if (start != null || end != null) {
				hqlBuf.append(" and datetime = ?");
			} 
		} else {
			if (start != null && end != null) {
				hqlBuf.append(" and datetime >= ? and datetime <= ?");
			} else if (start != null) {
				hqlBuf.append(" and datetime >= ? ");
			}  else if (end != null) {
				hqlBuf.append(" and datetime <= ? ");
			}
		}
		
		hqlBuf.append(" order by datetime desc ");
		
		Session sess = getSession();
		Query query = sess.createQuery(hqlBuf.toString());
		int idx = 0;
		if (shop != null) {
			query.setLong(idx++, shop.getId());
		}
		
		if (specificDate) {
			if (start != null ) {
				query.setDate(idx++, start);
			} else if (end != null) {
				query.setDate(idx++, end);
			} 
		} else {
			if (start != null && end != null) {
				query.setDate(idx++, start);
				query.setDate(idx++, end);
			} else if (start != null) {
				query.setDate(idx++, start);
			}  else if (end != null) {
				query.setDate(idx++, end);
			}
		}
		
		List<InventoryRequestRecord>  list = query.list();
		List<WFInventoryRequest> wflist = new ArrayList<WFInventoryRequest>(list.size());
		for (InventoryRequestRecord irq : list) {
			WFInventoryRequest wf = new WFInventoryRequest();
			wf.setId(irq.getId());
			wf.setDatetime(irq.getDatetime());
			wf.setOperator(irq.getOperator());
			wf.setIs(irq.getStatus());
			wf.setShop(new WFShop(irq.getShop()));
			wflist.add(wf);
		}
		
		return wflist;
	}
	
	
	public Result queryInventoryRequestGoods(WFInventoryRequest ir) {
		if (ir == null) {
			return Result.ERR_GENERIC_FAILED;
		}
		Session sess = getSession();
		Query query = sess.createQuery(" from InventoryRequestGoods where record.id = ? ");
		query.setLong(0, ir.getId());
		List<InventoryRequestGoods>  goodsList = query.list();
		for (InventoryRequestGoods g : goodsList) {
			ir.addInventoryItem(goodsService.getGoods(g.getGoods().getId()), g.getCount(), true);
		}
		ir.setLoadItem(true);
		
		query = sess.createQuery(" from InventoryUpdateRecord where requestInventoryId = ?");
		query.setLong(0, ir.getId());
		List<InventoryUpdateRecord> drlist = query.list();
		if (drlist.size() > 0) {
			InventoryUpdateRecord dr = drlist.get(0);
			query = sess.createQuery(" from  InventoryGoods  where record.id = ? ");
			query.setLong(0, dr.getId());
			List<InventoryGoods> drgList = query.list();
			for (InventoryGoods drg : drgList) {
				ir.updateInventoryItem(goodsService.getGoods(drg.getGoods().getId()), drg.getCount(), drg.getPrice());
			}
		}
		return Result.SUCCESS;
	}
	
	
	public File generateDeliveryForm(WFInventoryRequest ir) {
		if (ir == null) {
			throw new NullPointerException("ir is null ");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String rootDir = ServerConstants.getInstance().getDeliveryFormPath()
				+ File.separator + sdf.format(ir.getDatetime())
				+ File.separator;
		String fileName =  "shop_" + ir.getShop().getId() + "_ir_"
				+ ir.getId()+".pdf";
		File dir = new File(rootDir);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException(" can not make dir:"+ rootDir);
			}
		}
		
		  Runtime rt = Runtime.getRuntime();
	        try {
				rt.exec("chmod 777 " + dir.getAbsolutePath());
				 logger.info(" Update dir permisson:" + dir.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		File file = new File(rootDir + fileName);
		if (file.exists()) {
			return file;
		}
		
		
		Document document = new Document();
		BaseFont bf;
        try {
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
		    bf= BaseFont.createFont("hxb-meixinti.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
       
	
        Font chapterFont = new Font(bf, 14);
        Chunk chunk = new Chunk("", chapterFont);
        Chapter chapter = new Chapter(new Paragraph(chunk), 0);
        chapter.setNumberDepth(0);
        Paragraph title = new Paragraph("出货单 \n", chapterFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        chapter.add(title);
        chapter.add(new Paragraph("店铺： "+ir.getShop().getName()+" \n", chapterFont));
        chapter.add(new Paragraph("日期： "+sdf1.format(ir.getDatetime())+"\n", chapterFont));
        chapter.add(new Paragraph("地址： "+ir.getShop().getAddress()+ " \n\n", chapterFont));
        
        Font chapterFont1 = new Font(bf, 11);
        Paragraph detail = new Paragraph("产品明细 \n\n", chapterFont1);
        detail.setAlignment(Paragraph.ALIGN_CENTER);
        chapter.add(detail);
        
        
        PdfPTable table = new PdfPTable(7);
        table.addCell( new PdfPCell( new Phrase("序号", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("品名", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("品类", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("规格", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("数量", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("单价", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("小计", chapterFont1)));
        
        
        List<WFInventoryRequest.Item> list = ir.getItemList();
        int count = list.size();
        double sum = 0;
        for(int aw = 0; aw < count; aw++){
        	WFInventoryRequest.Item  item = list.get(aw);
        	table.addCell(new PdfPCell( new Phrase((aw + 1)+"", chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getName(), chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getCate().getName(), chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getUnit() , chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getCount()+"", chapterFont1)));
        	table.addCell( new PdfPCell( new Phrase(item.getGoods().getPrice1()+"", chapterFont1)));
        	table.addCell( new PdfPCell( new Phrase(String.format("%.2f", item.getGoods().getPrice1() * item.getCount()) , chapterFont1)));
        	sum += item.getGoods().getPrice() * item.getCount();
        }   
       
        
        chapter.add(table);
        
        PdfPTable table1 = new PdfPTable(1);
        table1.addCell(new Phrase("总计： " + String.format("%.2f", sum), chapterFont1));
        chapter.add(table1);
        
        Chapter chapterbottom = new Chapter(new Paragraph(""), 0);
        chapterbottom.setNumberDepth(0);
        Paragraph p1 = new Paragraph("出单人：__________"   +" 配送人:___________" +"店长：______________", chapterFont);
        p1.setAlignment(Paragraph.ALIGN_CENTER);
        chapter.add(p1);
        
        try {
			document.add(chapter);
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			 document.close();
		}
        
        try {
			rt.exec("chmod 777 " + file.getAbsolutePath());
			 logger.info(" Update file permisson:" + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return file;
       
	}
	
	
	
	public File generateDeliveryForm(WFDelivery wf) {
		if (wf == null) {
			throw new NullPointerException("wf is null ");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String rootDir = ServerConstants.getInstance().getDeliveryFormPath()
				+ File.separator + sdf.format(wf.getDatetime())
				+ File.separator;
		String fileName =  "shop_" + wf.getShop().getId() + "_ir_"
				+ wf.getId()+".pdf";
		File dir = new File(rootDir);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException(" can not make dir:"+ rootDir);
			}
			dir.setWritable(true, false);
			dir.setReadable(true, false);
			
			 Runtime rt = Runtime.getRuntime();
		        try {
					rt.exec("chmod 777 " + dir.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
		        logger.info(" Update dir permisson:" + dir.getAbsolutePath());
		}
		
		File file = new File(rootDir + fileName);
		if (file.exists()) {
			return file;
		}
		
		
		Document document = new Document();
		BaseFont bf;
        try {
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
		    bf= BaseFont.createFont("hxb-meixinti.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
       
	
        Font chapterFont = new Font(bf, 14);
        Chunk chunk = new Chunk("", chapterFont);
        Chapter chapter = new Chapter(new Paragraph(chunk), 0);
        chapter.setNumberDepth(0);
        Paragraph title = new Paragraph("出货单 \n", chapterFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        chapter.add(title);
        chapter.add(new Paragraph("店铺： "+wf.getShop().getName()+" \n", chapterFont));
        chapter.add(new Paragraph("日期： "+sdf1.format(wf.getDatetime())+"\n", chapterFont));
        chapter.add(new Paragraph("地址： "+wf.getShop().getAddress()+ " \n\n", chapterFont));
        
        Font chapterFont1 = new Font(bf, 11);
        Paragraph detail = new Paragraph("产品明细 \n\n", chapterFont1);
        detail.setAlignment(Paragraph.ALIGN_CENTER);
        chapter.add(detail);
         
        
        PdfPTable table = new PdfPTable(7);
        table.addCell( new PdfPCell( new Phrase("序号", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("品名", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("品类", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("规格", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("数量", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("单价", chapterFont1)));
        table.addCell( new PdfPCell( new Phrase("小计", chapterFont1)));
        
        
        List<Item> list = wf.getItemList();
        int count = list.size();
        double sum = 0;
        for(int aw = 0; aw < count; aw++){
        	WFDelivery.Item  item = list.get(aw);
        	table.addCell(new PdfPCell( new Phrase((aw + 1)+"", chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getName(), chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getCate().getName(), chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getGoods().getUnit() , chapterFont1)));
        	table.addCell(new PdfPCell( new Phrase(item.getRealCount()+"", chapterFont1)));
        	table.addCell( new PdfPCell( new Phrase(item.getPrice()+"", chapterFont1)));
        	table.addCell( new PdfPCell( new Phrase(String.format("%.2f", item.getPrice() * item.getCount()) , chapterFont1)));
        	sum += item.getGoods().getPrice() * item.getCount();
        }   
       
        
        chapter.add(table);
        
        PdfPTable table1 = new PdfPTable(1);
        table1.addCell(new Phrase("总计： " + String.format("%.2f", sum), chapterFont1));
        chapter.add(table1);
        
        Chapter chapterbottom = new Chapter(new Paragraph(""), 0);
        chapterbottom.setNumberDepth(0);
        Paragraph p1 = new Paragraph("出单人：__________"   +" 配送人:___________" +"店长：______________", chapterFont);
        p1.setAlignment(Paragraph.ALIGN_CENTER);
        chapter.add(p1);
        
        try {
			document.add(chapter);
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			 document.close();
		}
        

        Runtime rt = Runtime.getRuntime();
        try {
			rt.exec("chmod 777 " + file.getAbsolutePath());
			 logger.info(" Update file permisson:" + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
        return file;
       
	}
	
	
	
	@Override
	public Result prepareDelivery(WFDelivery de, WFUser user) {
		Session sess = getSession();
		DeliveryRecord dr = new DeliveryRecord();
		dr.setInventoryRequestId(de.getId());
		dr.setShop(de.getShop());
		dr.setDatetime(de.getDatetime());
		dr.setOperator(user);
		dr.setShopAddress(de.getShop().getAddress());
		dr.setShopName(de.getShop().getName());
		dr.setStatus(InventoryStatus.DELIVERYED);
		Transaction tr = sess.beginTransaction();
		sess.save(dr);
		sess.flush();
		
		Set<Long> goodsId = new HashSet<Long>();
		List<Item> list = de.getItemList();
		for (WFDelivery.Item di : list) {
			if (di.getRealCount() <= 0) {
				continue;
			}
			DeliveryRecordGoods g = new DeliveryRecordGoods();
			InventoryGoods ig = queryAvailableInventoryGoods(di.getGoods().getId());
			float requestCount = di.getRealCount();
			while (requestCount > 0 && ig != null) {
				if (ig.getRemCount() > requestCount) {
					ig.setRemCount(ig.getRemCount() - requestCount);
					g.setInventoryCount(requestCount);
					requestCount = 0;
				} else {
					g.setInventoryCount(ig.getRemCount());
					requestCount -= ig.getRemCount();
					ig.setRemCount(0);
				}
				g.setDeliverCount(di.getRealCount());
				g.setRequestCount(di.getCount());
				if (di.getGoods().getPrice() <= 0) {
					g.setPrice(di.getPrice());
				} else {
					g.setPrice(di.getGoods().getPrice());
				}
				g.setGoods(di.getGoods());
				g.setBrandName(ig.getBrandName());
				g.setVendorName(ig.getVendorName());
				g.setInventoryPrice(ig.getPrice());
				g.setInventoryId(ig.getRecord().getId());
				g.setRecord(dr);
				sess.save(g);
				
				//Just update remain count 
				sess.update(ig);
				ig = queryAvailableInventoryGoods(di.getGoods().getId());
				g = new DeliveryRecordGoods();
				
				goodsId.add(Long.valueOf(di.getGoods().getId()));
			}
			
			if (requestCount != 0) {
				tr.rollback();
				return Result.ERR_OUT_OF_STOCK;
			}
		}
		
		//Update inventory and update goods price.
		
		Query irrQuery = sess.createQuery(" from InventoryRequestRecord where parentId = ? ");
		irrQuery.setLong(0, dr.getInventoryRequestId());
		List<InventoryRequestRecord> irrList = irrQuery.list();
		logger.info(" Update suppliery InventoryRequestRecord size : " + irrList.size());
		for (InventoryRequestRecord irr : irrList) {
			irr.setStatus(InventoryStatus.PREPARING_INVENTORY);
			sess.update(irr);
			logger.info(" Update suppliery InventoryRequestRecord : " + irr.getId());
		}

		
		tr.commit();
		
		//TODO update goods price
		
		return Result.SUCCESS;
	}
	
	
	
	private void createInventory(Session sess, WFInventory inventory, WFInventoryRequest req) {
		InventoryUpdateRecord record = new InventoryUpdateRecord();
		record.setIt(inventory.getIt());
		record.setDatetime(inventory.getDatetime());
		record.setOperator(inventory.getOperator());
		record.setIt(InventoryType.IN);
		record.setRequestInventoryId(req.getId());
		sess.save(record);
		int count = inventory.getItemCount();
		InventoryGoods ig = null;
		for (int i = 0; i < count; i++) {
			WFInventory.Item wi = inventory.getItem(i);
			if (wi.isPersisted()) {
				continue;
			}
			ig = new InventoryGoods();
			
			ig.setGoods(wi.getGoods());
			ig.setCount(wi.getCount());
			ig.setPrice(wi.getPrice());
			ig.setRemCount(wi.getCount());
			ig.setRecord(record);
			sess.save(ig);
			wi.setPersisted(true);
		}

		sess.flush();
	}
	
	
	private InventoryGoods queryAvailableInventoryGoods(long gid) {
		Session sess = getSession();
		Query query = sess.createQuery(" from InventoryGoods where goods.id = ? and remCount > 0 order by id ");
		query.setLong(0, gid);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List list = query.list();
		return list.size() > 0 ? (InventoryGoods)list.get(0) : null;
		
	}
	
	@Override
	public Result handleInternalDelivery(WFDelivery de, WFUser user) {
		
		Session sess = getSession();
		
		InternalDeliveryRecord dr = new InternalDeliveryRecord();
		dr.setDatetime(de.getDatetime());
		dr.setOperator(user);
		Transaction tr = sess.beginTransaction();
		sess.save(dr);
		sess.flush();
		
		Set<Long> goodsId = new HashSet<Long>();
		List<Item> list = de.getItemList();
		for (WFDelivery.Item di : list) {
			if (di.getRealCount() <= 0) {
				continue;
			}
			InternalDeliveryRecordGoods g = new InternalDeliveryRecordGoods();
			InventoryGoods ig = queryAvailableInventoryGoods(di.getGoods().getId());
			int requestCount = (int)di.getRealCount();
			while (requestCount > 0 && ig != null) {
				if (ig.getRemCount() > requestCount) {
					ig.setRemCount(ig.getRemCount() - requestCount);
					g.setInventoryCount(requestCount);
					requestCount = 0;
				} else {
					g.setInventoryCount(ig.getRemCount());
					requestCount -= ig.getRemCount();
					ig.setRemCount(0);
				}
				g.setDeliverCount(di.getRealCount());
				g.setPrice(di.getGoods().getPrice());
				g.setGoods(di.getGoods());
				g.setBrandName(ig.getBrandName());
				g.setVendorName(ig.getVendorName());
				g.setInventoryPrice(ig.getPrice());
				g.setInventoryId(ig.getRecord().getId());
				g.setRecord(dr);
				sess.save(g);
				
				//Just update remain count 
				sess.update(ig);
				ig = queryAvailableInventoryGoods(di.getGoods().getId());
				g = new InternalDeliveryRecordGoods();
				
				goodsId.add(Long.valueOf(di.getGoods().getId()));
			}
			
			if (requestCount != 0) {
				tr.rollback();
				return Result.ERR_OUT_OF_STOCK;
			}
		}
		
		
		tr.commit();
		
		return Result.SUCCESS;
	}
	
	@Override
	public Result updateShopIncomingAndOperationCost(WFShop shop, WFIncoming incoming, WFOperationCost cost, Date date) {
		
		Session sess = getSession();
		Query query = sess.createQuery(" from Incoming where shop.id =? and date = ?");
		query.setLong(0, shop.getId());
		query.setDate(1, date);
		List<Incoming> incomingList = query.list();
		if (incomingList == null || incomingList.size() <=0) {
			return Result.ERR_NO_SUCH_RECORD;
		}
		
		
		query = sess.createQuery(" from OperationCost where shop.id =? and date = ?");
		query.setLong(0, shop.getId());
		query.setDate(1, date);
		List<OperationCost> costList = query.list();
		if (costList == null || costList.size() <=0) {
			return Result.ERR_NO_SUCH_RECORD;
		}
		
		Incoming inco = incomingList.get(0);
		inco.setCash(incoming.getCash());
		inco.setAli(incoming.getAli());
		inco.setDazhong(incoming.getDazhong());
		inco.setDelivery(incoming.getDelivery());
		inco.setNuomi(incoming.getNuomi());
		inco.setWeixin(incoming.getWeixin());
		inco.setOther(incoming.getOther());
		inco.setZls(incoming.getZls());
		inco.setNuomiaf(incoming.getNuomiaf());
		inco.setDazhongaf(incoming.getDazhongaf());
		
		Transaction tr = sess.beginTransaction();
		sess.update(inco);
		
		OperationCost oc = costList.get(0);
		oc.setShop(shop);
		oc.setRytl(cost.getRytl());
		oc.setSb(cost.getSb());
		oc.setBc(cost.getBc());
		oc.setHsf(cost.getHsf());
		oc.setYl(cost.getYl());
		oc.setSf(cost.getSf());
		oc.setDf(cost.getDf());
		oc.setRqf(cost.getRqf());
		oc.setFf(cost.getFf());
		oc.setGz(cost.getGz());
		oc.setRz(cost.getRz());
		oc.setQt(cost.getQt());
		sess.update(oc);
		
		tr.commit();
		sess.flush();
		
		
		return Result.SUCCESS;
	}
	
	
	
	public List<WFDelivery> queryDelivery(WFShop shop, Date start, Date end) {
		Session sess = getSession();
		if (start == null || end == null) {
			return null;
		}
		
		Query query = null;
		if (shop == null) {
			query = sess.createQuery(" from DeliveryRecord  where datetime >= ? and datetime <=? ");
			query.setDate(0, start);
			query.setDate(1, end);
		} else {
			query = sess.createQuery(" from DeliveryRecord  where shopId =? and datetime >= ? and datetime <=? ");
			query.setLong(0, shop.getId());
			query.setDate(1, start);
			query.setDate(2, end);
		}
		List<DeliveryRecord> drlist = (List<DeliveryRecord>)query.list();
		List<WFDelivery> wfdlist = new ArrayList<WFDelivery>(drlist.size());
		WFDelivery wfd = null;
		for (DeliveryRecord dr : drlist) {
			wfd = new WFDelivery();
			wfd.setDatetime(dr.getDatetime());
			wfd.setId(dr.getId());
			wfd.setInventoryRequestId(dr.getInventoryRequestId());
			wfd.setShop(this.getShop(dr.getShopId()));
		}
		
		return wfdlist;
	}
	
	
	
	public List<WFShopInventoryCost> queryShopInventoryCost(WFShop shop, Date start, Date end) {
		Session sess = getSession();
		if (start == null || end == null) {
			return null;
		}
		
		Query query = null;
		if (shop == null) {
			query = sess.createSQLQuery(
					"select g.wf_good_id, g.wf_de_price, g.wf_de_count from WF_DELIVERY_RECORD  r join WF_DELIVER_RECORD_GOODS g on r.id = g.wf_rec_id  where r.WF_OPT_TIMESTAMP >= ? and r.WF_OPT_TIMESTAMP <=? ");
			query.setDate(0, start);
			query.setDate(1, end);
		} else {
			query = sess.createSQLQuery(
					"select g.wf_good_id, g.wf_de_price, g.wf_de_count from WF_DELIVERY_RECORD  r join WF_DELIVER_RECORD_GOODS g on r.id = g.wf_rec_id   where r.shopId =? and r.WF_OPT_TIMESTAMP >= ? and r.WF_OPT_TIMESTAMP <=? ");
			query.setLong(0, shop.getId());
			query.setDate(1, start);
			query.setDate(2, end);
		}
		
		Map<WFCategory, WFShopInventoryCost> costMap = new HashMap<WFCategory, WFShopInventoryCost>();
		List<Object[]> list = query.list();
		WFGoods wfg = null;
		WFCategory cateRoot = null;
		for (Object[] obj : list) {
			BigInteger gid = (BigInteger)obj[0];
			BigDecimal pr = (BigDecimal)obj[1];
			BigDecimal count = (BigDecimal)obj[2];
			wfg = this.goodsService.getGoods(gid.longValue());
			cateRoot = wfg.getRootCategory();
			WFShopInventoryCost wic = costMap.get(cateRoot);
			if (wic == null) {
				wic = new WFShopInventoryCost(cateRoot);
				costMap.put(cateRoot, wic);
			}
			wic.addCost(wfg, pr.doubleValue() * count.floatValue());
		}
		
		return new ArrayList<WFShopInventoryCost>(costMap.values());
		
	}
	
	
	public double queryTotalIncoming() {
		Session sess = getSession();
		Query query = sess.createSQLQuery("  select sum(WF_ZLS) from wf_incoming ");
		List<BigDecimal> list = query.list();
		if (list != null && list.size() > 0) {
			BigDecimal bd = list.get(0);
			if (bd != null) {
				return bd.doubleValue();
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	
	
	public double queryTotalOperationCost() {
		Session sess = getSession();
		Query query = sess.createSQLQuery(
				"  select sum(WF_RYTL + WF_BC + WF_SB + WF_HSF + WF_YL + WF_SF + WF_DF + WF_FF + WF_RQF + WF_GZ +WF_RZ + WF_QT) from WF_OPERATION_COST ");
		List<BigDecimal> list = query.list();
		if (list != null && list.size() > 0) {
			BigDecimal bd = list.get(0);
			if (bd != null) {
				return bd.doubleValue();
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	
}
