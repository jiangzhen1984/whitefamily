package com.whitefamily.web.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.whitefamily.po.InventoryType;
import com.whitefamily.po.artifact.ArtifactStaffType;
import com.whitefamily.service.IGoodsService;
import com.whitefamily.service.IInventoryService;
import com.whitefamily.service.IShopService;
import com.whitefamily.service.Result;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFArtifact;
import com.whitefamily.service.vo.WFArtifact.StaffGoods;
import com.whitefamily.service.vo.WFBrand;
import com.whitefamily.service.vo.WFDelivery;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFInventory;
import com.whitefamily.service.vo.WFVendor;

@ManagedBean(name = "artifactBean", eager = false)
@SessionScoped
public class ArtifactBean {
	
	private IShopService shopService;

	private IInventoryService inventoryService;
	
	private IGoodsService goodsService;
	
	private WFArtifact artifact;
	
	private List<WFArtifact> arsList;
	
	private String errMsg;
	
	public ArtifactBean() {
		shopService = ServiceFactory.getShopService();
		goodsService = ServiceFactory.getGoodsService();
		inventoryService = ServiceFactory.getInventoryService();
	}
	
	
	public String createArtifact() {
		
		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		String[] input_goods_id = map.get("asg_id");
		String[] input_goods_unit = map.get("asgunit");
		String[] output_goods_id = map.get("atg_id");
		String[] output_goods_unit = map.get("atgunit");
		String[] output_goods_desc = map.get("atgdesc");
		//String[] waste_goods_id = map.get("waste_g");
		String[] styles = map.get("style");
		String[] minals = map.get("minal");
		
		if (input_goods_id == null || input_goods_id.length <= 0) {
			errMsg = "请选择加工原材料";
			return "createArtifactFailed";
		}
		
		if (input_goods_unit == null || input_goods_unit.length <= 0) {
			errMsg = "请选择加工原材料生产单元";
			return "createArtifactFailed";
		}
		
		if (input_goods_unit.length != input_goods_id.length) {
			errMsg = "原材料生产单元和原产材料数量不匹配";
			return "createArtifactFailed";
		}
		
		if (output_goods_id == null || output_goods_id.length <= 0) {
			errMsg = "请选择加工产品";
			return "createArtifactFailed";
		}
		
		if (output_goods_unit == null || output_goods_unit.length <= 0) {
			errMsg = "请选择加工产品单元";
			return "createArtifactFailed";
		}
		
		if (output_goods_unit.length != output_goods_id.length) {
			errMsg = "生产单元和加工产品数量不匹配";
			return "createArtifactFailed";
		}
		
		
		WFGoods[] inputg = new WFGoods[input_goods_id.length];
		boolean flag = false;
		WFGoods wfg = null;
		for (int i = 0; i < input_goods_id.length; i++) {
			String sid =  input_goods_id[i];
			String unit =  input_goods_unit[i];
			flag = Pattern.matches("([0-9])+", sid);
			if (!flag) {
				errMsg = "原材料产品无法匹配";
				return "createArtifactFailed";
			}
			wfg = goodsService.getGoods(Long.parseLong(sid));
			if (wfg == null) {
				errMsg = "原材料产品没有绑定";
				return "createArtifactFailed";
			}
			inputg[i] = wfg;
			flag = Pattern.matches("([0-9]+(\\.[0-9]+)?)+", unit);
			if (!flag) {
				errMsg = "原材料单元应为数字";
				return "createArtifactFailed";
			}
			
		}
		
		WFGoods[] outputg = new WFGoods[output_goods_id.length];
		for (int i = 0; i < output_goods_id.length; i++) {
			String sid =  output_goods_id[i];
			String unit =  output_goods_unit[i];
			flag = Pattern.matches("([0-9])+", sid);
			if (!flag) {
				errMsg = "加工产品无法匹配";
				return "createArtifactFailed";
			}
			wfg = goodsService.getGoods(Long.parseLong(sid));
			if (wfg == null) {
				errMsg = "加工产品没有绑定";
				return "createArtifactFailed";
			}
			outputg[i] = wfg;
			flag = Pattern.matches("([0-9]+(\\.[0-9]+)?)+", unit);
			if (!flag) {
				errMsg = "加工产品单元应为数字";
				return "createArtifactFailed";
			}
		}
		
		artifact = new WFArtifact();
		for (int i = 0; i < inputg.length; i++) {
			artifact.addWFGoods(ArtifactStaffType.INPUT, inputg[i], Float.parseFloat(input_goods_unit[i]), null, null);
		}
		
		for (int i = 0; i < outputg.length; i++) {
			artifact.addWFGoods(ArtifactStaffType.OUTPUT, outputg[i], Float.parseFloat(output_goods_unit[i]), minals[i], styles[i]);
		}
		
		if (output_goods_desc != null && output_goods_desc.length > 0) {
			artifact.setDesc(output_goods_desc[0]);
		}
		goodsService.createWFArtifact(artifact);
		errMsg = null;
		return "success";
	}
	
	
	
