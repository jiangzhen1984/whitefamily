package com.whitefamily.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.whitefamily.po.payment.PaymentInfo.PaymentState;
import com.whitefamily.service.IPaymentService;
import com.whitefamily.service.IShopService;
import com.whitefamily.service.ServiceFactory;
import com.whitefamily.service.vo.WFOrder;
import com.whitefamily.service.vo.WFPaymentInfo;


@WebServlet(asyncSupported = false, urlPatterns = { "/payment/notification" } )
public class PaymentNotificationHandler extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5558608495411296386L;
	private Log logger = LogFactory.getLog(this.getClass());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String backData = req.getParameter("back_data");
		String fee = req.getParameter("order_fee");
		String orderSn = req.getParameter("order_no");
		String result = req.getParameter("payment_result");
		if (orderSn != null && !"".equals(orderSn)) {
			IShopService iss = ServiceFactory.getShopService();
			IPaymentService ips = ServiceFactory.getPaymentService();
			WFOrder order = iss.queryOrder(orderSn);
			//TODO check sign
			PaymentState ps;
			if ("0".equals(result)) {
				ps = PaymentState.PAIED;
			} else if ("1".equals(result)) {
				ps = PaymentState.CANCEL;
			} else {
				ps = PaymentState.PAY_ERROR;
			}
			
			WFPaymentInfo wfpi = new WFPaymentInfo();
			ips.createPaymentTransaction(wfpi, order);
			
			logger.info(" orderSn:" + orderSn+"  backData:"+backData+"  update payment result:"+ ps);
		} else {
			logger.error(" No orderSn:" + orderSn+"  backData:"+backData);
		}
		
	}
	
	

}
