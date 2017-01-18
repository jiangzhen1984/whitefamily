package com.whitefamily.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.whitefamily.po.InventoryGoods;
import com.whitefamily.po.InventoryRequestRecord;
import com.whitefamily.po.InventoryStatus;
import com.whitefamily.po.InventoryType;
import com.whitefamily.po.InventoryUpdateRecord;
import com.whitefamily.po.delivery.DeliverySupplierConfiguration;
import com.whitefamily.service.vo.WFInventory;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFShop;
import com.whitefamily.service.vo.WFSupplier;
import com.whitefamily.service.vo.WFSupplierMapping;

public class SupplierService extends BaseService implements ISupplierService {
	

	private List<WFSupplierMapping> cacheList = new ArrayList<WFSupplierMapping>(20);
	
	private IShopService shopService;
	

	@Override
	public Result addProductMapping(WFSupplierMapping wfm) {
		DeliverySupplierConfiguration dsc = new DeliverySupplierConfiguration();
		dsc.setMappingId(wfm.getMappingId());
		dsc.setMc(wfm.getMc());
		dsc.setBounds(wfm.getBounds());
		dsc.setSupplierType(wfm.getSupplierType());
		dsc.setSupplierId(wfm.getSupplierId());
		
		Session sess = getSession();
		beginTransaction(sess);
		sess.save(dsc);
		commitTrans();
		cacheList.add(wfm);
		return Result.SUCCESS;
	}

	@Override
	public Result removeProductMapping(WFSupplierMapping wfm) {
		Session sess = getSession();
		DeliverySupplierConfiguration obj = (DeliverySupplierConfiguration)sess.get(DeliverySupplierConfiguration.class, wfm.getId());
		beginTransaction(sess);
		sess.delete(obj);
		commitTrans();
		cacheList.remove(wfm);
		return Result.SUCCESS;
	}
	
	
	@Override
	public List<WFSupplierMapping> getMappingList() {
		if (cacheList.size() <= 0) {
			Session sess = getSession();
			Query query = sess.createQuery(" from DeliverySupplierConfiguration where WF_SUPPLIER_BOUND = 0");
			List<DeliverySupplierConfiguration> list = query.list();
			for (DeliverySupplierConfiguration dsc : list) {
				cacheList.add(new WFSupplierMapping(dsc));
			}
		}
		return cacheList;
	}

	
	@Override
	public List<WFInventoryRequest> querySupplierDeliveryRequest(WFSupplier suppler, Date date) {
		StringBuffer hqlBuf = new StringBuffer();
		hqlBuf.append(" from InventoryRequestRecord where status = ?");
		
		hqlBuf.append(" order by status, datetime asc ");
		
		Session sess = getSession();
		Query query = sess.createQuery(hqlBuf.toString());
		query.setInteger(0, InventoryStatus.REQUEST.ordinal());
		
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
	
	
	
	public Result prepareInventoryRequest(WFInventoryRequest req, WFSupplier supplier) {

		Session sess = getSession();
		
		Query drquery = sess.createQuery(" from InventoryUpdateRecord where requestInventoryId = ? ");
		drquery.setLong(0, req.getId());
		List<InventoryUpdateRecord> iruList =  drquery.list();
		boolean update  = iruList.size() > 0? true : false;
		beginTransaction(sess);
		if (!update) {
			InventoryUpdateRecord record = new InventoryUpdateRecord();
			record.setIt(InventoryType.IN);
			record.setDatetime(new Date());
			record.setOperator(supplier);
			record.setRequestInventoryId(req.getId());
			sess.save(record);
			
			List<WFInventoryRequest.Item> supplierItemList = req.getSupplierItemList();
			for (WFInventoryRequest.Item wfi : supplierItemList) {
				InventoryGoods ig = new InventoryGoods();
				ig.setGoods(wfi.getGoods());
				ig.setCount(wfi.getRealCount());
				ig.setPrice(wfi.getPrice());
				ig.setRemCount(wfi.getRealCount());
				ig.setRecord(record);
				sess.save(ig);
			}
		} else {
			long updateRecordId = iruList.get(0).getId();
			//Update
			Query  updateQuery = null;
			List<WFInventoryRequest.Item> supplierItemList = req.getSupplierItemList();
			for (WFInventoryRequest.Item wfi : supplierItemList) {
				updateQuery = sess.createQuery(" from InventoryGoods where record.id = ? and goods.id = ? ");
				updateQuery.setLong(0, updateRecordId);
				updateQuery.setLong(1, wfi.getGoods().getId());
				
				InventoryGoods g = (InventoryGoods)updateQuery.list().iterator().next();
				g.setPrice(wfi.getPrice());
				g.setRemCount(wfi.getRealCount());
				g.setCount(wfi.getRealCount());
				sess.update(g);
			}
		}
		
		commitTrans();
		
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

	public IShopService getShopService() {
		return shopService;
	}

	public void setShopService(IShopService shopService) {
		this.shopService = shopService;
	}

	
	
	
}
