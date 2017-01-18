package com.whitefamily.web.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.whitefamily.po.delivery.DeliverySupplierConfiguration.MC;
import com.whitefamily.service.ICategoryService;
import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.IInventoryService;
import com.whitefamily.service.IShopService;
import com.whitefamily.service.ISupplierService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFDelivery;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFSupplier;
import com.whitefamily.service.vo.WFSupplierMapping;

@ManagedBean(name = "supplierBean", eager = false)
@SessionScoped
public class SupplierBean {
	
	private List<WFInventoryRequest> inventoryAllList;
	private WFInventoryRequest inventoryRequestdetail;
	private List<WFDelivery> delist;
	
	private ISupplierService supplierService;
	private IShopService shopService;
	private IGoodsService goodsService;
	private ICategoryService categoryService;
	private IInventoryService inventoryService;
	
	
	
	private long prepareDerReqId;
	
	private String errMsg;
	
	private Date queryDeliveryDate;
	
	
	private List<WFSupplierMapping> commonMappings;
	private List<WFSupplierMapping> newMappings;
	
	
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;
	
	public SupplierBean() {
		super();
		supplierService = ServiceFactory.getSupplierService();
		shopService = ServiceFactory.getShopService();
		goodsService = ServiceFactory.getGoodsService();
		inventoryService = ServiceFactory.getInventoryService();
		categoryService = ServiceFactory.getCategoryService();
		filer();
	}

	public List<WFInventoryRequest> getInventoryAllList() {
		filer();
		return inventoryAllList;
	}
	
	public List<WFSupplierMapping> getCommonMappings() {
		if (commonMappings == null) {
			commonMappings = supplierService.getMappingList();
			for (WFSupplierMapping map : commonMappings) {
				if (map.getMc() == MC.CATE) {
					map.setCate(categoryService.getCategory(map.getMappingId()));
				} else if (map.getMc() == MC.GOODS) {
					map.setWfg(goodsService.getGoods(map.getMappingId()));
				}
			}
		}
		return commonMappings;
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
	
	
	
	
	
	public List<WFSupplierMapping> getNewMappings() {
		return newMappings;
	}

	public void addSupplierMapping() {
		newMappings = new ArrayList<WFSupplierMapping>();
		errMsg = null;
		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] mappingIds = map.get("mappingId");
		String[] mappingTypes = map.get("mappingType");
		String[] mappingNames = map.get("mappingNames");
		if (mappingIds == null || mappingTypes == null || mappingNames == null ||  mappingIds.length != mappingTypes.length
				|| mappingIds.length != mappingNames.length) {
			errMsg ="数据类型不匹配";
			return;
		}
		
		boolean checker = true;
		boolean flag;
		MC mc = null;
		for (int i = 0; i < mappingIds.length; i++) {
			flag = Pattern.matches("([0-9]+)", mappingIds[i]);
			if (!flag) {
				errMsg += mappingNames[i]+" 数据不正确， 请选择相应品类或产品";
				checker = false;
				continue;
			}
			
			if ((MC.GOODS.ordinal() +"").equals(mappingTypes[i])) {
				mc = MC.GOODS;
			} else if ((MC.CATE.ordinal() +"").equals(mappingTypes[i])) {
				mc = MC.CATE;
			} else {
				errMsg += mappingNames[i]+" 类型不正确， 请选择相应品类或产品";
				mc = null;
				checker = false;
				continue;
			}
			
			WFSupplierMapping mapping = new WFSupplierMapping();
			mapping.setMappingId(Long.parseLong(mappingIds[i]));
			mapping.setMc(mc);
			if (mc == MC.GOODS) {
				WFGoods wfg = this.goodsService.getGoods(mapping.getMappingId());
				mapping.setWfg(wfg);
			} else if (mc == MC.CATE) {
				WFCategory wfc = this.categoryService.getCategory(mapping.getMappingId());
				mapping.setCate(wfc);
			}
			mapping.setBounds(false);
			newMappings.add(mapping);
		}
		
		if (checker) {
			for (WFSupplierMapping mapping : newMappings) {
				this.supplierService.addProductMapping(mapping);
			}
			newMappings = null;
		}
	}
	
	
	public void removeMapping(int index) {
		if (index < 0 || index >= commonMappings.size()) {
			return;
		}
		WFSupplierMapping wfm = commonMappings.get(index);
		supplierService.removeProductMapping(wfm);
	}
}
