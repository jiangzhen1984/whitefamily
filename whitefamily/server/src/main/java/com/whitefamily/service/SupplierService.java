package com.whitefamily.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.whitefamily.po.delivery.DeliverySupplierConfiguration;
import com.whitefamily.service.vo.WFSupplierMapping;

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

}
