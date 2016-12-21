package com.whitefamily.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.whitefamily.po.InventoryStatus;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFInventory;
import com.whitefamily.service.vo.WFInventoryGoods;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFShop;

public interface IInventoryService {
	
	
	public void createInventory(WFInventory inventory);
	
	
	public Result createInventoryRequest(WFInventoryRequest request);
	
	public Result prepareInventoryRequest(WFInventoryRequest req);
	
	public Result deliveryInventoryRequest(WFInventoryRequest req);
	
	public Result finishInventoryRequest(WFInventoryRequest req);
	
	
    public List<WFInventoryRequest> queryWFInventoryRequest(int start, int count);
    
    public List<WFInventoryRequest> queryWFInventoryRequest(int start, int count, InventoryStatus is);
    
    public List<WFInventoryRequest> queryWFInventoryRequest(int start, int count, InventoryStatus[] isArr);
    
    public WFInventoryRequest queryShopWFInventoryRequest(WFShop shop, Date date);
	
	public void queryInventoryRequestDetail(WFInventoryRequest wf);
	
	public Result removeWFIneventoryRequest(long id);
	
	
	public List<WFInventory> queryInventory(int start, int count);
	
	public List<WFInventory> queryInventory(Date startDate, Date endDate, int start, int count);
	
	public void queryInventoryDetail(WFInventory wf);
	
	
	public List<WFInventoryGoods> queryCurrentStock();
	
	
	public List<WFInventory> queryInventoryAccordingToRequest(long requestId);
	
	public List<WFInventory> querySubInventoryRequest(long requestId);
	
	
	public double queryCurrentInventoryCost();
	
	public List<WFGoods> queryStockAlerting();
	
	
	public Map<WFGoods, Float> queryCurrentStockMap();

}
