package com.whitefamily.web.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whitefamily.po.incoming.DeliveryType;
import com.whitefamily.po.incoming.GroupOnType;
import com.whitefamily.service.vo.WFCategory;
import com.whitefamily.service.vo.WFGoods;
import com.whitefamily.service.vo.WFIncoming;
import com.whitefamily.service.vo.WFOperationCost;
import com.whitefamily.service.vo.WFShopInventoryCost;

public class ShopIncoming {

	
	private float zls;
	private float cash;
	private float groupon;
	private float delivery;
	private float nuomi;
	private float dazhong;
	private float other;
	private float ali;
	private float weixin;
	private float dazhongaf;
	private float nuomiaf;

	// group item dazhong
	private int gdCount;
	private float gdDje;
	private float gdGme;
	private float gdCe;
	private float gdSjje;
	private float gdOther;

	// group item meituan
	private int gmCount;
	private float gmDje;
	private float gmGme;
	private float gmCe;
	private float gmSjje;
	private float gmOther;

	// group item nuomi
	private int gnCount;
	private float gnDje;
	private float gnGme;
	private float gnCe;
	private float gnSjje;
	private float gnOther;

	// delivery item elm
	private float deTotalSum;
	private float deOnlinePaymentSum;
	private float dePlatformRefund;
	private float deResturantRefund;
	private float deServiceFee;
	private float deDeliverFee;
	private int deValid;

	// delivery item baidu
	private float dbTotalSum;
	private float dbOnlinePaymentSum;
	private float dbPlatformRefund;
	private float dbResturantRefund;
	private float dbServiceFee;
	private float dbDeliverFee;
	private int dbValid;

	// delivery item meituan
	private float dmTotalSum;
	private float dmOnlinePaymentSum;
	private float dmPlatformRefund;
	private float dmResturantRefund;
	private float dmServiceFee;
	private float dmDeliverFee;
	private int dmValid;

	// delivery item dazhong
	private float ddTotalSum;
	private float ddOnlinePaymentSum;
	private float ddPlatformRefund;
	private float ddResturantRefund;
	private float ddServiceFee;
	private float ddDeliverFee;
	private int ddValid;

	// delivery item koubei
	private float dkTotalSum;
	private float dkOnlinePaymentSum;
	private float dkPlatformRefund;
	private float dkResturantRefund;
	private float dkServiceFee;
	private float dkDeliverFee;
	private int dkValid;
	
	//Shop operation cost
	//日用调料
	private float costRYTL;
	//烧饼
	private float costSB;
	//补菜
	private float costBC;
	//伙食费
	private float costHSF;
	//饮料
	private float costYL;
	//水费
	private float costSF;
	//电费
	private float costDF;
	//燃气费
	private float costRQF;
	//房费
	private float costFF;
	//工资
	private float costGZ;
	//日杂
	private float costRZ;
	//其他
	private float costQT;
	
	
	private Map<WFCategory, WFShopInventoryCost> inventoryCostMap;


	public float getZls() {
		return zls;
	}

	public void setZls(float zls) {
		this.zls = zls;
	}

	public float getAli() {
		return ali;
	}

	public void setAli(float ali) {
		this.ali = ali;
	}

	public float getWeixin() {
		return weixin;
	}

	public void setWeixin(float weixin) {
		this.weixin = weixin;
	}

	public float getCash() {
		return cash;
	}

	public void setCash(float cash) {
		this.cash = cash;
	}

	public float getGroupon() {
		return groupon;
	}

	public void setGroupon(float groupon) {
		this.groupon = groupon;
	}

	public float getDelivery() {
		return delivery;
	}

	public void setDelivery(float delivery) {
		this.delivery = delivery;
	}

	public float getNuomi() {
		return nuomi;
	}

	public void setNuomi(float nuomi) {
		this.nuomi = nuomi;
	}

	public float getDazhong() {
		return dazhong;
	}

	public void setDazhong(float dazhong) {
		this.dazhong = dazhong;
	}

	public float getOther() {
		return other;
	}

	public void setOther(float other) {
		this.other = other;
	}

	public int getGdCount() {
		return gdCount;
	}

	public void setGdCount(int gdCount) {
		this.gdCount = gdCount;
	}

	public float getGdDje() {
		return gdDje;
	}

	public void setGdDje(float gdDje) {
		this.gdDje = gdDje;
	}

	public float getGdGme() {
		return gdGme;
	}

