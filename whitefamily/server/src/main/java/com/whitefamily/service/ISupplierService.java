package com.whitefamily.service;

import java.util.List;

import com.whitefamily.service.vo.WFSupplierMapping;

public interface ISupplierService {

	
	public Result addProductMapping(WFSupplierMapping wfm);
	
	
	public Result removeProductMapping(WFSupplierMapping wfm);
	
	
	public List<WFSupplierMapping> getMappingList();
}
