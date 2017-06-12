package com.whitefamily.web.bean.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@AjaxMapping(name ="version", uri="/version.ajaxj", param="", uriMapping=true)
public class AjaxTestJsonHandler extends AjaxDispatcherJson {

	
	public JSONObject onGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		return null;
	}

	public JSONObject onPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		return null;
	}

}
