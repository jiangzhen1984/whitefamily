package com.whitefamily.po;

public enum DamageStatus {

	REPORTED;
	
	
	public String getValue() {
		switch (this) {
		case REPORTED:
			return "已提交";
		default:
			return "未知";
		
		}
	}
}