	public void setGdGme(float gdGme) {
		this.gdGme = gdGme;
	}

	public float getGdCe() {
		return gdCe;
	}

	public void setGdCe(float gdCe) {
		this.gdCe = gdCe;
	}

	public float getGdSjje() {
		return gdSjje;
	}

	public void setGdSjje(float gdSjje) {
		this.gdSjje = gdSjje;
	}

	public float getGdOther() {
		return gdOther;
	}

	public void setGdOther(float gdOther) {
		this.gdOther = gdOther;
	}

	public int getGmCount() {
		return gmCount;
	}

	public void setGmCount(int gmCount) {
		this.gmCount = gmCount;
	}

	public float getGmDje() {
		return gmDje;
	}

	public void setGmDje(float gmDje) {
		this.gmDje = gmDje;
	}

	public float getGmGme() {
		return gmGme;
	}

	public void setGmGme(float gmGme) {
		this.gmGme = gmGme;
	}

	public float getGmCe() {
		return gmCe;
	}

	public void setGmCe(float gmCe) {
		this.gmCe = gmCe;
	}

	public float getGmSjje() {
		return gmSjje;
	}

	public void setGmSjje(float gmSjje) {
		this.gmSjje = gmSjje;
	}

	public float getGmOther() {
		return gmOther;
	}

	public void setGmOther(float gmOther) {
		this.gmOther = gmOther;
	}

	public int getGnCount() {
		return gnCount;
	}

	public void setGnCount(int gnCount) {
		this.gnCount = gnCount;
	}

	public float getGnDje() {
		return gnDje;
	}

	public void setGnDje(float gnDje) {
		this.gnDje = gnDje;
	}

	public float getGnGme() {
		return gnGme;
	}

	public void setGnGme(float gnGme) {
		this.gnGme = gnGme;
	}

	public float getGnCe() {
		return gnCe;
	}

	public void setGnCe(float gnCe) {
		this.gnCe = gnCe;
	}

	public float getGnSjje() {
		return gnSjje;
	}

	public void setGnSjje(float gnSjje) {
		this.gnSjje = gnSjje;
	}

	public float getGnOther() {
		return gnOther;
	}

	public void setGnOther(float gnOther) {
		this.gnOther = gnOther;
	}

	public float getDeTotalSum() {
		return deTotalSum;
	}

	public void setDeTotalSum(float deTotalSum) {
		this.deTotalSum = deTotalSum;
	}

	public float getDeOnlinePaymentSum() {
		return deOnlinePaymentSum;
	}

	public void setDeOnlinePaymentSum(float deOnlinePaymentSum) {
		this.deOnlinePaymentSum = deOnlinePaymentSum;
	}

	public float getDePlatformRefund() {
		return dePlatformRefund;
	}

	public void setDePlatformRefund(float dePlatformRefund) {
		this.dePlatformRefund = dePlatformRefund;
	}

	public float getDeResturantRefund() {
		return deResturantRefund;
	}

	public void setDeResturantRefund(float deResturantRefund) {
		this.deResturantRefund = deResturantRefund;
	}

	public float getDeServiceFee() {
		return deServiceFee;
	}

	public void setDeServiceFee(float deServiceFee) {
		this.deServiceFee = deServiceFee;
	}

	public float getDeDeliverFee() {
		return deDeliverFee;
	}

	public void setDeDeliverFee(float deDeliverFee) {
		this.deDeliverFee = deDeliverFee;
	}

	public float getDbTotalSum() {
		return dbTotalSum;
	}

	public void setDbTotalSum(float dbTotalSum) {
		this.dbTotalSum = dbTotalSum;
	}

	public float getDbOnlinePaymentSum() {
		return dbOnlinePaymentSum;
	}

	public void setDbOnlinePaymentSum(float dbOnlinePaymentSum) {
		this.dbOnlinePaymentSum = dbOnlinePaymentSum;
	}

	public float getDbPlatformRefund() {
		return dbPlatformRefund;
	}

	public void setDbPlatformRefund(float dbPlatformRefund) {
		this.dbPlatformRefund = dbPlatformRefund;
	}

	public float getDbResturantRefund() {
		return dbResturantRefund;
	}

	public void setDbResturantRefund(float dbResturantRefund) {
		this.dbResturantRefund = dbResturantRefund;
	}

	public float getDbServiceFee() {
		return dbServiceFee;
	}

	public void setDbServiceFee(float dbServiceFee) {
		this.dbServiceFee = dbServiceFee;
	}

