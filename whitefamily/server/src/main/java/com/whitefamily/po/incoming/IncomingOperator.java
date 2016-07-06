package com.whitefamily.po.incoming;

public enum IncomingOperator {

	PLUS,
	MINUS,
	MULIPLY,
	DIVIDE;
	
	
	public String getValue() {
		switch(this) {
		case DIVIDE:
			return "除";
		case MINUS:
			return "减";
		case MULIPLY:
			return "乘";
		case PLUS:
			return "加";
		default:
			return "未知";
		}
	}
}
