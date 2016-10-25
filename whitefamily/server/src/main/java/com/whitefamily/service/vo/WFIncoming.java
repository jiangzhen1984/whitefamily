package com.whitefamily.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.whitefamily.po.incoming.Delivery;
import com.whitefamily.po.incoming.DeliveryType;
import com.whitefamily.po.incoming.GroupOn;
import com.whitefamily.po.incoming.GroupOnType;
import com.whitefamily.po.incoming.Incoming;

public class WFIncoming  {

	
	private Incoming incoming;
	
	private List<DeliveryItem> delis;
	
	private List<GroupOnItem>  groups;
	
	private Date date;
	
	
	
	
	
	
	public WFIncoming() {
		super();
		incoming = new Incoming();
		delis = new ArrayList<DeliveryItem>(5);
		groups = new ArrayList<GroupOnItem>(5);
	}

	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}

	public float getZls() {
		return incoming.getZls();
	}

	public void setZls(float zls) {
		this.incoming.setZls(zls);
	}
	

	public float getCash() {
		return incoming.getCash();
	}

	public void setCash(float cash) {
		this.incoming.setCash(cash);
	}

	public float getWeixin() {
		return incoming.getWeixin();
	}

	public void setWeixin(float weixin) {
		this.incoming.setWeixin(weixin);
	}


	public float getDelivery() {
		return this.incoming.getDelivery();
	}

	public void setDelivery(float delivery) {
		this.incoming.setDelivery(delivery);
	}

	public float getAli() {
		return this.incoming.getAli();
	}

	public void setAli(float ali) {
		this.incoming.setAli(ali);
	}

	public float getNuomi() {
		return this.incoming.getNuomi();
	}
	
	public float getNuomiaf() {
		return this.incoming.getNuomiaf();
	}

	public void setNuomi(float nuomi) {
		this.incoming.setNuomi(nuomi);
	}
	
	public void setNuomiaf(float nuomiaf) {
		this.incoming.setNuomiaf(nuomiaf);
	}

	public float getDazhong() {
		return this.incoming.getDazhong();
	}
	
	public float getDazhongaf() {
		return this.incoming.getDazhongaf();
	}

	public void setDazhong(float dazhong) {
		this.incoming.setDazhong(dazhong);
	}
	
	
	public void setDaZhongaf(float dazhongaf) {
		this.incoming.setDazhongaf(dazhongaf);
	}

	public float getOther() {
		return this.incoming.getOther();
	}

	public void setOther(float other) {
		this.incoming.setOther(other);
	}

	public String getDesc() {
		return this.getDesc();
	}

	public void setDesc(String desc) {
		this.incoming.setDesc(desc);
	}
	
	
	public void addDeliveryData(DeliveryType deliveryType, float incoming,
			float onlinePayment, float refund, float refund1, float serviceFee,
			float deliveryFee, int valid) {
		removeDeliveryItem(deliveryType);
		DeliveryItem d = new DeliveryItem();
		d.setDeliveryType(deliveryType);
		d.setIncoming(incoming);
		d.setOnlinePayment(onlinePayment);
		d.setRefund(refund);
		d.setRefund1(refund1);
		d.setServiceFee(serviceFee);
		d.setValid(valid);
		this.delis.add(d);
	}
	
	
	public void addShopOnItem(GroupOnType groupype, int count, float djq, float gme,
			float ce, float sje, float other) {
		removeGroupOnItem(groupype);
		GroupOnItem  g = new GroupOnItem();
		g.setGroupType(groupype);
		g.setCount(count);
		g.setDjq(djq);
		g.setGme(gme);
		g.setCe(ce);
		g.setSje(sje);
		g.setOther(other);
		this.groups.add(g);
	}
	
	public void removeGroupOnItem(GroupOnType type) {
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getGroupType() == type) {
				groups.remove(i);
				return;
			}
		}
	}
	
	
	public void removeDeliveryItem(DeliveryType type) {
		for (int i = 0; i < delis.size(); i++) {
			if (delis.get(i).getDeliveryType() == type) {
				delis.remove(i);
				return;
			}
		}
	}
	
	
	public DeliveryItem getDeliveryItem(DeliveryType dt) {
		for (int i = 0; i < delis.size(); i++) {
			if (delis.get(i).getDeliveryType() == dt) {
				return delis.get(i);
			}
		}
		return null;
	}
	
	public GroupOnItem getGroupOnItem(GroupOnType gt) {
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getGroupType() == gt) {
				return groups.get(i);
			}
		}
		return null;
	}
	
	
	
	public List<DeliveryItem> getDelis() {
		return delis;
	}


	public List<GroupOnItem> getGroups() {
		return groups;
	}




	public class DeliveryItem extends Delivery {
		
	}
	
	
	public class GroupOnItem extends GroupOn {
		
	}

}
