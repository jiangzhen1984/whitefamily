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

import com.whitefamily.ServerConstants;
import com.whitefamily.po.customer.Role;
import com.whitefamily.po.incoming.DeliveryType;
import com.whitefamily.po.incoming.GroupOnType;
import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.IShopService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFDamageReport;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFIncoming.DeliveryItem;
import com.whitefamily.service.vo.WFIncoming.GroupOnItem;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFManager;
import com.whitefamily.service.vo.WFShop;

@ManagedBean(name = "shopBean", eager = false)
@SessionScoped
public class ShopBean {

	private String name;
	private String address;
	private long shopId;
	private String errMsg;
	private String nameRequire;

	IShopService shopService;
	IGoodsService goodsService;

	private long reportId;
	private WFDamageReport report;
	private WFInventoryRequest inventoryRequest;
	private ShopIncoming shopIncoming;
	private long viewShopIncomingId;
	private Date viewShopIncomingDate;

	private long viewShopInventoryRequestId;
	private List<WFInventoryRequest> inventoryRequestList;
	private WFShop inventoryRequestShop;
	private WFInventoryRequest inventoryRequestdetail;

	private WFInventoryRequest inventoryRequestForm;

	private List<WFInventoryRequest> inventoryAllList;

	private String iType;
	private String subType;

	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;

	public ShopBean() {
		super();
		shopService = ServiceFactory.getShopService();
		goodsService = ServiceFactory.getGoodsService();
		shopIncoming = new ShopIncoming();
		iType = "1";
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
		shopIncoming.setCash(wfi.getCash());
		shopIncoming.setDazhong(wfi.getDazhong());
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

	}

	public void setViewShopInventoryRequestId(long viewShopInventoryRequestId) {
		this.viewShopInventoryRequestId = viewShopInventoryRequestId;
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
			shopService.addShop(wf);
		} else {
			wf = shopService.getShop(shopId);
			wf.setName(name);
			wf.setAddress(address);
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
		errMsg = null;
		if (userBean.getUser() == null) {
			errMsg = "请重新登陆后，再登记营收!";
			return "reportincomingfailed";
		}
		shopService.reportIncoming(((WFManager) userBean.getUser()).getShop(),
				this.shopIncoming.buildToWFIncoming(),
				(WFManager) userBean.getUser());
		return "reportincomingsuccess";
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

		return "damageReportsuccess";

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
		boolean ma = false;
		for (int i = 0; i < goods_id.length; i++) {
			if (goods_id[i] == null || goods_id[i].isEmpty()) {
				errMsg = "请输入产品信息";
				return "inventoryrequestfailed";
			}
			WFGoods g = goodsService.getGoods(Long.parseLong(goods_id[i]));

			if (g == null) {
				errMsg = "没有找到相关产品： " + goods_id[i];
				return "inventoryrequestfailed";
			}

			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", count[i]);
			if (!ma) {
				errMsg = "报损数量应为数字";
				return "inventoryrequestfailed";
			}

			inventoryRequest.addInventoryItem(g, Float.parseFloat(count[i]),
					false);
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

}
