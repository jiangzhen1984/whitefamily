package com.whitefamily.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.whitefamily.po.InventoryGoods;
import com.whitefamily.po.InventoryRequestGoods;
import com.whitefamily.po.InventoryRequestRecord;
import com.whitefamily.po.InventoryStatus;
import com.whitefamily.po.InventoryType;
import com.whitefamily.po.InventoryUpdateRecord;
import com.whitefamily.po.delivery.DeliverySupplierConfiguration;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFInventory;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFSupplierMapping;

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
		for (int i = 0; i < count; i++) {
			WFInventory.Item wi = inventory.getItem(i);
			if (wi.isPersisted()) {
				continue;
			}
			ig = new InventoryGoods();
			if (wi.getBrand() != null) {
				ig.setBrand(goodsService.getBrand(wi.getBrand().getId()));
			}
			ig.setGoods(goodsService.getGoods(wi.getGoods().getId()));
			ig.setCount(wi.getCount());
			ig.setPrice(wi.getPrice());
			ig.setRecord(record);
			sess.save(ig);
			wi.setPersisted(true);
		}

		tr.commit();
		sess.close();

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
		for (InventoryGoods iur : list) {
			WFBrand bra = null;
			if (iur.getBrand() != null) {
				bra = goodsService.getBrand(iur.getBrand().getId());
			}
			wf.addInventoryItem(goodsService.getGoods(iur.getGoods().getId()),
					bra, iur.getCount(), iur.getPrice(), true);
		}
		sess.close();
	}
}
