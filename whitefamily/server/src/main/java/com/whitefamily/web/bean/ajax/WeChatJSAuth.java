package com.whitefamily.web.bean.ajax;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.whitefamily.ServerConstants;

@AjaxMapping(name ="jsauth", uri="/jsauth.ajaxj", param="", uriMapping=true)
public class WeChatJSAuth extends AjaxDispatcherJson {

	@Override
	public JSONObject onGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		return onPostRequest(request, response);
	}

	@Override
	public JSONObject onPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject json = new JSONObject();
		int ret = 0;		
		byte[] rep = PaymentReaderUtil.readGet(ServerConstants.getInstance().getPaymentAddress()+"/js/auth?url=" + URLEncoder.encode(request.getRequestURL().toString()));
		if (rep == null) {
			json.put("ret", -1);
			return json;
		}
		
		String appid = null, ts = null, nonce = null, s = null;
		String str = new String(rep, 0, rep.length);
		JSONObject object = (JSONObject) new JSONTokener(str).nextValue();
		if (object.getInt("err") == 0) {
			appid = object.getString("appid");
			ts = object.getString("timestamp");
			nonce = object.getString("nonce");
			s = object.getString("sign");
			
		} else {
			ret = -1;
		}
		json.put("ret", ret);
		json.put("appid", appid);
		json.put("timestamp", ts);
		json.put("nonce", nonce);
		json.put("sign", s);
		
		return json;
	}

}
