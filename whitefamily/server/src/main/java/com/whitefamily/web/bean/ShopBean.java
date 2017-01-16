package com.whitefamily.web.bean;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.whitefamily.ServerConstants;
import com.whitefamily.po.ShopType;
import com.whitefamily.po.customer.Role;
import com.whitefamily.po.incoming.DeliveryType;
import com.whitefamily.po.incoming.GroupOnType;
import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.IInventoryService;
import com.whitefamily.service.IShopService;
import com.whitefamily.service.Result;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFDamageReport;
import com.whitefamily.service.vo.WFDelivery;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFIncoming.DeliveryItem;
import com.whitefamily.service.vo.WFIncoming.GroupOnItem;
import com.whitefamily.service.vo.WFInventory;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFInventoryRequest.Item;
import com.whitefamily.service.vo.WFManager;
import com.whitefamily.service.vo.WFOperationCost;
import com.whitefamily.service.vo.WFShop;
import com.whitefamily.service.vo.WFShopInventory;

@ManagedBean(name = "shopBean", eager = false)
@SessionScoped
public class ShopBean {

	private String name;
	private String address;
	private long shopId;
	private String errMsg;
	private String nameRequire;
	private int shopType;

	IShopService shopService;
	IGoodsService goodsService;
	IInventoryService inventoryService;

	private long reportId;
	private WFDamageReport report;
	private WFShopInventory shopInventoryReport;
	private String reportResultType;
	private WFInventoryRequest inventoryRequest;
	private ShopIncoming shopIncoming;
	private long viewShopIncomingId;
	private Date viewShopIncomingDate;
	private Date viewShopInventoryDate;

	private List<WFInventoryRequest> inventoryRequestList;
	private WFShop inventoryRequestShop;
	private WFInventoryRequest inventoryRequestdetail;

	private WFInventoryRequest inventoryRequestForm;

	private List<WFInventoryRequest> inventoryAllList;

	private String iType;
	private String subType;
	
	private WFDelivery delivery;
	
	private WFInventoryRequest shopFilterInventory;

	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;
	
	
	private boolean[] saveFlag = new boolean[5];
	private String[] errorSaveMsg = new String[]{"营收没有保存", "支出没有保存", "外卖饿了吗没有保存", "外卖百度没有保存", "外卖没团没有保存"};
	
	private SelectItem[] shopTypes;

	public ShopBean() {
		super();
		shopService = ServiceFactory.getShopService();
		goodsService = ServiceFactory.getGoodsService();
		inventoryService = ServiceFactory.getInventoryService();
		shopIncoming = new ShopIncoming();
		iType = "1";
		shopTypes = new SelectItem[] {
				new SelectItem(ShopType.DIRECT_SALE.ordinal() + "", "直营店"),
				new SelectItem(ShopType.FRANCHISE_SALE.ordinal() + "", "加盟店") };
	}

	public List<WFShop> getShopList() {
		return shopService.getShopList();
	}

