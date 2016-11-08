package com.whitefamily.web.bean;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.whitefamily.service.IShopService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFOperationCost;

@ManagedBean(name = "incomingBean", eager = false)
@SessionScoped
public class IncomingBean {
	
	private IShopService shopService;
	
	private List<WFIncoming> incomingList;
	private List<WFOperationCost>  costList;
	
	private Date startDate;
	private Date endDate;
	
	private int type;
	
	
	public IncomingBean() {
		type = 1;
		shopService = ServiceFactory.getShopService();
	}


	public List<WFIncoming> getIncomingList() {
		return incomingList;
	}


	public List<WFOperationCost> getCostList() {
		return costList;
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
	
	
	
	
	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public void query() {
		incomingList = shopService.queryShopIncoming(startDate, endDate);
		costList = shopService.queryShopOperationCost(startDate, endDate);
	}
	
}
