package com.whitefamily.service.vo;

import com.whitefamily.po.delivery.DeliverySupplierConfiguration;

public class WFSupplierMapping extends DeliverySupplierConfiguration {

	public WFSupplierMapping() {
		super();
	}
	
	
	public WFSupplierMapping(DeliverySupplierConfiguration conf) {
		this.setId(conf.getId());
		this.setMc(conf.getMc());
		this.setMappingId(conf.getMappingId());
	}
	
	
	

}