	public List<WFDamageReport> getDamageReport() {
		return shopService.queryDamageReport(0, 50);
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getNameRequire() {
		return nameRequire;
	}

	public void setNameRequire(String nameRequire) {
		this.nameRequire = nameRequire;
	}

	public long getReportId() {
		return reportId;
	}

	public String getiType() {
		return iType;
	}

	public void setiType(String iType) {
		this.iType = iType;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
		report = new WFDamageReport();
		report.setId(this.reportId);
		shopService.queryDamageReportDetail(report);
	}

	public Date getViewShopIncomingDate() {
		return viewShopIncomingDate;
	}

	public List<WFInventoryRequest> getInventoryAllList() {
		if (inventoryAllList == null) {
			inventoryAllList = shopService.queryInventoryRequestList(0, 30);
		}
		return inventoryAllList;
	}

	public void setViewShopIncomingDate(Date viewShopIncomingDate) {
		this.viewShopIncomingDate = viewShopIncomingDate;
		buildShopIncoming();
	}

	public void setViewShopIncomingId(long viewShopIncomingId) {
		this.viewShopIncomingId = viewShopIncomingId;
		buildShopIncoming();
	}

	private void buildShopIncoming() {
		WFShop wfs = shopService.getShop(viewShopIncomingId);
		if (viewShopIncomingDate == null) {
			viewShopIncomingDate = new Date();
		}
		// query last;
		WFIncoming wfi = shopService.queryShopIncoming(wfs,
				viewShopIncomingDate);
		if (wfi == null) {
			if (shopIncoming != null) {
				shopIncoming.reset();
			}
			return;
		}
		shopIncoming.setZls(wfi.getZls());
		shopIncoming.setCash(wfi.getCash());
		shopIncoming.setDazhong(wfi.getDazhong());
		shopIncoming.setDazhongaf(wfi.getDazhongaf());
		shopIncoming.setAli(wfi.getAli());
		shopIncoming.setWeixin(wfi.getWeixin());
		shopIncoming.setNuomi(wfi.getNuomi());
		shopIncoming.setOther(wfi.getOther());

		// group meituan
		GroupOnItem gMeituan = wfi.getGroupOnItem(GroupOnType.MEITUAN);
		shopIncoming.setGmCe(gMeituan.getCe());
		shopIncoming.setGmCount(gMeituan.getCount());
		shopIncoming.setGmDje(gMeituan.getDjq());
		shopIncoming.setGmGme(gMeituan.getGme());
		shopIncoming.setGmSjje(gMeituan.getSje());
		shopIncoming.setGmOther(gMeituan.getOther());

		GroupOnItem gDazhong = wfi.getGroupOnItem(GroupOnType.DAZHONG);
		shopIncoming.setGdCe(gDazhong.getCe());
		shopIncoming.setGdCount(gDazhong.getCount());
		shopIncoming.setGdDje(gDazhong.getDjq());
		shopIncoming.setGdGme(gDazhong.getGme());
		shopIncoming.setGdSjje(gDazhong.getSje());
		shopIncoming.setGdOther(gDazhong.getOther());

		GroupOnItem gNuomi = wfi.getGroupOnItem(GroupOnType.NUOMI);
		shopIncoming.setGnCe(gNuomi.getCe());
		shopIncoming.setGnCount(gNuomi.getCount());
		shopIncoming.setGnDje(gNuomi.getDjq());
		shopIncoming.setGnGme(gNuomi.getGme());
		shopIncoming.setGnSjje(gNuomi.getSje());
		shopIncoming.setGnOther(gNuomi.getOther());

		DeliveryItem dBaidu = wfi.getDeliveryItem(DeliveryType.BAIDU);
		shopIncoming.setDbDeliverFee(dBaidu.getDeliveryFee());
		shopIncoming.setDbOnlinePaymentSum(dBaidu.getOnlinePayment());
		shopIncoming.setDbPlatformRefund(dBaidu.getRefund());
		shopIncoming.setDbResturantRefund(dBaidu.getRefund1());
		shopIncoming.setDbServiceFee(dBaidu.getServiceFee());
		shopIncoming.setDbTotalSum(dBaidu.getIncoming());
		shopIncoming.setDbValid(dBaidu.getValid());

		DeliveryItem dDazhong = wfi.getDeliveryItem(DeliveryType.DAZHONG);
		shopIncoming.setDdDeliverFee(dDazhong.getDeliveryFee());
		shopIncoming.setDdOnlinePaymentSum(dDazhong.getOnlinePayment());
		shopIncoming.setDdPlatformRefund(dDazhong.getRefund());
		shopIncoming.setDdResturantRefund(dDazhong.getRefund1());
		shopIncoming.setDdServiceFee(dDazhong.getServiceFee());
		shopIncoming.setDdTotalSum(dDazhong.getIncoming());
		shopIncoming.setDdValid(dDazhong.getValid());

		DeliveryItem dElm = wfi.getDeliveryItem(DeliveryType.ELM);
		shopIncoming.setDeDeliverFee(dElm.getDeliveryFee());
		shopIncoming.setDeOnlinePaymentSum(dElm.getOnlinePayment());
		shopIncoming.setDePlatformRefund(dElm.getRefund());
		shopIncoming.setDeResturantRefund(dElm.getRefund1());
		shopIncoming.setDeServiceFee(dElm.getServiceFee());
		shopIncoming.setDeTotalSum(dElm.getIncoming());
		shopIncoming.setDeValid(dElm.getValid());

		DeliveryItem dkoubei = wfi.getDeliveryItem(DeliveryType.KOUBEI);
		shopIncoming.setDkDeliverFee(dkoubei.getDeliveryFee());
		shopIncoming.setDkOnlinePaymentSum(dkoubei.getOnlinePayment());
		shopIncoming.setDkPlatformRefund(dkoubei.getRefund());
		shopIncoming.setDkResturantRefund(dkoubei.getRefund1());
		shopIncoming.setDkServiceFee(dkoubei.getServiceFee());
		shopIncoming.setDkTotalSum(dkoubei.getIncoming());
		shopIncoming.setDkValid(dkoubei.getValid());

		DeliveryItem dMeituan = wfi.getDeliveryItem(DeliveryType.MEIGUAN);
		shopIncoming.setDmDeliverFee(dMeituan.getDeliveryFee());
		shopIncoming.setDmOnlinePaymentSum(dMeituan.getOnlinePayment());
		shopIncoming.setDmPlatformRefund(dMeituan.getRefund());
		shopIncoming.setDmResturantRefund(dMeituan.getRefund1());
		shopIncoming.setDmServiceFee(dMeituan.getServiceFee());
		shopIncoming.setDmTotalSum(dMeituan.getIncoming());
		shopIncoming.setDmValid(dMeituan.getValid());

		
		// query last;
		WFOperationCost cost = shopService.queryShopOperationCost(wfs,
						viewShopIncomingDate);
		if (cost != null) {
			shopIncoming.setCostRYTL(cost.getRytl());
			shopIncoming.setCostSB(cost.getSb());
			shopIncoming.setCostBC(cost.getBc());
			shopIncoming.setCostHSF(cost.getHsf());
			shopIncoming.setCostYL(cost.getYl());
			shopIncoming.setCostSF(cost.getSf());
			shopIncoming.setCostDF(cost.getDf());
			shopIncoming.setCostRQF(cost.getRqf());
			shopIncoming.setCostFF(cost.getFf());
			shopIncoming.setCostGZ(cost.getGz());
			shopIncoming.setCostRZ(cost.getRz());
			shopIncoming.setCostQT(cost.getQt());
		}
	}
	
	

	public void setViewShopInventoryRequestId(long viewShopInventoryRequestId) {
		inventoryRequestShop = shopService.getShop(viewShopInventoryRequestId);
		inventoryRequestList = shopService
				.queryInventoryRequestList(inventoryRequestShop);
	}

	public WFShop getInventoryRequestShop() {
		return inventoryRequestShop;
	}

	public ShopIncoming getShopIncoming() {
		return shopIncoming;
	}

	public void setShopIncoming(ShopIncoming shopIncoming) {
		this.shopIncoming = shopIncoming;
	}

	public WFDamageReport getReport() {
		return report;
	}

	public WFInventoryRequest getInventoryRequestdetail() {
		return inventoryRequestdetail;
	}

	public void setInventoryRequestDetailIdx(int idx) {
		inventoryRequestdetail = this.inventoryRequestList.get(idx);
		if (!inventoryRequestdetail.isLoadItem()) {
			shopService.queryInventoryRequestGoods(inventoryRequestdetail);
		}
	}

	public void setInventoryRequestDetailIdxFromAll(int idx) {
		inventoryRequestdetail = this.inventoryAllList.get(idx);
		if (!inventoryRequestdetail.isLoadItem()) {
			shopService.queryInventoryRequestGoods(inventoryRequestdetail);
		}
	}

	public void setInventoryRequestFormIdx(int inventoryRequestFormIdx) {
		inventoryRequestForm = this.inventoryRequestList
				.get(inventoryRequestFormIdx);
		if (!inventoryRequestForm.isLoadItem()) {
			shopService.queryInventoryRequestGoods(inventoryRequestForm);
		}
	}

	public void setInventoryRequestFormAllIdx(int inventoryRequestFormIdx) {
		inventoryRequestForm = this.inventoryAllList
				.get(inventoryRequestFormIdx);
		if (!inventoryRequestForm.isLoadItem()) {
			shopService.queryInventoryRequestGoods(inventoryRequestForm);
		}
	}

	public List<WFInventoryRequest> getInventoryRequestList() {
		return inventoryRequestList;
	}

	public void showForm() {
		File file = shopService.generateDeliveryForm(inventoryRequestForm);
		String absp = file.getAbsolutePath();
		String url = ServerConstants.getInstance().getDeliveryFromContext()
				+ absp.replace(ServerConstants.getInstance()
						.getDeliveryFormPath(), "");
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			externalContext.redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String gotoViewShopIncoming() {
		return "gotoviewshopincoming";
	}

	public String gotoViewShopInventoryRequestList() {
		return "gotoViewshopinventoryrequestlist";
	}

	public String gotoViewShopInventoryRequestDetail() {
		return "gotoViewshopinventoryrequestdetail";
	}

	public String gotoAddShop() {
		name = null;
		address = null;
		shopId = 0;
		errMsg = null;
		nameRequire = null;
		return "gotoaddshop";
	}

	public String gotoUpdateShop() {
		errMsg = null;
		WFShop wf = shopService.getShop(shopId);
		if (wf == null) {
			errMsg = "无法识别店铺ID";
			return "gotoupdateshopfailed";
		}
		name = wf.getName();
		address = wf.getAddress();
		shopType = wf.getType().ordinal();
		return "gotoupdateshop";
	}

	public String saveOrUpdateShop() {
		WFShop wf = null;
		if (name == null || name.isEmpty()) {
			errMsg = "创建店铺失败";
			nameRequire = "请输入店铺名称";
			return "failed";
		}
		if (shopId <= 0) {
			wf = new WFShop();
			wf.setName(name);
			wf.setAddress(address);
			wf.setType(this.shopType == ShopType.DIRECT_SALE.ordinal() ? ShopType.DIRECT_SALE : ShopType.FRANCHISE_SALE);
			shopService.addShop(wf);
		} else {
			wf = shopService.getShop(shopId);
			wf.setName(name);
			wf.setAddress(address);
			wf.setType(this.shopType == ShopType.DIRECT_SALE.ordinal() ? ShopType.DIRECT_SALE : ShopType.FRANCHISE_SALE);
			shopService.updateShop(wf);
		}

		return "shoplist";
	}
	
	
	public void deleteShop() {
		WFShop wfs = shopService.getShop(shopId);
		shopService.removeShop(wfs);
	}

	public String gotoDamageReportDetail() {
		return "gotodamagereportdetail";
	}

	public String reportIncoming() {
		errMsg = "";
		if (userBean.getUser() == null) {
			errMsg = "请重新登陆后，再登记营收!";
			return "reportincomingfailed";
		}
		WFShop wfs = ((WFManager) userBean.getUser()).getShop();
		if (wfs == null) {
			errMsg = "该用户，没有关联店铺， 请重新登陆";
			return "failed";
		}
		if (checkSaveFlag() == false) {
			return "failed";
		}
		
		WFIncoming wf = shopService.queryShopIncoming(wfs, new Date());
		if (wf != null) {
			errMsg = "今天已经提交过营收报表， 无法重复提交";
			return "failed";
		}
		shopService.reportIncoming(((WFManager) userBean.getUser()).getShop(),
				this.shopIncoming.buildToWFIncoming(),
				this.shopIncoming.buildOperationCost(),
				(WFManager) userBean.getUser());
		return "reportincomingsuccess";
	}
	
	
	public void updateIncomingAndCost() {
		WFShop shop = shopService.getShop(viewShopIncomingId);
		shopService.updateShopIncomingAndOperationCost(shop,
				this.shopIncoming.buildToWFIncoming(),
				this.shopIncoming.buildOperationCost(), viewShopIncomingDate);
	}

	public String reportDamage() {
		report = null;
		// if (userBean.getUser().getRole() != Role.MANAGER) {
		// errMsg ="只有店长可以创建补货申请";
		// return "createinventoryrequestfailed";
		// }
		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] goods_id = map.get("goodsname_id");
		String[] count = map.get("goodscount");
		if (goods_id == null && count == null) {
			errMsg = "请输入要报损的产品信息";
			return "damageReportfailed";
		}
		if (report != null) {
			report.clearItems();
		} else {
			report = new WFDamageReport();
		}
		boolean ma = false;
		for (int i = 0; i < goods_id.length; i++) {
			if (goods_id[i] == null || goods_id[i].isEmpty()) {
				errMsg = "请输入要报损的产品信息";
				return "damageReportfailed";
			}
			WFGoods g = goodsService.getGoods(Long.parseLong(goods_id[i]));

			if (g == null) {
				errMsg = "没有找到相关产品： " + goods_id[i];
				return "damageReportfailed";
			}

			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", count[i]);
			if (!ma) {
				errMsg = "报损数量应为数字";
				return "damageReportfailed";
			}

			report.addDamageItem(g, Float.parseFloat(count[i]), false);
		}
		report.setDatetime(new Date());
		// inventoryRequest.setShop(((WFManager)userBean.getUser()).getShop());
		if (userBean.getUser() == null || userBean == null
				|| userBean.getUser().getRole() != Role.MANAGER) {
			errMsg = "非法用户";
			return "damageReportfailed";
		}
		shopService.reportDamage(report,
				((WFManager) userBean.getUser()).getShop(), userBean.getUser());

		reportResultType = "damage";
		return "damageReportsuccess";
	}
	
	
	
	public String reportShopInventory() {
		shopInventoryReport = null;
		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] goods_id = map.get("igoodsname_id");
		String[] count = map.get("igoodscount");
		if (goods_id == null && count == null) {
			errMsg = "请输入要盘点的菜品信息";
			return "shopInventoryReportfailed";
		}
		if (shopInventoryReport != null) {
			shopInventoryReport.clearItems();
		} else {
			shopInventoryReport = new WFShopInventory();
		}
		boolean ma = false;
		for (int i = 0; i < goods_id.length; i++) {
			if (goods_id[i] == null || goods_id[i].isEmpty()) {
				errMsg = "请输入要盘点的产品信息";
				return "shopInventoryReportfailed";
			}
			WFGoods g = goodsService.getGoods(Long.parseLong(goods_id[i]));

			if (g == null) {
				errMsg = "没有找到相关产品： " + goods_id[i];
				return "shopInventoryReportfailed";
			}

			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", count[i]);
			if (!ma) {
				errMsg = "盘点数量应为数字";
				return "shopInventoryReportfailed";
			}

			shopInventoryReport.addItem(g, Float.parseFloat(count[i]), false);
		}
		shopInventoryReport.setDatetime(new Date());
		// inventoryRequest.setShop(((WFManager)userBean.getUser()).getShop());
		if (userBean.getUser() == null || userBean == null
				|| userBean.getUser().getRole() != Role.MANAGER) {
			errMsg = "非法用户";
			return "shopInventoryReportfailed";
		}
		shopService.reportShopInventory(shopInventoryReport,
				((WFManager) userBean.getUser()).getShop(), userBean.getUser());

		reportResultType = "shopInventoryReport";
		return "shopInventoryReportsuccess";
	}

