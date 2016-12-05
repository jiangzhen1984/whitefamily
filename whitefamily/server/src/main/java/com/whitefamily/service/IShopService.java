package com.whitefamily.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.whitefamily.po.DamageStatus;
import com.whitefamily.service.vo.WFDamageReport;
import com.whitefamily.service.vo.WFDelivery;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFManager;
import com.whitefamily.service.vo.WFOperationCost;
import com.whitefamily.service.vo.WFShop;
import com.whitefamily.service.vo.WFShopInventoryCost;
import com.whitefamily.service.vo.WFUser;

public interface IShopService {
	
	
	public WFShop getShop(long id);
	
	public WFShop addShop(WFShop shop);
	
	public WFShop updateShop(WFShop shop);
	
	public WFShop getShop(WFManager wfm);
	
	public void removeShop(WFShop shop);
	
	public List<WFShop> getShopList();
	
	public List<WFDamageReport> queryDamageReport(int start, int count);
	
	public List<WFDamageReport> queryDamageReport(int start, int count, DamageStatus ds);
	
	public List<WFDamageReport> queryDamageReport(int start, int count, WFShop shop);
	
	public Result reportDamage(WFDamageReport report, WFShop shop, WFUser manager);
	
	public void queryDamageReportDetail(WFDamageReport wr);
	
	public void reportIncoming(WFShop shop, WFIncoming incoming, WFOperationCost cost, WFUser manager);
	
	public WFIncoming queryShopIncoming(WFShop shop, Date date);
	
	public WFOperationCost queryShopOperationCost(WFShop shop, Date date);
	
	public Result requestInventory(WFInventoryRequest inventory, WFShop shop, WFUser manager);
	
	public List<WFInventoryRequest> queryInventoryRequestList(int start, int count);
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop, Date date);
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop, Date start, Date end);
	
	public List<WFInventoryRequest> queryInventoryRequestList(WFShop shop);
	
	public List<WFInventoryRequest> queryInventoryRequestList(Date date);
	
	public List<WFInventoryRequest> queryInventoryRequestList(Date start, Date end);
	
	public Result queryInventoryRequestGoods(WFInventoryRequest ir);
	
	public File generateDeliveryForm(WFInventoryRequest ir);
	
	
	public File generateDeliveryForm(WFDelivery de);
	
	public Result prepareDelivery(WFDelivery de, WFUser user);
	
	
    public  List<WFIncoming>  queryShopIncoming(WFShop shop, Date start, Date end);
	
	public  List<WFOperationCost>   queryShopOperationCost(WFShop shop, Date start, Date end);
	
	public  List<WFIncoming>  queryShopIncoming(Date start, Date end);
		
	public  List<WFOperationCost>   queryShopOperationCost(Date start, Date end);
		
	
	
	public Result updateShopIncomingAndOperationCost(WFShop shop, WFIncoming wf, WFOperationCost cost, Date date);
	
	public Result handleInternalDelivery(WFDelivery de, WFUser user);
	
	
	
	public List<WFDelivery> queryDelivery(WFShop shop, Date start, Date end);
	
	
	public List<WFShopInventoryCost> queryShopInventoryCost(WFShop shop, Date start, Date end);
	
	
	public double queryTotalIncoming();
	
	
	public double queryTotalOperationCost();

}
