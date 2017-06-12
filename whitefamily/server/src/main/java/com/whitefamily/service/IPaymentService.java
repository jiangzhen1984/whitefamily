package com.whitefamily.service;

import com.whitefamily.service.vo.WFPaymentInfo;

public interface IPaymentService {

	
	public void createPaymentTransaction(WFPaymentInfo wfi);
	
	
	public Result updatePaymentTransaction(WFPaymentInfo wfi);
	
	
	public Result closePaymentTransaction(WFPaymentInfo wfi);
}
