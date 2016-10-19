package com.whitefamily.web.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.whitefamily.po.InventoryType;
import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.IInventoryService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFInventory;
import com.whitefamily.service.vo.WFInventoryRequest;

@ManagedBean(name = "inventoryBean", eager = false)
@RequestScoped
public class InventoryBean {

	
	IInventoryService inventoryService;
	IGoodsService  goodsService;
	private String errMsg;
	private WFInventory inventory;
	private long detailInventoryId;
	
	private WFInventoryRequest inventoryRequest;
	
	
	
	public InventoryBean() {
		super();
		inventoryService = ServiceFactory.getInventoryService();
		goodsService = ServiceFactory.getGoodsService();
	}
	
	
	
	public List<WFInventory> getInventoryList() {
		return inventoryService.queryInventory(0, 50);
	}

	public String getErrMsg() {
		return errMsg;
	}




	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}




	public WFInventory getInventory() {
		return inventory;
	}




	public void setInventory(WFInventory inventory) {
		this.inventory = inventory;
	}

	
	



	public long getDetailInventoryId() {
		return detailInventoryId;
	}



	public void setDetailInventoryId(long detailInventoryId) {
		this.detailInventoryId = detailInventoryId;
		inventory = new WFInventory();
		inventory.setId(this.detailInventoryId);
		inventoryService.queryInventoryDetail(inventory);
	}
	
	public String gotoInventoryDetail() {
		return "gotoinventorydetail";
	}

	
	public String gotoInventoryRequestCreatePage() {
		return "gotocreateinventoryrequestpage";
	}


	public String createInventory() {
		inventory = null;
		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] goods_id = map.get("goodsname_id");
		String[] brands_id = map.get("brandname_id");
		String[] prs = map.get("prs");
		String[] count = map.get("count");
		
		if (goods_id == null || prs == null || count == null) {
			errMsg ="请输入要更新的库存的产品信息";
			return "createinventoryfailed";
		}
		
		if (inventory != null) {
			inventory.clearItems();
		} else {
			inventory =  new WFInventory();
		}
		boolean ma = false;
		for (int i = 0; i < goods_id.length; i++) {
			if (goods_id[i] == null || goods_id[i].isEmpty()) {
				errMsg ="请输入要更新的库存的产品信息";
				return "createinventoryfailed";
			}
			WFGoods g = goodsService.getGoods(Long.parseLong(goods_id[i]));
			
			if (g == null) {
				errMsg ="没有找到相关产品： " + goods_id[i];
				return "createinventoryfailed";
			}
			
			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", count[i]);
			if (!ma) {
				errMsg ="产品数量应为数字";
				return "createinventoryfailed";
			}
			
			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", prs[i]);
			if (!ma) {
				errMsg ="产品价格应为数字";
				return "createinventoryfailed";
			}
			
			WFBrand b = null;
			if (brands_id[i] != null && !brands_id[i].isEmpty()) {
				b = goodsService.getBrand(Long.parseLong(brands_id[i]));
			}
			inventory.addInventoryItem(g, b, Float.parseFloat(count[i]), Float.parseFloat(prs[i]), false);
			inventory.setDatetime(new Date());
			inventory.setIt(InventoryType.IN);
		}
		inventoryService.createInventory(inventory);
		
		return "success";
	}
	
	
	
	
	public String createInventoryRequest() {
		inventoryRequest = null;
//		if (userBean.getUser().getRole() != Role.MANAGER) {
//			errMsg ="只有店长可以创建补货申请";
//			return "createinventoryRequestfailed";
//		}
		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] goods_id = map.get("goodsname_id");
		String[] count = map.get("count");
		
		if (inventoryRequest != null) {
			inventoryRequest.clearItems();
		} else {
			inventoryRequest =  new WFInventoryRequest();
		}
		boolean ma = false;
		for (int i = 0; i < goods_id.length; i++) {
			if (goods_id[i] == null || goods_id[i].isEmpty()) {
				errMsg ="请输入要更新的库存的产品信息";
				return "createinventoryRequestfailed";
			}
			WFGoods g = goodsService.getGoods(Long.parseLong(goods_id[i]));
			
			if (g == null) {
				errMsg ="没有找到相关产品： " + goods_id[i];
				return "createinventoryRequestfailed";
			}
			
			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", count[i]);
			if (!ma) {
				errMsg ="产品数量应为数字";
				return "createinventoryRequestfailed";
			}
			
			inventoryRequest.addInventoryItem(g, Float.parseFloat(count[i]), false);
		}
		inventoryRequest.setDatetime(new Date());
		//inventoryRequest.setShop(((WFManager)userBean.getUser()).getShop());
		inventoryService.createInventoryRequest(inventoryRequest);
		
		return "successcreateinveontryrequest";
	
	}
	
}
