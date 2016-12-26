package com.whitefamily.web.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.IInventoryService;
import com.whitefamily.service.IShopService;
import com.whitefamily.service.ISupplierService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFDelivery;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFSupplier;

@ManagedBean(name = "supplierBean", eager = false)
@SessionScoped
public class SupplierBean {
	
	private List<WFInventoryRequest> inventoryAllList;
	private WFInventoryRequest inventoryRequestdetail;
	private List<WFDelivery> delist;
	
	private ISupplierService supplierService;
	private IShopService shopService;
	private IGoodsService goodsService;
	private IInventoryService inventoryService;
	
	
	
	private long prepareDerReqId;
	
	private String errMsg;
	
	private Date queryDeliveryDate;
	
	
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;
	
	public SupplierBean() {
		super();
		supplierService = ServiceFactory.getSupplierService();
		shopService = ServiceFactory.getShopService();
		goodsService = ServiceFactory.getGoodsService();
		inventoryService = ServiceFactory.getInventoryService();
		filer();
	}

	public List<WFInventoryRequest> getInventoryAllList() {
		filer();
		return inventoryAllList;
	}
	
	
	
	
	public void filer() {
		inventoryAllList = supplierService.querySupplierDeliveryRequest(null, new Date());
	}

	
	
	public void setInventoryRequestDetailIdxFromAll(int idx) {
		inventoryRequestdetail = this.inventoryAllList.get(idx);
		if (!inventoryRequestdetail.isLoadItem()) {
			shopService.queryInventoryRequestGoods(inventoryRequestdetail);
		}
		this.prepareDerReqId = inventoryRequestdetail.getId();
	}
	
	
	public String gotoViewShopInventoryRequestDetail() {
		return "gotoViewshopinventoryrequestdetail";
	}

	public WFInventoryRequest getInventoryRequestdetail() {
		return inventoryRequestdetail;
	}
	
	
	public String gotoDelivery() {
		return "gotoVegDeliver";
	}
	
	
	public String prepareDelivery() {

		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] goods_id = map.get("gi");
		String[] price = map.get("gp");
		String[] realCount = map.get("rc");
		
		if (goods_id == null && realCount == null) {
			errMsg = "请输入配送价格和数量";
			return "prepareDeliveryFailed";
		}
		errMsg = "";
	    boolean errorFlag = false;
		
		boolean ma = false;
		for (int i = 0; i < goods_id.length; i++) {
			if (goods_id[i] == null || goods_id[i].isEmpty()) {
				errMsg = "请输入产品信息";
				errorFlag = true;
				continue;
			}
			WFGoods g = goodsService.getGoods(Long.parseLong(goods_id[i]));

			if (g == null) {
				errMsg = "没有找到相关产品： " + goods_id[i] +" \n";
				errorFlag = true;
				continue;
			}

			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", realCount[i]);
			if (!ma) {
				errMsg += g.getName()+"配送数量应为数字" +" \n";
				errorFlag = true;
				continue;
			}

			
			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", price[i]);
			if (!ma) {
				errMsg += g.getName()+" 配送价格应为数字";
				errorFlag = true;
				continue;
			}
			
			inventoryRequestdetail.updateInventoryItem(g,  Float.parseFloat(realCount[i]),  Float.parseFloat(price[i]), Float.parseFloat(realCount[i]));
		}
		if (errorFlag) {
			return "prepareDeliveryFailed";
		}
		inventoryRequestdetail.setDatetime(new Date());
		
		supplierService.prepareInventoryRequest(inventoryRequestdetail, (WFSupplier)userBean.getUser());

		return "prepareDeliverysuccess";
	
	}
	
	
	

	public long getPrepareDerReqId() {
		return prepareDerReqId;
	}

	public void setPrepareDerReqId(long prepareDerReqId) {
		this.prepareDerReqId = prepareDerReqId;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public Date getQueryDeliveryDate() {
		return queryDeliveryDate;
	}

	public void setQueryDeliveryDate(Date queryDeliveryDate) {
		this.queryDeliveryDate = queryDeliveryDate;
	}
	
	
	public List<WFDelivery> getDelist() {
		return delist;
	}

	public String queryHistory() {
		this.delist = inventoryService.queryDeliveryHistory(queryDeliveryDate);
		return "query";
	}
	
}
