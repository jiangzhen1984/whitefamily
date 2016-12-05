package com.whitefamily.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.whitefamily.po.Goods;
import com.whitefamily.po.InventoryGoods;
import com.whitefamily.po.InventoryRequestGoods;
import com.whitefamily.po.InventoryRequestRecord;
import com.whitefamily.po.InventoryStatus;
import com.whitefamily.po.InventoryType;
import com.whitefamily.po.InventoryUpdateRecord;
import com.whitefamily.po.delivery.DeliveryRecord;
import com.whitefamily.po.delivery.DeliveryRecordGoods;
import com.whitefamily.po.delivery.DeliverySupplierConfiguration;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFInventory;
import com.whitefamily.service.vo.WFInventoryGoods;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFSupplierMapping;
import com.whitefamily.service.vo.WFVendor;

public class InventoryService extends BaseService implements IInventoryService {

	private IGoodsService goodsService;

	private IShopService shopService;
	
	private ISupplierService supplierService;
	
	private IUserService userService;

	public IGoodsService getGoodsService() {
		return goodsService;
	}

	public void setGoodsService(IGoodsService goodsService) {
		this.goodsService = goodsService;
	}

	public IShopService getShopService() {
		return shopService;
	}

	public void setShopService(IShopService shopService) {
		this.shopService = shopService;
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

	@Override
	public void createInventory(WFInventory inventory) {
		InventoryUpdateRecord record = new InventoryUpdateRecord();
		record.setIt(inventory.getIt());
		record.setDatetime(inventory.getDatetime());
		record.setOperator(inventory.getOperator());
		record.setIt(InventoryType.IN);

		Session sess = getSession();
		Transaction tr = sess.beginTransaction();
		sess.save(record);
		sess.flush();
		int count = inventory.getItemCount();
		InventoryGoods ig = null;
		List<Goods> list = new ArrayList<Goods>(count);
		boolean flag = false;
		for (int i = 0; i < count; i++) {
			WFInventory.Item wi = inventory.getItem(i);
			if (wi.isPersisted()) {
				continue;
			}
			flag = false;
			Goods gs = goodsService.getGoods(wi.getGoods().getId());
			ig = new InventoryGoods();
			ig.setBrandId(wi.getBrand().getId());
			ig.setBrandName(wi.getBrand().getName());
			ig.setVendorId(wi.getVendor().getId());
			ig.setVendorName(wi.getVendor().getName());
			ig.setGoods(gs);
			ig.setCount(wi.getCount());
			ig.setPrice(wi.getPrice());
			ig.setRecord(record);
			ig.setRate(wi.getRate());
			ig.setRate1(wi.getRate1());
			ig.setRemCount(wi.getCount());
			sess.save(ig);
			wi.setPersisted(true);
			
			float rat = 1 + wi.getRate() / 100 ;
			float rat1 = 1 + wi.getRate1() / 100 ;
			if (gs.getPrice() <= wi.getPrice() *rat) {
				gs.setPrice(wi.getPrice() * rat);
				flag = true;
			}
			if (gs.getPrice1() <= wi.getPrice() * rat1) {
				gs.setPrice1(wi.getPrice() * rat1);
				flag = true;
			}
			
			if (flag) {
				list.add(gs);
			}
		}
		
		
		for (Goods wfg : list) {
			Goods g = (Goods)sess.get(Goods.class, wfg.getId());
			g.setPrice(wfg.getPrice());
			g.setPrice1(wfg.getPrice1());
			g.setPrice2(wfg.getPrice2());
			g.setPrice3(wfg.getPrice3());
			sess.update(g);
		}
		tr.commit();

	}

	public Result createInventoryRequest(WFInventoryRequest request) {
		if (request.getShop() == null) {
			return Result.ERR_INVALID_SHOP_FOR_INVENTORY_REQUEST;
		}

		InventoryRequestRecord record = new InventoryRequestRecord();
		record.setDatetime(request.getDatetime());
		record.setOperator(request.getOperator());
		record.setShop(request.getShop());

		List<WFSupplierMapping> mappingList = supplierService.getMappingList();
		Map<WFSupplierMapping, LocalMappingSubRecord> mapping = new HashMap<WFSupplierMapping, LocalMappingSubRecord>();
		
		
		
		Session sess = getSession();
		Transaction tr = sess.beginTransaction();
		sess.save(record);
		sess.flush();
		
		int count = request.getItemCount();
		InventoryRequestGoods ig = null;
		for (int i = 0; i < count; i++) {
			WFInventoryRequest.Item wi = request.getItem(i);
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
		
		for (LocalMappingSubRecord lmsr : mapping.values()) {
			saveSubRecord(lmsr);
		}

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

	public Result prepareInventoryRequest(WFInventoryRequest req) {
		Session sess = getSession();
		Transaction tr = sess.beginTransaction();
		Result res = internalUpdateInventoryRequestRecord(sess, req.getId(),
				InventoryStatus.PREPARING_INVENTORY);
		if (res == Result.SUCCESS) {
			tr.commit();
		} else {
			tr.rollback();
		}
		sess.close();
		return res;
	}

	public Result deliveryInventoryRequest(WFInventoryRequest req) {
		Session sess = getSession();
		Transaction tr = sess.beginTransaction();
		Result res = internalUpdateInventoryRequestRecord(sess, req.getId(),
				InventoryStatus.DELIVERING);
		if (res == Result.SUCCESS) {
			tr.commit();
		} else {
			tr.rollback();
		}
		sess.close();
		return res;
	}

	public Result finishInventoryRequest(WFInventoryRequest req) {
		Session sess = getSession();
		Transaction tr = sess.beginTransaction();
		Result res = internalUpdateInventoryRequestRecord(sess, req.getId(),
				InventoryStatus.DELIVERYED);
		if (res == Result.SUCCESS) {
			tr.commit();
		} else {
			tr.rollback();
		}
		sess.close();
		return res;
	}

	private Result internalUpdateInventoryRequestRecord(Session sess, long id,
			InventoryStatus is) {
		InventoryRequestRecord ir = (InventoryRequestRecord) sess.get(
				InventoryRequestRecord.class, id);
		if (ir == null) {
			return Result.ERR_GENERIC_INVALID_ID;
		}
		ir.setStatus(InventoryStatus.PREPARING_INVENTORY);

		sess.update(ir);
		return Result.SUCCESS;
	}

	public List<WFInventoryRequest> queryWFInventoryRequest(int start, int count) {
		return queryWFInventoryRequest(start, count, (InventoryStatus[]) null);
	}

	public List<WFInventoryRequest> queryWFInventoryRequest(int start,
			int count, InventoryStatus is) {
		return queryWFInventoryRequest(start, count,
				new InventoryStatus[] { is });
	}

	public List<WFInventoryRequest> queryWFInventoryRequest(int start,
			int count, InventoryStatus[] isArr) {
		StringBuffer hsqlBuffer = new StringBuffer();
		hsqlBuffer.append(" from InventoryRequestRecord ");
		if (isArr != null && isArr.length == 1) {
			hsqlBuffer.append(" where status = ? ");
		} else {
			hsqlBuffer.append(" where ");
			for (int i = 0; i < isArr.length; i++) {
				if (i == 0) {
					hsqlBuffer.append(" ( ");
					hsqlBuffer.append(" status = ? ");
				} else {
					hsqlBuffer.append(" or status = ? ");
					if (i == isArr.length - 1) {
						hsqlBuffer.append(" ) ");
					}
				}
			}
		}
		hsqlBuffer.append(" order by datetime asc ");

		Session sess = getSession();
		Query query = sess.createQuery(hsqlBuffer.toString());
		query.setFirstResult(start);
		query.setMaxResults(count);
		for (int i = 0; i < isArr.length; i++) {
			query.setInteger(i, isArr[i].ordinal());
		}

		List<InventoryRequestRecord> list = query.list();
		List<WFInventoryRequest> inventoryList = new ArrayList<WFInventoryRequest>(
				list.size());
		for (InventoryRequestRecord iur : list) {
			WFInventoryRequest wf = new WFInventoryRequest();
			wf.setId(iur.getId());
			wf.setDatetime(iur.getDatetime());
			if (iur.getOperator() != null) {
				wf.setOperator(userService.getUser(iur.getOperator().getId()));
			}
			wf.setIs(iur.getStatus());
			wf.setShop(shopService.getShop(iur.getId()));
			inventoryList.add(wf);
		}
		sess.close();
		return inventoryList;
	}

	public void queryInventoryRequestDetail(WFInventoryRequest wf) {
		Session sess = getSession();
		Query query = sess
				.createQuery(" from InventoryReqiestGoods  where record.id = ? ");
		query.setLong(0, wf.getId());
		List<InventoryRequestGoods> list = query.list();
		for (InventoryRequestGoods iur : list) {
			wf.addInventoryItem(goodsService.getGoods(iur.getGoods().getId()),
					iur.getCount(), true);
		}
		sess.close();
	}

	@Override
	public List<WFInventory> queryInventory(int start, int count) {
		Session sess = getSession();
		Query query = sess
				.createQuery(" from InventoryUpdateRecord order by datetime desc ");
		query.setFirstResult(start);
		query.setMaxResults(count);
		List<InventoryUpdateRecord> list = query.list();
		List<WFInventory> inventoryList = new ArrayList<WFInventory>(
				list.size());
		for (InventoryUpdateRecord iur : list) {
			WFInventory wf = new WFInventory();
			wf.setId(iur.getId());
			wf.setDatetime(iur.getDatetime());
			if (iur.getOperator() != null) {
				wf.setOperator(userService.getUser(iur.getOperator().getId()));
			}
			wf.setIt(iur.getIt());
			inventoryList.add(wf);
		}
		sess.close();
		return inventoryList;
	}

	@Override
	public List<WFInventory> queryInventory(Date startDate, Date endDate,
			int start, int count) {
		return null;
	}

	public void queryInventoryDetail(WFInventory wf) {
		Session sess = getSession();
		Query query = sess
				.createQuery(" from InventoryGoods  where record.id = ? ");
		query.setLong(0, wf.getId());
		List<InventoryGoods> list = query.list();
		WFBrand wfb = null;
		WFVendor wfv = null;
		
		for (InventoryGoods iur : list) {
			wfb = goodsService.getBrand(iur.getBrandName());
			wfv = goodsService.getVendor(iur.getVendorName());
			wf.addInventoryItem(goodsService.getGoods(iur.getGoods().getId()),
					wfb, wfv, iur.getCount(), iur.getPrice(), iur.getRate(), iur.getRate1(), iur.getRemCount(), true);
		}
		sess.close();
	}
	
	
	public Result removeWFIneventoryRequest(long id) {
		Session sess = getSession();
		
		Query query = sess.createQuery(" from InventoryRequestRecord where parentId = ?");
		query.setLong(0, id);
		List<InventoryRequestRecord> subRecords = query.list();
		
		Transaction tr = sess.beginTransaction();
		for (InventoryRequestRecord irr : subRecords) {
			Query goodsQuery = sess.createQuery(" delete from InventoryRequestGoods where record.id = ?");
			goodsQuery.setParameter(0, irr.getId());
			goodsQuery.executeUpdate();
			sess.delete(irr);
		}
		
		InventoryRequestRecord irc = (InventoryRequestRecord)sess.get(InventoryRequestRecord.class, id);
		if (irc == null) {
			tr.commit();
			return Result.ERR_NO_SUCH_INVENTORY_REQUEST;
		}
		
		Query goodsQuery = sess.createQuery(" delete from InventoryRequestGoods where record.id = ?");
		goodsQuery.setParameter(0, irc.getId());
		goodsQuery.executeUpdate();
		
		sess.delete(irc);
		tr.commit();
		return Result.SUCCESS;
	}
	
	
	
	public List<WFInventoryGoods> queryCurrentStock() {
		
		List<WFInventoryGoods> list = null;
		Session sess = getSession();
		StringBuffer sql = new StringBuffer();
		sql.append(" select wig.WF_GOOD_ID,  wig.WF_BRAND_NAME, wig.WF_VENDOR_NAME, wig.WF_PRICE, wig.WF_UNIT_COUNT,");
		sql.append(" wig.WF_UNIT_REM_COUNT, wi.WF_OPT_TIMESTAMP ");
		sql.append(" from wf_inventory_goods wig left join wf_inventory_record wi on wi.id = wig.wf_rec_id where wig.wf_unit_rem_count > 0 order by wf_opt_timestamp, wf_good_id ");
		Query query = sess.createSQLQuery(sql.toString());
		List<Object[]> qlist = query.list();
		list = new ArrayList<WFInventoryGoods>(qlist.size());
		BigInteger biId = null;
		BigDecimal biprice = null;
		BigDecimal biuc = null;
		BigDecimal biurc = null;
		for (Object[] obj : qlist) {
			biId = (BigInteger)obj[0];
			biprice = (BigDecimal)obj[3];
			biuc = (BigDecimal)obj[4];
			biurc = (BigDecimal)obj[5];
			WFInventoryGoods wfg = new WFInventoryGoods();
			wfg.setGoods(goodsService.getGoods(biId.longValue()));
			wfg.setBrand(goodsService.getBrand((String)obj[1]));
			wfg.setVendor(goodsService.getVendor((String)obj[2]));
			wfg.setCount(biuc.floatValue());
			wfg.setRemain(biurc.floatValue());
			wfg.setPrice(biprice.floatValue());
			wfg.setDate((Date)obj[6]);
			list.add(wfg);
		}
		
		return list;
	}
	
	
	public List<WFInventory> queryInventoryAccordingToRequest(long requestId) {
		Session sess = getSession();
		Query query = sess.createQuery(" from InventoryUpdateRecord where requestInventoryId = ?");
		query.setLong(0, requestId);
		List<InventoryUpdateRecord> iurList =query.list();
		List<WFInventory> list = new ArrayList<WFInventory>(iurList.size());
		for (InventoryUpdateRecord iur : iurList) {
			WFInventory wfi = new WFInventory();
			wfi.setId(iur.getId());
			wfi.setDatetime(iur.getDatetime());
			Query goodsQuery = sess.createQuery(" from InventoryGoods where record.id = ? ");
			goodsQuery.setLong(0, iur.getId());
			List<InventoryGoods> irgList = goodsQuery.list();
			for (InventoryGoods irg : irgList) {
				wfi.addInventoryItem(
						goodsService.getGoods(irg.getGoods().getId()),
						goodsService.getBrand(irg.getBrandName()),
						goodsService.getVendor(irg.getVendorName()),
						irg.getCount(), irg.getPrice(), irg.getRate(),
						irg.getRate1(), true);
			}
			
			list.add(wfi);
		}
		
		return list;
	}
	
	
	
	public List<WFInventory> querySubInventoryRequest(long requestId) {
		Session sess = getSession();

		Query query = sess.createQuery(" from InventoryUpdateRecord where requestInventoryId = ?");
		query.setLong(0, requestId);
		
		List<InventoryUpdateRecord> iurList =query.list();
		List<WFInventory> list = new ArrayList<WFInventory>(iurList.size());
		for (InventoryUpdateRecord iur : iurList) {
			WFInventory wfi = new WFInventory();
			wfi.setId(iur.getId());
			wfi.setDatetime(iur.getDatetime());
			Query goodsQuery = sess.createQuery(" from InventoryGoods where record.id = ? ");
			goodsQuery.setLong(0, iur.getId());
			List<InventoryGoods> irgList = goodsQuery.list();
			for (InventoryGoods irg : irgList) {
				wfi.addInventoryItem(
						goodsService.getGoods(irg.getGoods().getId()),
						goodsService.getBrand(irg.getBrandName()),
						goodsService.getVendor(irg.getVendorName()),
						irg.getCount(), irg.getPrice(), 0,
						0, true);
			}
			
			list.add(wfi);
		}
		return list;
	}
	
	
	public double queryCurrentInventoryCost() {
		Session sess = getSession();
		Query query = sess.createSQLQuery("  select sum(wf_price * WF_UNIT_REM_COUNT) from wf_inventory_goods where WF_UNIT_REM_COUNT > 0 ");
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