	public String requestInventory() {
		inventoryRequest = null;
		// if (userBean.getUser().getRole() != Role.MANAGER) {
		// errMsg ="只有店长可以创建补货申请";
		// return "createinventoryrequestfailed";
		// }
		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] goods_id = map.get("goodsname_id");
		String[] count = map.get("goodscount");
		if (goods_id == null && count == null) {
			errMsg = "请输入产品信息";
			return "inventoryrequestfailed";
		}
		if (inventoryRequest != null) {
			inventoryRequest.clearItems();
		} else {
			inventoryRequest = new WFInventoryRequest();
		}
		
		boolean errorFlag = false;
		boolean ma = false;
		errMsg = "";
		for (int i = 0; i < goods_id.length; i++) {
			if (goods_id[i] == null || goods_id[i].isEmpty()) {
				errMsg += "请输入产品信息"+"\n";
				errorFlag = true;
				continue;
			}
			WFGoods g = goodsService.getGoods(Long.parseLong(goods_id[i]));

			if (g == null) {
				errMsg += "没有找到相关产品： " + goods_id[i]+"\n";
				errorFlag = true;
				continue;
			}

			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", count[i]);
			if (!ma) {
				errMsg += g.getName()+"配货数量应为数字"+"\n";
				errorFlag = true;
				continue;
			}

			inventoryRequest.addInventoryItem(g, Float.parseFloat(count[i]),
					false);
		}
		if (errorFlag) {
			return "inventoryrequestfailed";
		}
		
