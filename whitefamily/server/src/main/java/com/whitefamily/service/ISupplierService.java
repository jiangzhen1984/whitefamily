package com.whitefamily.service;

import java.util.Date;
import java.util.List;

import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFSupplier;
import com.whitefamily.service.vo.WFSupplierMapping;

public interface ISupplierService {

	
	public Result addProductMapping(WFSupplierMapping wfm);
	
	
	public Result removeProductMapping(WFSupplierMapping wfm);
	
	
	public List<WFSupplierMapping> getMappingList();
	
	
	public List<WFInventoryRequest> querySupplierDeliveryRequest(WFSupplier suppler, Date date);
	
	
	
	public Result prepareInventoryRequest(WFInventoryRequest req, WFSupplier supplier);
}
