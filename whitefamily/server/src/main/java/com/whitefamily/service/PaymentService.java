package com.whitefamily.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.whitefamily.po.payment.PaymentInfo;
import com.whitefamily.po.payment.PaymentInfo.PaymentState;
import com.whitefamily.po.payment.PaymentInventoryRecord;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFOrder;
import com.whitefamily.service.vo.WFPaymentInfo;

public class PaymentService extends BaseService implements IPaymentService {
	
	public void createPaymentTransaction(WFPaymentInfo wfi, WFOrder order) {
		Session sess = getSession();
		Transaction tr = this.beginTransaction(sess);
		PaymentInfo pi = new PaymentInfo();
		pi.setPs(wfi.getPs());
		pi.setCreateTime(new Date());
		pi.setPid(wfi.getPid());
		List<WFInventoryRequest> list = wfi.getRequests();
		if (list != null && list.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for(WFInventoryRequest wir : list) {
				sb.append(wir.getId()).append(",");
			}
			pi.setInventoryIds(sb.toString());
		}
		pi.setPs(wfi.getPs());
		if (pi.getPs() == PaymentState.PAIED) {
			pi.setPaymentTime(new Date());
		}
		sess.save(tr);
		
		PaymentInventoryRecord  pir = null;
		if (list != null && list.size() > 0) {
			for(WFInventoryRequest wir : list) {
				pir = new PaymentInventoryRecord();
				pir.setInvecId(wir.getId());
				pir.setPayId(pi.getId());
				sess.save(pir);
			}
		}
		//TODO update order and payment information
		
		this.commitTrans();
		wfi.setId(pi.getId());
	}

}
