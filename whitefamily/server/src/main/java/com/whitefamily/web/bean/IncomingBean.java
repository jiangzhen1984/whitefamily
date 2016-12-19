package com.whitefamily.web.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.whitefamily.service.IShopService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFOperationCost;
import com.whitefamily.service.vo.WFShop;
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
		monthlyCost = new WFOperationCost();
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
	
	
	
	public void queryMonthly() {
		List<WFOperationCost> lc = shopService.queryShopMonthlyOperationCost(shopService.getShop(shopId), startDate);
		if (lc != null && lc.size()  > 0) {
			monthlyCost = lc.get(0);
		}
	}
	
	
	public String selectDetailInventoryCost(int index) {
		return "incoming_delivery_detail";
	}
	
	public void setDetailInventoryIndex(int index) {
		detail = inventoryCost.get(index);
	}
	
	
	private String errMsg;
	private long rshopId;
	private String rshopName;
	private WFOperationCost monthlyCost;
	public String reportMonthlyCost() {
		if (rshopId <=0) {
			errMsg =" 请选择店铺信息";
			return "monthly_report_failed";
		}
		WFShop wfs = shopService.getShop(rshopId);
		if (wfs == null) {
			errMsg =" 请选择店铺信息";
			return "monthlyReportFailed";
		}
		
		//TODO check
		List<WFOperationCost> list = shopService.queryShopMonthlyOperationCost(wfs, new Date());
		if (list != null && list.size() > 0) {
			errMsg =wfs.getName()+" 当月已经填报过月报表，无法重复填报";
			return "monthlyReportFailed";
		}
		monthlyCost.setShop(wfs);
		monthlyCost.setDate(new Date());
		if (monthlyCost.getEmployeesCost() != null) {
			monthlyCost.getEmployeesCost().clear();
		}
		
		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] names = map.get("names");
		String[] salarys = map.get("salars");
		
		if (salarys != null) {
			boolean ma;
			for (int i = 0; i < salarys.length; i++) {
				String str = salarys[i];
				ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+",  str);
				if (!ma) {
					errMsg = "工资应为数字";
					return "monthlyReportFailed";
				}
				
				monthlyCost.addEmployeeCost(names.length > i ? names[i]: "", Float.parseFloat(str));
			}
		}
		
		
		shopService.addMonthlyOperationCost(monthlyCost);
		rshopId = 0;
		rshopName = null;
		return "monthlyShow";
	}




	public long getRshopId() {
		return rshopId;
	}




	public void setRshopId(long rshopId) {
		this.rshopId = rshopId;
	}




	public String getRshopName() {
		return rshopName;
	}




	public void setRshopName(String rshopName) {
		this.rshopName = rshopName;
	}




	public WFOperationCost getMonthlyCost() {
		return monthlyCost;
	}
	
	
	
	public String gotoReportMonthlyReport() {
		monthlyCost = new WFOperationCost();
		return "gotoMonthlyReport";
	}




	public String getErrMsg() {
		return errMsg;
	}
	
	
	
}