	public float getDbDeliverFee() {
		return dbDeliverFee;
	}

	public void setDbDeliverFee(float dbDeliverFee) {
		this.dbDeliverFee = dbDeliverFee;
	}

	public float getDmTotalSum() {
		return dmTotalSum;
	}

	public void setDmTotalSum(float dmTotalSum) {
		this.dmTotalSum = dmTotalSum;
	}

	public float getDmOnlinePaymentSum() {
		return dmOnlinePaymentSum;
	}

	public void setDmOnlinePaymentSum(float dmOnlinePaymentSum) {
		this.dmOnlinePaymentSum = dmOnlinePaymentSum;
	}

	public float getDmPlatformRefund() {
		return dmPlatformRefund;
	}

	public void setDmPlatformRefund(float dmPlatformRefund) {
		this.dmPlatformRefund = dmPlatformRefund;
	}

	public float getDmResturantRefund() {
		return dmResturantRefund;
	}

	public void setDmResturantRefund(float dmResturantRefund) {
		this.dmResturantRefund = dmResturantRefund;
	}

	public float getDmServiceFee() {
		return dmServiceFee;
	}

	public void setDmServiceFee(float dmServiceFee) {
		this.dmServiceFee = dmServiceFee;
	}

	public float getDmDeliverFee() {
		return dmDeliverFee;
	}

	public void setDmDeliverFee(float dmDeliverFee) {
		this.dmDeliverFee = dmDeliverFee;
	}

	public float getDdTotalSum() {
		return ddTotalSum;
	}

	public void setDdTotalSum(float ddTotalSum) {
		this.ddTotalSum = ddTotalSum;
	}

	public float getDdOnlinePaymentSum() {
		return ddOnlinePaymentSum;
	}

	public void setDdOnlinePaymentSum(float ddOnlinePaymentSum) {
		this.ddOnlinePaymentSum = ddOnlinePaymentSum;
	}

	public float getDdPlatformRefund() {
		return ddPlatformRefund;
	}

	public void setDdPlatformRefund(float ddPlatformRefund) {
		this.ddPlatformRefund = ddPlatformRefund;
	}

	public float getDdResturantRefund() {
		return ddResturantRefund;
	}

	public void setDdResturantRefund(float ddResturantRefund) {
		this.ddResturantRefund = ddResturantRefund;
	}

	public float getDdServiceFee() {
		return ddServiceFee;
	}

	public void setDdServiceFee(float ddServiceFee) {
		this.ddServiceFee = ddServiceFee;
	}

	public float getDdDeliverFee() {
		return ddDeliverFee;
	}

	public void setDdDeliverFee(float ddDeliverFee) {
		this.ddDeliverFee = ddDeliverFee;
	}

	public float getDkTotalSum() {
		return dkTotalSum;
	}

	public void setDkTotalSum(float dkTotalSum) {
		this.dkTotalSum = dkTotalSum;
	}

	public float getDkOnlinePaymentSum() {
		return dkOnlinePaymentSum;
	}

	public void setDkOnlinePaymentSum(float dkOnlinePaymentSum) {
		this.dkOnlinePaymentSum = dkOnlinePaymentSum;
	}

	public float getDkPlatformRefund() {
		return dkPlatformRefund;
	}

	public void setDkPlatformRefund(float dkPlatformRefund) {
		this.dkPlatformRefund = dkPlatformRefund;
	}

	public float getDkResturantRefund() {
		return dkResturantRefund;
	}

	public void setDkResturantRefund(float dkResturantRefund) {
		this.dkResturantRefund = dkResturantRefund;
	}

	public float getDkServiceFee() {
		return dkServiceFee;
	}

	public void setDkServiceFee(float dkServiceFee) {
		this.dkServiceFee = dkServiceFee;
	}

	public float getDkDeliverFee() {
		return dkDeliverFee;
	}

	public void setDkDeliverFee(float dkDeliverFee) {
		this.dkDeliverFee = dkDeliverFee;
	}

	public int getDeValid() {
		return deValid;
	}

	public void setDeValid(int deValid) {
		this.deValid = deValid;
	}

	public int getDbValid() {
		return dbValid;
	}

	public void setDbValid(int dbValid) {
		this.dbValid = dbValid;
	}

	public int getDmValid() {
		return dmValid;
	}

	public void setDmValid(int dmValid) {
		this.dmValid = dmValid;
	}

	public int getDdValid() {
		return ddValid;
	}

