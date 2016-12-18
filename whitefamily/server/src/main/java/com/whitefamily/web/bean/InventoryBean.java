package com.whitefamily.web.bean;

import java.util.ArrayList;
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
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFInventory;
import com.whitefamily.service.vo.WFInventoryGoods;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFVendor;

@ManagedBean(name = "inventoryBean", eager = false)
@RequestScoped
public class InventoryBean {

	
	IInventoryService inventoryService;
	IGoodsService  goodsService;
	private String errMsg;
	private WFInventory inventory;
	private long detailInventoryId;
	
	private List<WFInventoryGoods> wfiList;
	private List<WFInventoryGoods> cacheiList;
	private WFInventoryRequest inventoryRequest;
	
	
	// for filter goods
	private String filterGoodsName;
	private String filterCateName;
	private long filterCateId;
	
	
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
		String[] brands = map.get("brandname");
		String[] vendors = map.get("vendorname");
		String[] rate = map.get("rate");
		String[] rate1 = map.get("rate1");
		String[] prs = map.get("prs");
		String[] count = map.get("count");
		
		if (goods_id == null || prs == null || count == null || brands == null || vendors == null) {
			errMsg ="请输入要更新的库存的产品信息";
			return "createinventoryfailed";
		}
		
		if (goods_id.length != brands.length  || goods_id.length != vendors.length) {
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
			
			if (brands[i] == null || brands[i].isEmpty()) {
				errMsg ="产品品牌必须输入";
				return "createinventoryfailed";
			}
			
			if (vendors[i] == null || vendors[i].isEmpty()) {
				errMsg ="品牌供应商必须输入";
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
			
			if (rate[i] != null) {
				ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", rate[i]);
				if (!ma) {
					errMsg ="出货价格比例应为数字";
					return "createinventoryfailed";
				}
			}
			
			if (rate1[i] != null) {
				ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", rate1[i]);
				if (!ma) {
					errMsg ="出货加盟商价格比例应为数字";
					return "createinventoryfailed";
				}
			}
			
			WFBrand wfb = goodsService.getBrand(brands[i]);
			if (wfb == null) {
				wfb = new WFBrand();
				wfb.setName(brands[i]);
				goodsService.addBrand(wfb);
			}
			
			WFVendor wfv = goodsService.getVendor(vendors[i]);
			if (wfv == null) {
				wfv = new WFVendor();
				wfv.setName(vendors[i]);
				goodsService.addVendor(wfv);
			}
			
			inventory.addInventoryItem(g, wfb, wfv, Float.parseFloat(count[i]), Float.parseFloat(prs[i]),
					rate[i] == null ? 0 : Float.parseFloat(rate[i]), rate1[i] == null ? 0 : Float.parseFloat(rate1[i]),
					false);
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



	public List<WFInventoryGoods> getWfiList() {
		if (wfiList == null) {
			wfiList = this.inventoryService.queryCurrentStock();
			cacheiList = wfiList;
		}
		return wfiList;
	}



	public void setWfiList(List<WFInventoryGoods> wfiList) {
		this.wfiList = wfiList;
	}



	public String getFilterGoodsName() {
		return filterGoodsName;
	}



	public void setFilterGoodsName(String filterGoodsName) {
		this.filterGoodsName = filterGoodsName;
	}



	public String getFilterCateName() {
		return filterCateName;
	}



	public void setFilterCateName(String filterCateName) {
		this.filterCateName = filterCateName;
	}



	public long getFilterCateId() {
		return filterCateId;
	}



	public void setFilterCateId(long filterCateId) {
		this.filterCateId = filterCateId;
	}
	
	
	public void filterGoods() {
		if (this.filterCateId <=0 && (this.filterGoodsName == null || this.filterGoodsName.isEmpty())) {
			wfiList = this.cacheiList;
			return;
		}
		
		// filter goods
		List<WFInventoryGoods> newList = new ArrayList<WFInventoryGoods>(wfiList.size());

		List<WFInventoryGoods> dbList = wfiList;
		
		if (this.filterCateId > 0) {
			int len = dbList.size();
			for (int i = 0; i < len; i++) {
				WFInventoryGoods wig = dbList.get(i);
				WFCategory wfc = (WFCategory) wig.getGoods().getCate();
				while (wfc != null) {
					if (wfc.getId() == filterCateId) {
						newList.add(wig);
						break;
					}
					wfc = wfc.getParent();
				}

			}
			this.wfiList = newList;
			return;
		}

		if (filterGoodsName != null && !this.filterGoodsName.isEmpty()) {
			int len = dbList.size();
			for (int i = 0; i < len; i++) {
				WFInventoryGoods wig = dbList.get(i);
				if (wig.getGoods().getName().contains(this.filterGoodsName)
						|| wig.getGoods().getAbbr().contains(this.filterGoodsName)) {
					newList.add(wig);
				}
			}
			this.wfiList = newList;
			return;
		}
		
		this.wfiList = dbList;
	}
	
	
}
