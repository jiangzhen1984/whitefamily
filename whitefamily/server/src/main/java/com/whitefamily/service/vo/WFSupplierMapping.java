package com.whitefamily.service.vo;

import com.whitefamily.po.delivery.DeliverySupplierConfiguration;

public class WFSupplierMapping extends DeliverySupplierConfiguration {
	
	private WFCategory cate;
	
	private WFGoods wfg;

	public WFSupplierMapping() {
		super();
	}
	
	
	public WFSupplierMapping(DeliverySupplierConfiguration conf) {
		this.setId(conf.getId());
		this.setMc(conf.getMc());
		this.setMappingId(conf.getMappingId());
		this.setBounds(conf.getBounds());
		this.setSupplierType(conf.getSupplierType());
		this.setSupplierId(conf.getSupplierId());
	}


	public WFCategory getCate() {
		return cate;
	}


	public void setCate(WFCategory cate) {
		this.cate = cate;
	}


	public WFGoods getWfg() {
		return wfg;
	}


	public void setWfg(WFGoods wfg) {
		this.wfg = wfg;
	}
	
	
	
	public String getStrName() {
		return this.getMc() == MC.CATE ? (cate == null ? "未知分类" : cate.getName())
				: (wfg == null ? "未知产品" : wfg.getName());
	}
	

}
