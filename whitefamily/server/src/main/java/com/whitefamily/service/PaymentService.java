package com.whitefamily.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.whitefamily.po.payment.PaymentInfo;
import com.whitefamily.po.payment.PaymentInfo.PaymentState;
import com.whitefamily.po.payment.PaymentInventoryRecord;
import com.whitefamily.service.vo.WFInventoryRequest;
import com.whitefamily.service.vo.WFPaymentInfo;

public class PaymentService extends BaseService implements IPaymentService {
	
	
	public void createPaymentTransaction(WFPaymentInfo wfi) {
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
		pi.setPs(PaymentState.UNPAY);
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
		
		this.commitTrans();
		wfi.setId(pi.getId());
	}
	
	
	public Result updatePaymentTransaction(WFPaymentInfo wfi) {
		return updatePaymentTrans(wfi, PaymentState.PAIED);
	}
		
	
	public Result closePaymentTransaction(WFPaymentInfo wfi) {
		return updatePaymentTrans(wfi, PaymentState.CLOSED);
	}
	
	
	
	private Result updatePaymentTrans(WFPaymentInfo wfi, PaymentState ps) {
		if (wfi == null || wfi.getId() == null || wfi.getId() <= 0) {
			return Result.ERR_PAYMENT_TRANS_ID_NOT_EXIST;
		}
		Session sess = getSession();
		PaymentInfo pi = (PaymentInfo)sess.get(PaymentInfo.class, wfi.getId());
		if (pi == null) {
			return Result.ERR_PAYMENT_TRANS_ID_NOT_EXIST;
		}
		if (PaymentState.PAIED == ps) {
			pi.setPaymentTime(new Date());
		}
		pi.setPs(ps);
		beginTransaction(sess);
		sess.update(pi);
		commitTrans();
		return Result.SUCCESS;
	}
	
	

}
