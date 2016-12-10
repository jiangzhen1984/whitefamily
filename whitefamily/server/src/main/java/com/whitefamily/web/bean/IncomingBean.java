package com.whitefamily.web.bean;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.whitefamily.service.IShopService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFOperationCost;
import com.whitefamily.service.vo.WFShopInventoryCost;

@ManagedBean(name = "incomingBean", eager = false)
@SessionScoped
public class IncomingBean {
	
	private IShopService shopService;
	
	private WFIncoming incoming;
	private WFOperationCost  cost;
	private List<WFShopInventoryCost> inventoryCost;
	
	private Date startDate;
	private Date endDate;
	
	private int type;
	
	private long shopId;
	
	private WFShopInventoryCost detail;
	
	
	public IncomingBean() {
		type = 1;
		shopService = ServiceFactory.getShopService();
	}




	public WFIncoming getIncoming() {
		return incoming;
	}




	public WFOperationCost getCost() {
		return cost;
	}





	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	
	
	public long getShopId() {
		return shopId;
	}


	public void setShopId(long shopId) {
		this.shopId = shopId;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}
	
	


	public List<WFShopInventoryCost> getInventoryCost() {
		return inventoryCost;
	}




	public WFShopInventoryCost getDetail() {
		return detail;
	}




	public void query() {
		incoming = new WFIncoming();
		cost = new WFOperationCost();
		List<WFIncoming> incomingList = null;
		List<WFOperationCost> costList= null;
		if (shopId <= 0) {
			incomingList = shopService.queryShopIncoming(startDate, endDate);
			costList = shopService.queryShopOperationCost(startDate, endDate);
		
		} else {
			incomingList = shopService.queryShopIncoming(shopService.getShop(shopId), startDate, endDate);
			costList = shopService.queryShopOperationCost(shopService.getShop(shopId), startDate, endDate);
		}
		
		if (incomingList != null && incomingList.size() > 0) {
			for (WFIncoming wfi : incomingList) {
				incoming.setAli(wfi.getAli() + incoming.getAli());
				incoming.setZls(wfi.getZls()+ incoming.getZls());
				incoming.setCash(wfi.getCash()+ incoming.getCash());
				incoming.setDazhong(wfi.getDazhong()+ incoming.getDazhong());
				incoming.setDaZhongaf(wfi.getDazhongaf()+ incoming.getDazhongaf());
				incoming.setDelivery(wfi.getDelivery()+ incoming.getDelivery());
				incoming.setNuomi(wfi.getNuomi()+ incoming.getNuomi());
				incoming.setWeixin(wfi.getWeixin()+ incoming.getWeixin());
				incoming.setNuomiaf(wfi.getNuomiaf()+ incoming.getNuomiaf());
			}
		}
		if (costList != null && costList.size() > 0) {
			for (WFOperationCost woc : costList) {
				cost.setBc(woc.getBc() + woc.getBc());
				cost.setDf(woc.getDf()+ cost.getDf());
				cost.setFf(woc.getFf()+ cost.getFf());
				cost.setGz(woc.getGz()+ cost.getGz());
				cost.setHsf(woc.getHsf()+ cost.getHsf());
				cost.setQt(woc.getQt()+ cost.getQt());
				cost.setRqf(woc.getRqf()+ cost.getRqf());
				cost.setRytl(woc.getRytl()+ cost.getRytl());
				cost.setRz(woc.getRz()+ cost.getRz());
				cost.setSb(woc.getSb()+ cost.getSb());
				cost.setSf(woc.getSf()+ cost.getSf());
				cost.setYl(woc.getYl()+ cost.getYl());
			}
		}
		
		inventoryCost = shopService.queryShopInventoryCost(shopService.getShop(shopId), startDate, endDate);
	}
	
	
	
	public String selectDetailInventoryCost(int index) {
		return "incoming_delivery_detail";
	}
	
	public void setDetailInventoryIndex(int index) {
		detail = inventoryCost.get(index);
	}
	
}
