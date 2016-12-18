package com.whitefamily.service.vo;

import java.util.ArrayList;
import java.util.List;

import com.whitefamily.po.artifact.ArtifactProduct;
import com.whitefamily.po.artifact.ArtifactStaffType;

public class WFArtifact extends ArtifactProduct {

	
	private List<StaffGoods> targetGoods;
	
	private List<StaffGoods> staffGoods;
	
	private List<StaffGoods> staffCostGoods;
	
	private String desc;
	
	
	public void addWFGoods(ArtifactStaffType type, WFGoods goods, float unit, String minalProduceUnit, String style) {
		List<StaffGoods> tg = null;
		switch (type) {
		case OUTPUT:
			if (targetGoods == null) {
				targetGoods = new ArrayList<StaffGoods>();
			}
			tg = targetGoods;
			break;
		case INPUT:
			if (staffGoods == null) {
				staffGoods = new ArrayList<StaffGoods>();
			}
			tg = staffGoods;
			break;
		case PRODUCED:
			if (staffCostGoods == null) {
				staffCostGoods = new ArrayList<StaffGoods>();
			}
			tg = staffCostGoods;
			break;
		default:
			break;
		}
		
		tg.add(new StaffGoods(goods, unit, style, minalProduceUnit));
	}
	
	
	public class StaffGoods {
		public WFGoods wfg;
		public float unit;
		public String style;
		public String minialProduceUnit;
		public String desc;
		public StaffGoods(WFGoods wfg, float unit) {
			super();
			this.wfg = wfg;
			this.unit = unit;
		}
		public StaffGoods(WFGoods wfg, float unit, String style, String minialProduceUnit) {
			super();
			this.wfg = wfg;
			this.unit = unit;
			this.style = style;
			this.minialProduceUnit = minialProduceUnit;
		}
		public WFGoods getWfg() {
			return wfg;
		}
		public void setWfg(WFGoods wfg) {
			this.wfg = wfg;
		}
		public float getUnit() {
			return unit;
		}
		public void setUnit(float unit) {
			this.unit = unit;
		}
		public String getStyle() {
			return style;
		}
		public void setStyle(String style) {
			this.style = style;
		}
		public String getMinialProduceUnit() {
			return minialProduceUnit;
		}
		public void setMinialProduceUnit(String minialProduceUnit) {
			this.minialProduceUnit = minialProduceUnit;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
		
		
		
		
	}
	
	
	public List<StaffGoods> getStaffGoods(ArtifactStaffType type) {
		List<StaffGoods> tg = null;
		switch (type) {
		case OUTPUT:
			if (targetGoods == null) {
				targetGoods = new ArrayList<StaffGoods>();
			}
			tg = targetGoods;
			break;
		case INPUT:
			if (staffGoods == null) {
				staffGoods = new ArrayList<StaffGoods>();
			}
			tg = staffGoods;
			break;
		case PRODUCED:
			if (staffCostGoods == null) {
				staffCostGoods = new ArrayList<StaffGoods>();
			}
			tg = staffCostGoods;
			break;
		}
		return tg;
	}


	public List<StaffGoods> getTargetGoods() {
		return targetGoods;
	}


	public void setTargetGoods(List<StaffGoods> targetGoods) {
		this.targetGoods = targetGoods;
	}


	public List<StaffGoods> getStaffGoods() {
		return staffGoods;
	}


	public void setStaffGoods(List<StaffGoods> staffGoods) {
		this.staffGoods = staffGoods;
	}


	public List<StaffGoods> getStaffCostGoods() {
		return staffCostGoods;
	}


	public void setStaffCostGoods(List<StaffGoods> staffCostGoods) {
		this.staffCostGoods = staffCostGoods;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
	
	
}
