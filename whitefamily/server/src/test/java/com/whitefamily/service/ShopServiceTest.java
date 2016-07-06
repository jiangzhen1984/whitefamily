package com.whitefamily.service;


import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFDamage;
import com.whitefamily.service.vo.WFInventory;
import com.whitefamily.service.vo.WFShop;

public class ShopServiceTest extends TestCase {

	IShopService service;
	ICategoryService service1;
	 
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		service = new ShopService();
		service1 = new CategoryService();
	}

	@Test
	public void testReportDamage() {
		WFCategory wf = new WFCategory();
		service1.addCategory(wf);
		
		WFShop wfs = new WFShop();
		service.addShop(wfs);
		
		WFDamage d = new WFDamage();
		d.setCate(wf);
		d.setTime(new Date());
		d.setQuality(230.F);
	//	service.reportDamage(wfs, d, null);
	}

	@Test
	public void testReportInventory() {
		WFCategory wf = new WFCategory();
		service1.addCategory(wf);
		
		WFShop wfs = new WFShop();
		service.addShop(wfs);
		
		
		
		WFInventory d = new WFInventory();
//		d.setTime(new Date());
//		d.setQuality(230.F);
//		d.setCate(wf);
		//service.reportInventory(wfs, d, null);
	}

}
