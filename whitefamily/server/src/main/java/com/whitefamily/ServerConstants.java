package com.whitefamily;

import java.util.Properties;

public class ServerConstants {

	
	private static ServerConstants instance;
	
	
	private String deliveryFormPath;
	
	private String deliveryFromContext;
	
	public static ServerConstants getInstance() {
		if (instance == null) {
			instance = new ServerConstants();
		}
		return instance;
	}
	
	
	public void init(Properties prop) {
		deliveryFormPath = prop.getProperty("delivery_form_path");
		deliveryFromContext = prop.getProperty("dr_form_context");
	}
	
	
	public String getDeliveryFormPath() {
		return deliveryFormPath;
	}


	public String getDeliveryFromContext() {
		return deliveryFromContext;
	}
	
	
}
