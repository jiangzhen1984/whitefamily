package com.whitefamily.web.bean.ajax;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AjaxMapping {
	
	public String name();
	
	public String uri();
	
	public String param();	
	
	public boolean uriMapping();

}
