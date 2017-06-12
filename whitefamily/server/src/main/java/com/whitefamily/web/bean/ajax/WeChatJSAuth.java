package com.whitefamily.web.bean.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@AjaxMapping(name ="jsauth", uri="/jsauth.ajaxj", param="", uriMapping=true)
public class WeChatJSAuth extends AjaxDispatcherJson {

	@Override
	public JSONObject onGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject json = new JSONObject();
		json.put("ret", -1);
		return json;
	}

	@Override
	public JSONObject onPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject json = new JSONObject();
		//TODO get auth information from payment server
		
		json.put("ret", 0);
		json.put("appid", 0);
		json.put("timestamp", 0);
		json.put("nonce", 0);
		json.put("sign", 0);
		
		return json;
	}

}
