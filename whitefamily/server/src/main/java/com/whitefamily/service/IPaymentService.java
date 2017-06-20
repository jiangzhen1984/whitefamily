package com.whitefamily.service;

import com.whitefamily.service.vo.WFOrder;
import com.whitefamily.service.vo.WFPaymentInfo;

public interface IPaymentService {

	
	public void createPaymentTransaction(WFPaymentInfo wfi, WFOrder order);
	
}
