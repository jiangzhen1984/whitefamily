package com.whitefamily.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
import com.whitefamily.service.vo.WFInventoryRequest.Item;

public class SupplierService extends BaseService implements ISupplierService {
	

	private List<WFSupplierMapping> cacheList = new ArrayList<WFSupplierMapping>(20);
	
	
	

	@Override
	public Result addProductMapping(WFSupplierMapping wfm) {
		DeliverySupplierConfiguration dsc = new DeliverySupplierConfiguration();
		dsc.setMappingId(wfm.getMappingId());
		dsc.setMc(wfm.getMc());
		Session sess = getSession();
		Transaction tr = sess.beginTransaction();
		sess.save(dsc);
		tr.commit();
		cacheList.add(wfm);
		return Result.SUCCESS;
	}

	@Override
	public Result removeProductMapping(WFSupplierMapping wfm) {
		Session sess = getSession();
		DeliverySupplierConfiguration obj = (DeliverySupplierConfiguration)sess.get(DeliverySupplierConfiguration.class, wfm.getId());
		Transaction tr = sess.beginTransaction();
		sess.delete(obj);
		tr.commit();
		cacheList.remove(wfm);
		return Result.SUCCESS;
	}
	
	
	@Override
	public List<WFSupplierMapping> getMappingList() {
		if (cacheList.size() <= 0) {
			Session sess = getSession();
			Query query = sess.createQuery(" from DeliverySupplierConfiguration ");
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
		hqlBuf.append(" from InventoryRequestRecord where supplierRecd = ? and status = ?");
		
		hqlBuf.append(" order by status, datetime asc ");
		
		Session sess = getSession();
		Query query = sess.createQuery(hqlBuf.toString());
		query.setInteger(0, InventoryRequestRecord.TYPE_IS_SUPPLIER);
		query.setInteger(1, InventoryStatus.REQUEST.ordinal());
		
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
		
		sess.close();
		return wflist;
	}
	
	
	
	public Result prepareInventoryRequest(WFInventoryRequest req, WFSupplier supplier) {

		Session sess = getSession();
		
		
		InventoryRequestRecord irr = (InventoryRequestRecord)sess.get(InventoryRequestRecord.class, req.getId());
		if (irr == null) {
			return Result.ERR_EXIST_INVENTORY_RECORD;
		}
		irr.setStatus(InventoryStatus.PREPARING_INVENTORY);
		
		
		WFInventory inventory =  new WFInventory();
		inventory.setDatetime(new Date());
		inventory.setIt(InventoryType.IN);
		inventory.setOperator(supplier);
		
		List<Item> list = req.getItemList();
		for (WFInventoryRequest.Item wfi : list) {
			inventory.addInventoryItem(wfi.getGoods(), null, null, wfi.getRealCount(), wfi.getPrice(), 0, 0, false);
		}
		
		Transaction tr = sess.beginTransaction();
		createInventory(sess, inventory, req);
		
		sess.update(irr);
		
		
		tr.commit();
		
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

}
