package com.whitefamily.web.bean.ajax;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.whitefamily.ServerConstants;

@AjaxMapping(name ="wxoc", uri="/wxoc.ajaxj", param="", uriMapping=true)
public class WeChatOrderCreate extends AjaxDispatcherJson {

	@Override
	public JSONObject onGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		return null;
	}

	@Override
	public JSONObject onPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject json = new JSONObject();
		int ret = 0;
		String  strOrderNo = request.getParameter("orderno");
		if (strOrderNo == null || "".equals(strOrderNo)) {
			ret = -1;
		} else {
			//TODO get order id
			JSONObject rjson = new JSONObject();
			rjson.put("order_id", "1234567890123");
			rjson.put("order_desc", "aaaaa");
			rjson.put("back_url", "http://payment.wxphome.cn");
			rjson.put("fee", "123");
			rjson.put("back_data", "aaaa");
			
			byte[] rep = PaymentReaderUtil.readPost(ServerConstants.getInstance().getPaymentAddress()+"/order/create?url=" + URLEncoder.encode("http://wf.wxphome.cn/"), rjson.toString().getBytes());
			if (rep == null) {
				json.put("ret", -1);
				return json;
			}
			
			String wxid = null, orderid = null;
			String str = new String(rep, 0, rep.length);
			JSONObject object = (JSONObject) new JSONTokener(str).nextValue();
			if (object.getInt("error") == 0) {
				wxid = object.getString("wxid");
				orderid = object.getString("oid");
				
			} else {
				ret = -1;
			}
			json.put("ret", ret);
			json.put("wxid", wxid);
			json.put("orderid", orderid);
			
			return json;
			
			
		}
		json.put("ret", ret);
		return json;
		
		
		
		
	}
	

}