	public void setDdValid(int ddValid) {
		this.ddValid = ddValid;
	}

	public int getDkValid() {
		return dkValid;
	}

	public void setDkValid(int dkValid) {
		this.dkValid = dkValid;
	}
	
	
	
	

	public float getCostRYTL() {
		return costRYTL;
	}

	public void setCostRYTL(float costRYTL) {
		this.costRYTL = costRYTL;
	}

	public float getCostSB() {
		return costSB;
	}

	public void setCostSB(float costSB) {
		this.costSB = costSB;
	}

	public float getCostBC() {
		return costBC;
	}

	public void setCostBC(float costBC) {
		this.costBC = costBC;
	}

	public float getCostHSF() {
		return costHSF;
	}

	public void setCostHSF(float costHSF) {
		this.costHSF = costHSF;
	}

	public float getCostYL() {
		return costYL;
	}

	public void setCostYL(float costYL) {
		this.costYL = costYL;
	}

	public float getCostSF() {
		return costSF;
	}

	public void setCostSF(float costSF) {
		this.costSF = costSF;
	}

	public float getCostDF() {
		return costDF;
	}

	public void setCostDF(float costDF) {
		this.costDF = costDF;
	}

	public float getCostRQF() {
		return costRQF;
	}

	public void setCostRQF(float costRQF) {
		this.costRQF = costRQF;
	}

	public float getCostFF() {
		return costFF;
	}

	public void setCostFF(float costFF) {
		this.costFF = costFF;
	}

	public float getCostGZ() {
		return costGZ;
	}

	public void setCostGZ(float costGZ) {
		this.costGZ = costGZ;
	}

	public float getCostRZ() {
		return costRZ;
	}

	public void setCostRZ(float costRZ) {
		this.costRZ = costRZ;
	}

	public float getCostQT() {
		return costQT;
	}

	public void setCostQT(float costQT) {
		this.costQT = costQT;
	}
	
	

	public float getDazhongaf() {
		return dazhongaf;
	}

	public void setDazhongaf(float dazhongaf) {
		this.dazhongaf = dazhongaf;
	}

	public float getNuomiaf() {
		return nuomiaf;
	}

	public void setNuomiaf(float nuomiaf) {
		this.nuomiaf = nuomiaf;
	}
	
	
	public void addInventoryCost(WFGoods wfg, double cost) {
		if (wfg == null) {
			return;
		}
		synchronized (this) {
			if (inventoryCostMap == null) {
				inventoryCostMap = new HashMap<WFCategory, WFShopInventoryCost>();
			}
		}
		
		WFCategory wfc = wfg.getRootCategory();
		WFShopInventoryCost wic = inventoryCostMap.get(wfc);
		synchronized (inventoryCostMap) {
			if (wic == null) {
				wic = new WFShopInventoryCost(wfc);
				inventoryCostMap.put(wfc, wic);
			}
		}
		wic.addCost(cost);
	}

	public void reset() {
		zls = 0;
		cash = 0;
		groupon = 0;
		delivery = 0;
		nuomi = 0;
		dazhong = 0;
		other = 0;
		ali = 0;
		weixin = 0;

		// group item dazhong
		gdCount = 0;
		gdDje = 0;
		gdGme = 0;
		gdCe = 0;
		gdSjje = 0;
		gdOther = 0;

		// group item meituan
		gmCount = 0;
		gmDje = 0;
		gmGme = 0;
		gmCe = 0;
		gmSjje = 0;
		gmOther = 0;

		// group item nuomi
		gnCount = 0;
		gnDje = 0;
		gnGme = 0;
		gnCe = 0;
		gnSjje = 0;
		gnOther = 0;

		// delivery item elm
		deTotalSum = 0;
		deOnlinePaymentSum = 0;
		dePlatformRefund = 0;
		deResturantRefund = 0;
		deServiceFee = 0;
		deDeliverFee = 0;
		deValid = 0;

		// delivery item baidu
		dbTotalSum = 0;
		dbOnlinePaymentSum = 0;
		dbPlatformRefund = 0;
		dbResturantRefund = 0;
		dbServiceFee = 0;
		dbDeliverFee = 0;
		dbValid = 0;

		// delivery item meituan
		dmTotalSum = 0;
		dmOnlinePaymentSum = 0;
		dmPlatformRefund = 0;
		dmResturantRefund = 0;
		dmServiceFee = 0;
		dmDeliverFee = 0;
		dmValid = 0;

		// delivery item dazhong
		ddTotalSum = 0;
		ddOnlinePaymentSum = 0;
		ddPlatformRefund = 0;
		ddResturantRefund = 0;
		ddServiceFee = 0;
		ddDeliverFee = 0;
		ddValid = 0;

		// delivery item koubei
		dkTotalSum = 0;
		dkOnlinePaymentSum = 0;
		dkPlatformRefund = 0;
		dkResturantRefund = 0;
		dkServiceFee = 0;
		dkDeliverFee = 0;
		dkValid = 0;
		
		//日用调料
		costRYTL = 0;
		//烧饼
		costSB = 0;
		//补菜
		costBC = 0;
		//伙食费
		costHSF = 0;
		//饮料
		costYL = 0;
		//水费
		costSF = 0;
		//电费
		costDF = 0;
		//燃气费
		costRQF = 0;
		//房费
		costFF = 0;
		//工资
		costGZ = 0;
		//日杂
		costRZ = 0;
		//其他
		costQT = 0;
	}

