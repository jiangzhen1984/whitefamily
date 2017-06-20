package com.whitefamily;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerConstants {

	private Log logger = LogFactory.getLog(this.getClass());
	
	private static ServerConstants instance;
	
	
	private String deliveryFormPath;
	
	private String deliveryFromContext;
	
	private String paymentBaseAddress;
	
	private String paymentResultAddress;
	
	private String paymentSubmitAddress;
	
	public static ServerConstants getInstance() {
		if (instance == null) {
			instance = new ServerConstants();
		}
		return instance;
	}
	
	
	public void init(Properties prop) {
		deliveryFormPath = prop.getProperty("delivery_form_path");
		logger.info("===> deliveryFormPath:" + deliveryFormPath);
		deliveryFromContext = prop.getProperty("dr_form_context");
		logger.info("===> deliveryFromContext:" + deliveryFromContext);
		paymentBaseAddress = prop.getProperty("payment_base_address");
		logger.info("===> paymentAddress:" + paymentBaseAddress);
		
		paymentResultAddress = prop.getProperty("payment_result_address");
		logger.info("===> paymentResultAddress:" + paymentResultAddress);
		
		paymentSubmitAddress = prop.getProperty("payment_submit_address");
		logger.info("===> paymentSubmitAddress:" + paymentSubmitAddress);
	}
	
	
	public String getDeliveryFormPath() {
		return deliveryFormPath;
	}


	public String getDeliveryFromContext() {
		return deliveryFromContext;
	}
	
	
	public String getPaymentAddress() {
		return paymentBaseAddress;
	}
	
	public String getPaymentResultAddress() {
		return paymentResultAddress;
	}


	public String getPaymentSubmitAddress() {
		return paymentSubmitAddress;
	}

	
}