	public String artifactInventoryUpdate() {

		Map<String, String[]> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap();
		
		String[] ar_id = map.get("ar_id");
		if (ar_id == null || ar_id.length <= 0) {
			errMsg = "请选择加工产品";
			return "artifactInventoryUpdateFailed";
		}
		boolean flag = false;
		for (String str : ar_id) {
			flag = Pattern.matches("([0-9]+(\\.[0-9]+)?)+", str);
			if (flag == false) {
				errMsg = "ID绑定失败";
				return "artifactInventoryUpdateFailed";
			}
		}
		
		arsList = new ArrayList<WFArtifact>(ar_id.length);
		WFArtifact wfa = null;
		for (String str : ar_id) {
			wfa = goodsService.loadArtifact(Long.parseLong(str));
			if (wfa != null) {
				arsList.add(wfa);
			}
		}
		
		return "inventoryupdate";
	}

	
	
	public String saveArtifactInventory() {

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
			return "saveinventoryfailed";
		}
		
		if (goods_id.length != brands.length  || goods_id.length != vendors.length) {
			errMsg ="请输入要更新的库存的产品信息";
			return "saveinventoryfailed";
		}
		
	
		WFInventory inventory =  new WFInventory();
		boolean ma = false;
		for (int i = 0; i < goods_id.length; i++) {
			if (goods_id[i] == null || goods_id[i].isEmpty()) {
				errMsg ="请输入要更新的库存的产品信息";
				return "saveinventoryfailed";
			}
			WFGoods g = goodsService.getGoods(Long.parseLong(goods_id[i]));
			
			if (g == null) {
				errMsg ="没有找到相关产品： " + goods_id[i];
				return "saveinventoryfailed";
			}
			
			if (brands[i] == null || brands[i].isEmpty()) {
				errMsg ="产品品牌必须输入";
				return "saveinventoryfailed";
			}
			
			if (vendors[i] == null || vendors[i].isEmpty()) {
				errMsg ="品牌供应商必须输入";
				return "saveinventoryfailed";
			}
			
			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", count[i]);
			if (!ma) {
				errMsg ="产品数量应为数字";
				return "saveinventoryfailed";
			}
			
			ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", prs[i]);
			if (!ma) {
				errMsg ="产品价格应为数字";
				return "saveinventoryfailed";
			}
			
			if (rate[i] != null) {
				ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", rate[i]);
				if (!ma) {
					errMsg ="出货价格比例应为数字";
					return "saveinventoryfailed";
				}
			}
			
			if (rate1[i] != null) {
				ma = Pattern.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+", rate1[i]);
				if (!ma) {
					errMsg ="出货加盟商价格比例应为数字";
					return "saveinventoryfailed";
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
		
		WFDelivery wfd = new WFDelivery();
		WFGoods wfg = null;
		for (WFArtifact wf : arsList) {
			for(StaffGoods sg : wf.getStaffGoods()) {
				wfg = goodsService.getGoods(sg.wfg.getId());
				wfd.addItem(wfg, sg.getUnit(), sg.getUnit(), wfg.getPrice(), false);
			}
		}
		Result ret = shopService.handleInternalDelivery(wfd, null);
		if (ret != Result.SUCCESS) {
			errMsg ="处理提货失败,库存不足";
			return "saveinventoryfailed";
		}
		inventoryService.createInventory(inventory);
		
		return "inventorylist";
	}
	

	public WFArtifact getArtifact() {
		return artifact;
	}


	public void setArtifact(WFArtifact artifact) {
		this.artifact = artifact;
	}


	public String getErrMsg() {
		return errMsg;
	}


	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


	public List<WFArtifact> getArsList() {
		return arsList;
	}


	public void setArsList(List<WFArtifact> arsList) {
		this.arsList = arsList;
	}
	
	
	
}