	public WFIncoming buildToWFIncoming() {
		WFIncoming wfi = new WFIncoming();
		wfi.setZls(zls);
		wfi.setCash(cash);
		wfi.setAli(ali);
		wfi.setDazhong(dazhong);
		wfi.setDelivery(delivery);
		wfi.setNuomi(nuomi);
		wfi.setWeixin(weixin);
		wfi.setDate(new Date());
		wfi.setDaZhongaf(dazhongaf);
		wfi.setNuomiaf(nuomiaf);
		wfi.addDeliveryData(DeliveryType.ELM, this.deTotalSum,
				this.deOnlinePaymentSum, this.dePlatformRefund,
				this.deResturantRefund, this.deServiceFee, this.deDeliverFee,
				this.deValid);
		wfi.addDeliveryData(DeliveryType.BAIDU, this.dbTotalSum,
				this.dbOnlinePaymentSum, this.dbPlatformRefund,
				this.dbResturantRefund, this.dbServiceFee, this.dbDeliverFee,
				this.dbValid);
		wfi.addDeliveryData(DeliveryType.DAZHONG, this.ddTotalSum,
				this.ddOnlinePaymentSum, this.ddPlatformRefund,
				this.ddResturantRefund, this.ddServiceFee, this.ddDeliverFee,
				this.ddValid);
		wfi.addDeliveryData(DeliveryType.KOUBEI, this.dkTotalSum,
				this.dkOnlinePaymentSum, this.dkPlatformRefund,
				this.dkResturantRefund, this.dkServiceFee, this.dkDeliverFee,
				this.dkValid);
		wfi.addDeliveryData(DeliveryType.MEIGUAN, this.dmTotalSum,
				this.dmOnlinePaymentSum, this.dmPlatformRefund,
				this.dmResturantRefund, this.dmServiceFee, this.dmDeliverFee,
				this.dmValid);

		wfi.addShopOnItem(GroupOnType.DAZHONG, this.gdCount, this.gdDje,
				this.gdGme, this.gdCe, this.gdSjje, this.gdOther);

		wfi.addShopOnItem(GroupOnType.MEITUAN, this.gmCount, this.gmDje,
				this.gmGme, this.gmCe, this.gmSjje, this.gmOther);

		wfi.addShopOnItem(GroupOnType.NUOMI, this.gnCount, this.gnDje,
				this.gnGme, this.gnCe, this.gnSjje, this.gnOther);

		return wfi;
	}
	
	public WFOperationCost buildOperationCost() {
		WFOperationCost cost = new WFOperationCost();
		//日用调料
		cost.setRytl(costRYTL);
		//烧饼
		cost.setSb(costSB);
		//补菜
		cost.setBc(costBC);
		//伙食费
		cost.setHsf(costHSF);
		//饮料
		cost.setYl(costYL);
		//水费
		cost.setSf(costSF);
		//电费
		cost.setDf(costDF);
		//燃气费
		cost.setRqf(costRQF);
		//房费
		cost.setFf(costFF);
		//工资
		cost.setGz(costGZ);
		//日杂
		cost.setRz(costRZ);
		//其他
		cost.setQt(costQT);
		return cost;
	}
	
	
	public List<WFShopInventoryCost> buildshopInventoryCost() {
		if (inventoryCostMap == null || inventoryCostMap.size() <= 0) {
			return null;
		} else {
			return new ArrayList<WFShopInventoryCost>(inventoryCostMap.values());
		}
	}
}