		inventoryRequest.setDatetime(new Date());
		// inventoryRequest.setShop(((WFManager)userBean.getUser()).getShop());
		if (userBean.getUser() == null || userBean == null
				|| userBean.getUser().getRole() != Role.MANAGER) {
			errMsg = "非法用户";
			return "inventoryrequestfailed";
		}
		
		
		shopService.requestInventory(inventoryRequest,
				((WFManager) userBean.getUser()).getShop(), userBean.getUser());

		return "inventoryrequestsuccess";

	}
	
	
	public String prepareDelivery() {
		 delivery = new WFDelivery();
		 delivery.setShop(inventoryRequestdetail.getShop());
		 delivery.setInventoryRequestId(inventoryRequestdetail.getId());
		 delivery.setId(inventoryRequestdetail.getId());
		 List<Item> list =  inventoryRequestdetail.getItemList();
		 Map<WFGoods, Float> stockMap = this.inventoryService.queryCurrentStockMap();
		 Float stock;
		 for (WFInventoryRequest.Item wri : list) {
			 delivery.addItem(wri.getGoods(), wri.getCount(), wri.getCount(), wri.getGoods().getPrice(), false);
			 stock = stockMap.get(wri.getGoods());
			 inventoryRequestdetail.updateInventoryItem(wri.getGoods(), wri.getCount(),  wri.getGoods().getPrice(), stock == null ? 0 : stock);
		 }
		 
		 //Query from vendor and confirm real delivery counts like vegetable vendor
		 List<WFInventory> ilist = inventoryService.querySubInventoryRequest(inventoryRequestdetail.getId());
		 for (WFInventory wfi : ilist) {
			 List<WFInventory.Item>  itemList = wfi.getItemList();
			 if (itemList == null) {
				 continue;
			 }
			 for (WFInventory.Item  wi : itemList) {
				 delivery.updateItem(wi.getGoods(), wi.getCount(), wi.getPrice());
				 inventoryRequestdetail.updateInventoryItem(wi.getGoods(), wi.getCount(), wi.getPrice(), wi.getCount());
			 }
		 }
		return "perparedelivery";
	}
	
	
	public String generateDeliveryPaper() {

		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] goods_id = map.get("g_id");
		String[] realCount = map.get("g_count");
		
		if (goods_id == null ) {
			errMsg = "请输选择配送菜品";
			return "deliveriedFailed";
		}
		
		if (realCount == null) {
			errMsg = "配送菜品数量应为数字";
			return "deliveriedFailed";
		}
		
	
		
		boolean ma = false;
		for (int i = 0; i < goods_id.length; i++) {
			if (goods_id[i] == null || goods_id[i].isEmpty()) {
				errMsg = "请输入产品信息";
				return "deliveriedFailed";
			}
			String[] str = goods_id[i].split("_");
			WFGoods g = goodsService.getGoods(Long.parseLong(str[0]));

			if (g == null) {
				errMsg = "没有找到相关产品： " + goods_id[i];
				return "prepareDeliveryFailed";
			}

			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", realCount[Integer.parseInt(str[1])]);
			if (!ma) {
				errMsg = g.getName()+"配送数量应为数字";
				return "deliveriedFailed";
			}

			
			delivery.updateItem(g,  Float.parseFloat(realCount[Integer.parseInt(str[1])]));
		}
		delivery.setDatetime(new Date());
		//TODO update item according to selected checkbox
		
		Result ret = shopService.prepareDelivery(delivery, userBean.getUser());
		if (ret == Result.ERR_OUT_OF_STOCK) {
			errMsg = "库存不足，无法处理相关配送";
			return "deliveriedFailed";
		}
		
		File file = shopService.generateDeliveryForm(delivery);
		String absp = file.getAbsolutePath();
		String url = ServerConstants.getInstance().getDeliveryFromContext()
				+ absp.replace(ServerConstants.getInstance()
						.getDeliveryFormPath(), "");
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			externalContext.redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public WFDelivery getDelivery() {
		return delivery;
	}
	
	private boolean checkSaveFlag() {
		boolean ret = true;
		for (int i = 0; i < saveFlag.length; i++) {
			if (saveFlag[i] == false) {
				this.errMsg += errorSaveMsg[i]+"\n";
				ret = false;
			}
		}
		return ret;
	}
	
	public void updateFlag() {
		if (this.iType.equals("1")) {
			this.saveFlag[0] = true;
		} else if  (this.iType.equals("2")) {
			this.saveFlag[1] = true;
		} else if  (this.iType.equals("4")) {
			if (this.subType.equals("1")) {
				this.saveFlag[2] = true;
			} else if (this.subType.equals("2")) {
				this.saveFlag[3] = true;
			} else if (this.subType.equals("4")) {
				this.saveFlag[4] = true;
			}
		}
	}
	
	
	public void removeInventoryRequest(long id) {
		for (WFInventoryRequest wfr : inventoryAllList) {
			if (wfr.getId() == id) {
				inventoryAllList.remove(wfr);
				break;
			}
		}
		inventoryService.removeWFIneventoryRequest(id);
	}

	public Date getViewShopInventoryDate() {
		return viewShopInventoryDate;
	}

	public void setViewShopInventoryDate(Date viewShopInventoryDate) {
		this.viewShopInventoryDate = viewShopInventoryDate;
	}

	public WFInventoryRequest getShopFilterInventory() {
		return shopFilterInventory;
	}
	
	public void filterInventory() {
		if (userBean.getUser().getRole() == Role.MANAGER) {
			shopFilterInventory = this.inventoryService.queryShopWFInventoryRequest(((WFManager)(userBean.getUser())).getShop(), viewShopInventoryDate);
		}
	}
	
	
	public void clearFilter() {
		shopFilterInventory = null;
	}

	public SelectItem[] getShopTypes() {
		return shopTypes;
	}

	public void setShopTypes(SelectItem[] shopTypes) {
		this.shopTypes = shopTypes;
	}

	public int getShopType() {
		return shopType;
	}

	public void setShopType(int shopType) {
		this.shopType = shopType;
	}

	public WFShopInventory getShopInventoryReport() {
		return shopInventoryReport;
	}

	public void setShopInventoryReport(WFShopInventory shopInventoryReport) {
		this.shopInventoryReport = shopInventoryReport;
	}

	public String getReportResultType() {
		return reportResultType;
	}

	public void setReportResultType(String reportResultType) {
		this.reportResultType = reportResultType;
	}
	

	
}
